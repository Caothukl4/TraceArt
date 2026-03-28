package com.aktech.ardrawing.utils

import com.aktech.ardrawing.di.RemoteKey
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> FirebaseRemoteConfig.getConfig(): T {
    val clazz = T::class
    val constructor = clazz.primaryConstructor
        ?: throw IllegalArgumentException("Class must have primary constructor")

    val args = constructor.parameters.associateWith { param ->
        val key = param.findAnnotation<RemoteKey>()?.key ?: param.name
        ?: throw IllegalArgumentException("Param ${param} must have a name or @RemoteKey")

        when (param.type.classifier) {
            Boolean::class -> getBoolean(key)
            Int::class -> getLong(key).toInt()
            Long::class -> getLong(key)
            Double::class -> getDouble(key)
            String::class -> getString(key)
            List::class, Array<String>::class -> {
                val json = getString(key)
                try {
                    val type = object : TypeToken<List<String>>() {}.type
                    Gson().fromJson<List<String>>(json, type)
                } catch (_: Exception) {
                    null
                }
            }

            else -> null
        }
    }

    return constructor.callBy(args)
}