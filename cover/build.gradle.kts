@file:Suppress("LocalVariableName")

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.vanniktech.maven.publish")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {

    val android_version: String by project.ext
    val kotlin_version: String by project.ext

    compileOnly(gradleApi())
    compileOnly("com.android.tools.build:gradle:${android_version}")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")

}