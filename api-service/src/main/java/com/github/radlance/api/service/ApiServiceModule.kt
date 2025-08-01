package com.github.radlance.api.service

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ApiServiceModule {

    @Binds
    fun provideMyUser(myUser: BaseMyUser): MyUser

    @Binds
    fun provideAuth(auth: BaseAuth): Auth

    @Binds
    fun provideProvideDatabase(provideDatabase: BaseProvideDatabase): ProvideDatabase
}

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Provides
    @Singleton
    fun provideService(provideDatabase: ProvideDatabase): Service {
        return BaseService(provideDatabase.database())
    }
}