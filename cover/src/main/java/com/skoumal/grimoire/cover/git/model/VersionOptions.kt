package com.skoumal.grimoire.cover.git.model

open class VersionOptions {
    open var versionType = VersionType.NONE
    open var versionCodeOverride: VersionCodeOverrideAction = VersionCodeOverrideAction.default
}