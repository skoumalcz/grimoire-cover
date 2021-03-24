package com.skoumal.grimoire.cover.git

import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import org.gradle.api.provider.Provider

@Suppress("UnstableApiUsage")
object GitTag {

    @JvmOverloads
    @JvmStatic
    fun into(
        provider: Provider<GitVersionTag> = GitTagSemanticProvider(),
        variant: ApplicationVariant,
        override: VersionCodeOverride
    ) {
        val output = variant.outputs.single { it.outputType == OutputType.SINGLE }

        output.versionName.set(provider.map { it.versionName })
        output.versionCode.set(provider.map { override.onCodeOverride(it.versionCode) })
    }

    @JvmOverloads
    @JvmStatic
    fun into(
        variant: ApplicationVariant,
        provider: Provider<GitVersionTag> = GitTagSemanticProvider()
    ) = into(provider, variant) { it }

}

fun interface VersionCodeOverride {
    fun onCodeOverride(code: Int): Int
}