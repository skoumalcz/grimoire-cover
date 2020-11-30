package com.skoumal.grimoire.cover.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

class AndroidLibraryConfigurationOptions(
    override val project: Project,
    override val extension: LibraryExtension
) : AndroidConfigurationOptions()
