package com.skoumal.grimoire.cover.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

class GenericAndroidConfigurationOptions(
    override val project: Project,
    override val extension: BaseExtension
) : AndroidConfigurationOptions()