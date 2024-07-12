package com.chimbori.bugbear

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.reflect.KClass

/** Indicates which [Populator] is responsible for each field in [Report]. */
@Target(PROPERTY)
@Retention(SOURCE)
internal annotation class PopulatedBy(val populator: KClass<out Populator>)
