package com.skoumal.grimoire.cover.git.model

import com.android.build.api.component.ComponentIdentity

interface VersionCodeOverrideAction {

    fun onVersionCode(identity: ComponentIdentity, code: Long): Long

    companion object {

        val default = object : VersionCodeOverrideAction {
            override fun onVersionCode(identity: ComponentIdentity, code: Long): Long {
                return code
            }
        }

    }

}