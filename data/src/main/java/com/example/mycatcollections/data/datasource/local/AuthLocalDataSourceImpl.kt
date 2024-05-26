package com.example.mycatcollections.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mycatcollections.data.datasource.interfaces.AuthLocalDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : AuthLocalDataSource {
    companion object {
        const val KEY_TOKEN = "token"
        private val DATASTORE_KEY_TOKEN = stringPreferencesKey(KEY_TOKEN)
    }

    private val user = mapOf(
        "username" to "puay124",
        "password" to "Puay1244"
    )

    override suspend fun userLogin(username: String, password: String): String {
        delay(1000)
        if ((username == user.get("username")) && (password == user.get("password"))) {
            return "cuakssssssa6wadidawjiwa"
        } else {
            throw UnsupportedOperationException("Maaf, user ini tidak ditemukan")
        }
    }

    override suspend fun setToken(token: String) {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_TOKEN] = token
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { pref ->
            pref[DATASTORE_KEY_TOKEN]
        }.firstOrNull()
    }

    override suspend fun clearToken() {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_TOKEN] = ""
        }
    }


}