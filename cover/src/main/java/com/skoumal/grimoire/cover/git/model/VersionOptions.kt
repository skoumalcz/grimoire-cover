package com.skoumal.grimoire.cover.git.model

open class VersionOptions {
    open var versionType = VersionType.NONE
    internal var versionCodeOverride: (VersionCodeOverrideAction) = { it }

    fun versionCodeOverride(action: VersionCodeOverrideAction) {
        versionCodeOverride = action
    }
}