package com.github.radlance.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.common.core.BaseManageResource
import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.data.BaseDataStoreManager
import com.github.radlance.common.data.BaseHandleError
import com.github.radlance.common.data.BaseIgnoreHandle
import com.github.radlance.common.data.BaseUsersRemoteDataSource
import com.github.radlance.common.data.DataStoreManager
import com.github.radlance.common.data.HandleError
import com.github.radlance.common.data.IgnoreHandle
import com.github.radlance.common.data.RemoteUsersRepository
import com.github.radlance.common.data.UsersRemoteDataSource
import com.github.radlance.common.domain.UsersRepository
import com.github.radlance.common.presentation.BaseDispatchersList
import com.github.radlance.common.presentation.BaseRunAsync
import com.github.radlance.common.presentation.DispatchersList
import com.github.radlance.common.presentation.RunAsync
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CoreModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideManageResource(context: Context): ManageResource = BaseManageResource(context)

    @Singleton
    @Provides
    fun provideHandleError(): HandleError = BaseHandleError()

    @Singleton
    @Provides
    fun provideDispatcherList(): DispatchersList = BaseDispatchersList()

    @Singleton
    @Provides
    fun provideDataStore(context: Context): DataStore<Preferences> = context.datastore

    @Singleton
    @Provides
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return BaseDataStoreManager(dataStore)
    }

    @Singleton
    @Provides
    fun provideRunAsync(dispatchersList: DispatchersList): RunAsync {
        return BaseRunAsync(dispatchersList)
    }

    @Provides
    @Singleton
    fun provideUsersRepository(usersRemoteDataSource: UsersRemoteDataSource): UsersRepository {
        return RemoteUsersRepository(usersRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideIgnoreHandle(): IgnoreHandle = BaseIgnoreHandle()

    @Provides
    @Singleton
    fun provideUsersRemoteDataSource(
        service: Service,
        myUser: MyUser
    ): UsersRemoteDataSource = BaseUsersRemoteDataSource(service, myUser)

    companion object {
        private val Context.datastore by preferencesDataStore("settings")
    }
}