package com.github.radlance.kanbanboards.navigation.di

import com.github.radlance.kanbanboards.navigation.data.BaseNavigationRepository
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun provideNavigationRepository(navigationRepository: BaseNavigationRepository): NavigationRepository
}