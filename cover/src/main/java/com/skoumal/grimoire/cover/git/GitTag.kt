package com.skoumal.grimoire.cover.git

import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import org.gradle.api.provider.Provider

@Suppress("UnstableApiUsage")
object GitTag {

    @JvmOverloads
    fun into(
        variant: ApplicationVariant,
        provider: Provider<GitVersionTag> = GitTagSemanticProvider()
    ) {
        val output = variant.outputs.single { it.outputType == OutputType.SINGLE }

        output.versionName.set(provider.map { it.versionName })
        output.versionCode.set(provider.map { it.versionCode })
    }

}