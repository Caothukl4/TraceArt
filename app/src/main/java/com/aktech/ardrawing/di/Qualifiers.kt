package com.aktech.ardrawing.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

/**
 * Annotation để ánh xạ key từ Firebase Remote Config vào thuộc tính của data class.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteKey(val key: String)