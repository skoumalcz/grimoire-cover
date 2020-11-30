package com.skoumal.grimoire.cover.android

import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CompileOptions
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class AndroidConfigurationOptions {

    protected abstract val project: Project
    protected abstract val extension: BaseExtension

    fun setTargetSdk(sdkInt: Int) = apply {
        extension.defaultConfig.targetSdk = sdkInt
        setCompileSdk(sdkInt)
    }

    fun setCompileSdk(sdkInt: Int) = apply {
        extension.compileSdkVersion(sdkInt)
    }

    fun setMinSdk(sdkInt: Int) = apply {
        extension.defaultConfig.minSdk = sdkInt
    }

    fun addBuildType(type: String, body: BuildType.() -> Unit) = apply {
        extension.buildTypes.maybeCreate(type).body()
    }

    fun compileOptions(body: CompileOptions.() -> Unit) = apply {
        extension.compileOptions(body)
    }

    fun kotlinOptions(body: KotlinJvmOptions.() -> Unit) = apply {
        project.tasks.withType(KotlinCompile::class.java).all {
            kotlinOptions(body)
        }
    }

    fun useExtension(body: (BaseExtension) -> Unit) = apply { body(extension) }

    companion object {

        fun by(
            project: Project,
            extension: BaseExtension
        ) = when (extension) {
            is LibraryExtension -> AndroidLibraryConfigurationOptions(project, extension)
            is AppExtension -> AndroidAppLibraryConfigurationOptions(project, extension)
            else -> GenericAndroidConfigurationOptions(project, extension)
        }

    }

}

fun <Config : AndroidConfigurationOptions> Config.applyJavaVersion(version: JavaVersion) = apply {
    compileOptions {
        sourceCompatibility = version
        targetCompatibility = version
    }
    kotlinOptions {
        jvmTarget = version.name
    }
}