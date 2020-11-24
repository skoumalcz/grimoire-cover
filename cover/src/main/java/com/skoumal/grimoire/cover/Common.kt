package com.skoumal.grimoire.cover

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider

fun Project.grimoireDir(): Provider<Directory> = layout.buildDirectory.dir("com")
    .map { it.dir("skoumal") }
    .map { it.dir("grimoire") }
    .map { it.dir("cover") }