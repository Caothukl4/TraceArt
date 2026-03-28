package com.aktech.ardrawing.utils

import androidx.datastore.preferences.core.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.aktech.ardrawing.di.RemoteKey
import com.google.gson.Gson
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.jvm.javaType

suspend inline fun <reified T : Any> DataStore<Preferences>.saveData(data: T) {
    val clazz = T::class
    val gson = Gson()
    edit { prefs ->
        clazz.memberProperties.forEach { prop ->
            val key = prop.findAnnotation<RemoteKey>()?.key ?: prop.name
            val value = prop.getter.call(data)
            when (value) {
                is String -> prefs[stringPreferencesKey(key)] = value
                is Int -> prefs[intPreferencesKey(key)] = value
                is Long -> prefs[longPreferencesKey(key)] = value
                is Float -> prefs[floatPreferencesKey(key)] = value
                is Double -> prefs[doublePreferencesKey(key)] = value
                is Boolean -> prefs[booleanPreferencesKey(key)] = value
                null -> {
                    // Xoá key nếu value là null
                    val returnType = prop.returnType.classifier
                    when (returnType) {
                        String::class -> prefs.remove(stringPreferencesKey(key))
                        Int::class -> prefs.remove(intPreferencesKey(key))
                        Long::class -> prefs.remove(longPreferencesKey(key))
                        Float::class -> prefs.remove(floatPreferencesKey(key))
                        Double::class -> prefs.remove(doublePreferencesKey(key))
                        Boolean::class -> prefs.remove(booleanPreferencesKey(key))
                    }
                }
                else -> {
                    // Với complex type: chuyển sang JSON
                    val json = gson.toJson(value)
                    prefs[stringPreferencesKey(key)] = json
                }
            }
        }
    }
}

inline fun <reified T : Any> DataStore<Preferences>.getData(): Flow<T?> = data.map { prefs ->
    val clazz = T::class
    val constructor = clazz.constructors.first() // Lấy constructor đầu tiên (thường là primary)
    val gson = Gson()
    // Map các argument cho constructor
    val args = constructor.parameters.associateWith { param ->
        val key = param.findAnnotation<RemoteKey>()?.key ?: param.name ?: return@associateWith null
        val returnType = param.type.classifier

        when (returnType) {
            String::class -> prefs[stringPreferencesKey(key)]
            Int::class -> prefs[intPreferencesKey(key)]
            Long::class -> prefs[longPreferencesKey(key)]
            Float::class -> prefs[floatPreferencesKey(key)]
            Double::class -> prefs[doublePreferencesKey(key)]
            Boolean::class -> prefs[booleanPreferencesKey(key)]
            else -> {
                // Complex type (Map, List, custom data class...) -> deserialize từ JSON string
                val json = prefs[stringPreferencesKey(key)] ?: return@associateWith null
                try {
                    gson.fromJson(json, param.type.javaType)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

            }
        }
    }
    if (args.values.all { it == null }) {
        return@map null
    }
    try {
        constructor.callBy(args)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
