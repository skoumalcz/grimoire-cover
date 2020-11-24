package com.skoumal.grimoire.cover.git

import com.android.build.api.component.ComponentIdentity
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.skoumal.grimoire.cover.git.GitTagTaskBuilder.Companion.register
import com.skoumal.grimoire.cover.git.model.VersionOptions
import com.skoumal.grimoire.cover.grimoireDir
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider

/**
 * Builder helps you to create [GitTagTask] hassle free. Make sure to [register] before calling
 * [build].
 *
 * Preferred way of constructing this builder:
 * ```
 * override fun apply(target: Project) {
 *   GitTagTaskBuilder.register(target.extensions)
 *       .buildUponProject(target)
 *       ...
 *       .build()
 * }
 * ```
 *
 * @mandatory [setProjectName] or [setTaskName]
 * @mandatory [setApplicationVariant]
 * @mandatory [setIdentity]
 *
 * @optional [setBuildFile]
 * @optional [setDefaultVersionName]
 * @optional [setDefaultVersionCode]
 * */
@Suppress("UnstableApiUsage")
class GitTagTaskBuilder(
    private val project: Project,
    private val options: VersionOptions? = null
) {

    private lateinit var taskName: String
    private lateinit var variant: ApplicationVariant
    private lateinit var identity: ComponentIdentity

    private var defaultVersionName = "1.0.0"
    private var defaultVersionCode = 0
    private var buildFile: Provider<RegularFile> = project.grimoireDir()
        .map { it.file("git-version") }

    /**
     * Sets the project name and builds default task name. This parameter is mandatory.
     *
     * Example:
     * ```
     * BaseAppModuleExtension.onVariantProperties { variant  ->
     *   GitTagTaskBuilder(project)
     *     ...
     *     .setProjectName(variant.name)
     *     ...
     * }
     * ```
     * */
    fun setProjectName(name: String) = setTaskName("setGitTagFor${name.capitalize()}")

    /**
     * Sets the task name and assigns it to the task created in [build]. This parameter is
     * mandatory, unless set via [setProjectName]
     * */
    fun setTaskName(taskName: String) = apply { this.taskName = taskName }

    /**
     * Sets application variant to be used for output selection. This parameter is mandatory.
     * @see [setProjectName] as example
     * */
    fun setApplicationVariant(variant: ApplicationVariant) = apply { this.variant = variant }

    /**
     * Sets build file to whatever file you want and doesn't check the scope or anything.
     * You can use the [Project.grimoireDir] extension to fetch the library directory.
     * */
    fun setBuildFile(file: Provider<RegularFile>) = apply { buildFile = file }

    /**
     * Sets current identity to the project, this should match the [ApplicationVariant] often than
     * not. It's used for the "versionCodeOverride" to reassign the version code based on the
     * identity provided.
     * This parameter is mandatory.
     * */
    fun setIdentity(identity: ComponentIdentity) = apply { this.identity = identity }

    /**
     * Sets a default version name for cases where version name is not assigned or cannot be
     * fetched for whatever reason.
     * */
    fun setDefaultVersionName(name: String) = apply { defaultVersionName = name }

    /**
     * Sets a default version code for cases where version code is not assigned or cannot be
     * fetched for whatever reason.
     * */
    fun setDefaultVersionCode(code: Int) = apply { defaultVersionCode = code }

    /**
     * Creates a task of type [GitTagTask] and applies its output to [variant]s. If versionName is
     * not defined or [options] are invalid, the default values will be used assigned to the
     * flavor/variant itself. If these are also unassigned, [defaultVersionName] and `0` code is
     * assigned to the variant.
     * */
    fun build() {
        val mainOutput = variant.outputs.single { it.outputType == OutputType.SINGLE }
        val options = options ?: project.extensions.getByType(VersionOptions::class.java)
        val task = project.tasks.register(taskName, GitTagTask::class.java) {
            it.outputFile.set(buildFile)
            it.versionOptions.set(options)
            it.identity.set(identity)
        }

        val defaultName = mainOutput.versionName.getOrElse("1.0.0") ?: "1.0.0"
        val defaultCode = mainOutput.versionCode.getOrElse(0) ?: 0

        val taskOutput = task.map { it.outputFile.runCatching { get().asFile.readLines() } }

        mainOutput.versionName.set(
            taskOutput.map { it.mapCatching { it[0] }.fold({ it }, { defaultName }) }
        )
        mainOutput.versionCode.set(
            taskOutput.map { it.mapCatching { it[1].toInt() }.fold({ it }, { defaultCode }) }
        )
    }

    companion object {

        @JvmStatic
        fun register(container: ExtensionContainer): VersionOptions =
            container.create("versionUpdater", VersionOptions::class.java)

        @JvmStatic
        fun VersionOptions.buildUponProject(project: Project) =
            GitTagTaskBuilder(project, this)

    }

}