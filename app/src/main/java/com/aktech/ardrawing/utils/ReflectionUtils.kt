package com.aktech.ardrawing.utils

import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> T.toFirestoreMap(): MutableMap<String, Any?> {
    return T::class.memberProperties
        .associate { it.name to it.get(this) }
        .filterValues { it != null }
        .toMutableMap()
}

inline fun <reified T : Any> T.merge(other: T): T {
    val kClass = T::class
    val constructor = kClass.primaryConstructor!!

    val args = constructor.parameters.associateWith { param ->
        val prop = kClass.memberProperties.first { it.name == param.name }

        val newValue = prop.get(other)
        val oldValue = prop.get(this)

        newValue ?: oldValue
    }

    return constructor.callBy(args)
}