package com.example.datastoreexample

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingManager(cxt: Context?) {

    private val dataStore = cxt?.createDataStore(name = "settings_pref")

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
        val PERSON_NAME = preferencesKey<String>("person_name")
    }


    suspend fun setUiMode(uiMode: UiMode) {
        dataStore?.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    suspend fun setName(name: String) {
        dataStore?.edit {
            it[PERSON_NAME] = name
        }
    }

    val personName = dataStore?.data?.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }?.map {
        it[PERSON_NAME]
    }

    val uiTheme = dataStore?.data?.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }?.map {
        when (it[IS_DARK_MODE] ?: false) {
            true -> UiMode.DARK
            false -> UiMode.LIGHT
        }
    }
}