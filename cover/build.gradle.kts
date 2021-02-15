plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    signing
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

apply(from = project.rootDir.absolutePath + File.separator + "groovy.build.gradle")