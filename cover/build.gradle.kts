plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.jfrog.bintray")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

apply(from = project.rootDir.absolutePath + File.separator + "groovy.build.gradle")