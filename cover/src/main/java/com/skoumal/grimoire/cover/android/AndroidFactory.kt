package com.skoumal.grimoire.cover.android

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies

abstract class AndroidFactory {

    protected abstract val project: Project
    protected val plugins inline get() = project.plugins

    abstract fun applyAndroid(): AndroidFactory

    fun applyKotlin() = apply {
        applyPluginSafely(PLUGIN_KOTLIN)
        applyPluginSafely(PLUGIN_KOTLIN_KAPT)
    }

    @Deprecated("Use applyParcelize on projects with kotlin >= 1.4.20")
    fun applyAndroidExtension() = apply {
        applyPluginSafely(PLUGIN_KOTLIN_EXT)
    }

    fun applyParcelize() = apply {
        applyPluginSafely(PLUGIN_KOTLIN_PARCELIZE)
    }

    fun applyPublishing() = apply {
        applyPluginSafely(PLUGIN_MAVEN)
        applyPluginSafely(PLUGIN_BINTRAY)
    }

    /**
     * @param version [Kotlin][LIBRARY_KOTLIN]
     * @param version [Kotlin Reflect][LIBRARY_KOTLIN_REFLECT]
     * */
    fun applyKotlinDependencies(version: String) = apply {
        project.dependencies {
            implementation(LIBRARY_KOTLIN, version)
            implementation(LIBRARY_KOTLIN_REFLECT, version)
        }
    }

    /** @param version [Coroutines Android][LIBRARY_COROUTINES] */
    fun applyCoroutineDependencies(version: String) = apply {
        project.dependencies {
            implementation(LIBRARY_COROUTINES, version)
        }
    }

    /**
     * @param junit [jUnit][LIBRARY_TEST_JUNIT]
     * @param truth [Truth][LIBRARY_TEST_JUNIT]
     * @param mockito [Mockito][LIBRARY_TEST_MOCKITO] & [Mockito Inline][LIBRARY_TEST_MOCKITO_INLINE]
     * */
    fun applyTestDependencies(
        junit: String = "4.+",
        truth: String = "1.+",
        mockito: String = "3.+"
    ) = apply {
        project.dependencies {
            testImplementation(LIBRARY_TEST_JUNIT, junit)
            testImplementation(LIBRARY_TEST_TRUTH, truth)
            testImplementation(LIBRARY_TEST_MOCKITO, mockito)
            testImplementation(LIBRARY_TEST_MOCKITO_INLINE, mockito)
        }
    }

    /** @param version [AndroidX jUnit][LIBRARY_TEST_JUNIT_ANDROID] */
    fun applyAndroidTestDependencies(version: String = "1.+") = apply {
        project.dependencies {
            testImplementation(LIBRARY_TEST_JUNIT_ANDROID, version)
        }
    }

    /** @param version [Coroutines Test][LIBRARY_COROUTINES] */
    fun applyCoroutinesTestDependencies(version: String) = apply {
        project.dependencies {
            testImplementation(LIBRARY_TEST_COROUTINES, version)
        }
    }

    abstract fun build(): AndroidConfigurationOptions

    // ---

    protected fun applyPluginSafely(name: String) {
        if (plugins.findPlugin(name) != null) {
            println("Plugin [name=${name}] has already been applied to project [name=${project.name}]")
            return
        }
        plugins.apply(name)
    }

    private fun DependencyHandlerScope.implementation(notation: String, version: String) =
        add("implementation", "%s:%s".format(notation, version))

    private fun DependencyHandlerScope.testImplementation(notation: String, version: String) =
        add("testImplementation", "%s:%s".format(notation, version))

    companion object {

        const val PLUGIN_APP = "com.android.application"
        const val PLUGIN_LIBRARY = "com.android.library"
        private const val PLUGIN_KOTLIN = "kotlin-android"
        private const val PLUGIN_KOTLIN_KAPT = "kotlin-kapt"
        private const val PLUGIN_KOTLIN_EXT = "kotlin-android-extensions"
        private const val PLUGIN_KOTLIN_PARCELIZE = "kotlin-parcelize"
        private const val PLUGIN_MAVEN = "maven-publish"
        private const val PLUGIN_BINTRAY = "com.jfrog.bintray"

        private const val LIBRARY_KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib"
        private const val LIBRARY_KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
        private const val LIBRARY_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android"

        private const val LIBRARY_TEST_JUNIT = "junit:junit"
        private const val LIBRARY_TEST_JUNIT_ANDROID = "androidx.test.ext:junit"
        private const val LIBRARY_TEST_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test"
        private const val LIBRARY_TEST_TRUTH = "com.google.truth:truth"
        private const val LIBRARY_TEST_MOCKITO = "org.mockito:mockito-core"
        private const val LIBRARY_TEST_MOCKITO_INLINE = "org.mockito:mockito-inline"

    }

}