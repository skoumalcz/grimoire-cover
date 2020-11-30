package com.skoumal.grimoire.cover.gradle

/**
 * Generator will help you to register your plugin correctly with no need to look stuff up.
 * Add this to your **root** build.gradle:
 *
 * ```groovy
 * import com.skoumal.grimoire.cover.gradle.BuildRegistryGenerator
 *
 * BuildRegistryGenerator
 *      .setPluginName("my.super.awesome.plugin")
 *      .setClass("com.example.my.package.MyPlugin")
 *      .build()
 * ```
 *
 * And sync your project. Go to your build pane and follow the instructions.
 * */
object BuildRegistryGenerator {

    private var name: String = "project.build"
    private lateinit var startClass: String

    @JvmStatic
    fun setPluginName(name: String) = apply { this.name = name }

    @JvmStatic
    fun setClass(cls: Class<*>) = apply { startClass = cls.name }

    @JvmStatic
    fun setClass(cls: String) = apply { startClass = cls }

    @JvmStatic
    fun build() {
        println("Copy / Paste the following to your {project}/buildSrc/build.gradle.src:")
        println(
            """gradlePlugin {
    plugins {
        register("$name") {
            id = "$name"
            implementationClass = "$startClass"
        }
    }
}"""
        )
    }

}
