package com.skoumal.grimoire.cover.git

import com.android.build.api.component.ComponentIdentity
import com.skoumal.grimoire.cover.git.model.VersionOptions
import com.skoumal.grimoire.cover.git.model.VersionType
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GitTagTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    abstract val versionOptions: Property<VersionOptions>
    abstract val identity: Property<ComponentIdentity>

    private val _versionOptions inline get() = versionOptions.get()
    private val config: Config?
        inline get() = when (_versionOptions.versionType) {
            VersionType.SEMANTIC -> GitTagTaskConfigSemantic
            VersionType.INTEGRATION -> GitTagTaskConfigIntegration
            else -> null
        }

    @TaskAction
    @OptIn(ExperimentalStdlibApi::class)
    fun taskAction() {
        val config = config ?: return writeResult()
        val process = ProcessBuilder("git", "describe", "--match", config.pattern, "--long").start()
        val error = process.errorStream.readBytes().decodeToString()

        val longTag = if (error.isNotBlank()) {
            println("Git repo is not initialized or other error occurred")
            "%s%s%s".format(config.prefix ?: "", config.default, config.postfix ?: "")
        } else {
            process.inputStream.readBytes().decodeToString().trim()
        }

        val prefixIndex = config.prefix?.let { longTag.indexOf(it) + it.length } ?: 0
        val postfixIndex = config.postfix?.let { longTag.indexOf(it) } ?: longTag.length

        val versionName = longTag.substring(
            startIndex = prefixIndex,
            endIndex = postfixIndex
        )
        val versionCode = _versionOptions.versionCodeOverride.onVersionCode(
            identity = identity.get(),
            code = config.transformVersionCode(versionName)
        )

        @Suppress("DEPRECATION")
        return writeResult(
            StringBuilder()
                .appendln(versionName)
                .append(versionCode)
                .toString()
        )
    }

    private fun writeResult(result: String = "") {
        outputFile.get().asFile.writeText(result)
    }

    interface Config {

        val pattern: String
        val default: String
        val prefix: String?
        val postfix: String?

        fun transformVersionCode(versionName: String): Long

    }

}