package com.skoumal.grimoire.cover.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

class AndroidLibraryFactory(
    override val project: Project
) : AndroidFactory() {

    override fun applyAndroid() = apply {
        applyPluginSafely(PLUGIN_LIBRARY)
    }

    override fun build(): AndroidConfigurationOptions {
        val extension = project.extensions.getByName("android") as LibraryExtension
        return AndroidConfigurationOptions.by(project, extension)
    }

}
