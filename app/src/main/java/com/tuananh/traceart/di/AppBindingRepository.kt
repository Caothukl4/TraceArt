package com.tuananh.traceart.di

import com.tuananh.traceart.data.repository.ApiRepositoryImpl
import com.tuananh.traceart.data.repository.CommonFirebaseRepositoryImpl
import com.tuananh.traceart.data.repository.FirebaseFunctionsRepositoryImpl
import com.tuananh.traceart.data.repository.OcrRepositoryImpl
import com.tuananh.traceart.data.repository.TtsRepositoryImpl
import com.tuananh.traceart.domain.repository.ApiRepository
import com.tuananh.traceart.domain.repository.CommonFirebaseRepository
import com.tuananh.traceart.domain.repository.FirebaseFunctionsRepository
import com.tuananh.traceart.domain.repository.OcrRepository
import com.tuananh.traceart.domain.repository.TtsRepository
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