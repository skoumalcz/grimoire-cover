package com.skoumal.grimoire.cover.git

interface GitTagConfig {

    val pattern: String
    val default: String
    val prefix: String?
    val postfix: String?

    fun transformVersionCode(versionName: String): Int

}