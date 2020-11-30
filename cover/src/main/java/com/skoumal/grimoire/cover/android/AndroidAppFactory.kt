package com.skoumal.grimoire.cover.android

import com.android.build.gradle.AppExtension
import org.gradle.api.Project

class AndroidAppFactory(
    override val project: Project
) : AndroidFactory() {

    override fun applyAndroid() = apply {
        applyPluginSafely(PLUGIN_APP)
    }

    override fun build(): AndroidConfigurationOptions {
        val extension = project.extensions.getByName("android") as AppExtension
        return AndroidConfigurationOptions.by(project, extension)
    }

}
