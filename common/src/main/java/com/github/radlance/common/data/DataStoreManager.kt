package com.github.radlance.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DataStoreManager {

    suspend fun saveAuthorized(authorized: Boolean)

    fun authorized(): Flow<Boolean>
}

internal class BaseDataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreManager {
    override suspend fun saveAuthorized(authorized: Boolean) {
        dataStore.edit { settings ->
            settings[KEY_AUTHORIZED_IN] = authorized
        }
    }

    override fun authorized(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_AUTHORIZED_IN] ?: false
        }
    }

    companion object {
        private val KEY_AUTHORIZED_IN = booleanPreferencesKey("authorized")
    }
}