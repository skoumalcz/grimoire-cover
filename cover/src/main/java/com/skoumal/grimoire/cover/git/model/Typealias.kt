package com.skoumal.grimoire.cover.git.model

import com.android.build.api.component.ComponentIdentity

typealias VersionCodeOverrideAction = ComponentIdentity.(versionCode: Long) -> Long