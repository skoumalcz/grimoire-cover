package com.skoumal.grimoire.cover.gradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

val Project.parentExtra get() = requireNotNull(parent).extra
val Project.nullableParentExtra get() = parent?.extra
