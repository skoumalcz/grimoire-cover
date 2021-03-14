package com.skoumal.grimoire.cover.git

import org.gradle.api.internal.provider.DefaultProvider

sealed class GitTagProvider(
    config: GitTagConfig
) : DefaultProvider<GitVersionTag>(
    createVersionTagValue(config)
)


class GitTagSemanticProvider : GitTagProvider(GitTagTaskConfigSemantic)
class GitTagIntegrationProvider : GitTagProvider(GitTagTaskConfigIntegration)


private fun getGitTag(config: GitTagConfig): GitVersionTag {
    val process = ProcessBuilder("git", "describe", "--match", config.pattern, "--long").start()
    val error = process.errorStream.readBytes().decodeToString()

    val longTag = if (error.isNotBlank()) {
        println("Git repo is not initialized or other error occurred")
        "%s%s%s".format(config.prefix ?: "", config.default, config.postfix ?: "")
    } else {
        process.inputStream.readBytes().decodeToString().trim()
    }

    val prefixIndex = config.prefix?.let { longTag.indexOf(it) + it.length } ?: 0
    val postfixIndex = config.postfix?.let { longTag.indexOf(it) } ?: longTag.length

    val versionName = longTag.substring(
        startIndex = prefixIndex,
        endIndex = postfixIndex
    )

    return GitVersionTag(
        versionName = versionName,
        versionCode = config.transformVersionCode(versionName)
    )
}

private fun createVersionTagValue(config: GitTagConfig): () -> GitVersionTag = { getGitTag(config) }