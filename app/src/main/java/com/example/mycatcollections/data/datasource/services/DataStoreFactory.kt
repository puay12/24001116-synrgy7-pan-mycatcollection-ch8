package com.example.mycatcollections.data.datasource.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "session")