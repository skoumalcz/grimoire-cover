plugins {
    `java-gradle-plugin`
    `kotlin-dsl` version "1.4.4"
    `maven-publish`
    id("com.jfrog.bintray")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

apply(from = "groovy.build.gradle")