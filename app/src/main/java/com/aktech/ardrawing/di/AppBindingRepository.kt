package com.aktech.ardrawing.di

import com.aktech.ardrawing.data.repository.ApiRepositoryImpl
import com.aktech.ardrawing.data.repository.CommonFirebaseRepositoryImpl
import com.aktech.ardrawing.data.repository.FirebaseFunctionsRepositoryImpl
import com.aktech.ardrawing.data.repository.OcrRepositoryImpl
import com.aktech.ardrawing.data.repository.TtsRepositoryImpl
import com.aktech.ardrawing.domain.repository.ApiRepository
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import com.aktech.ardrawing.domain.repository.FirebaseFunctionsRepository
import com.aktech.ardrawing.domain.repository.OcrRepository
import com.aktech.ardrawing.domain.repository.TtsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingRepository {

    @Binds
    abstract fun bindApiRepository(
        impl: ApiRepositoryImpl
    ): ApiRepository

    @Binds
    abstract fun bindCommonFirebaseRepository(
        impl: CommonFirebaseRepositoryImpl
    ): CommonFirebaseRepository

    @Binds
    abstract fun bindOcrRepository(
        impl: OcrRepositoryImpl
    ): OcrRepository

    @Binds
    abstract fun bindTtsRepository(
        impl: TtsRepositoryImpl
    ): TtsRepository

    @Binds
    abstract fun bindFirebaseFunctionsRepository(
        impl: FirebaseFunctionsRepositoryImpl
    ): FirebaseFunctionsRepository

}