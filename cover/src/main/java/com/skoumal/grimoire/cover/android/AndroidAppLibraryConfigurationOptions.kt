package com.skoumal.grimoire.cover.android

import com.android.build.gradle.AppExtension
import org.gradle.api.Project

class AndroidAppLibraryConfigurationOptions(
    override val project: Project,
    override val extension: AppExtension
) : AndroidConfigurationOptions()
