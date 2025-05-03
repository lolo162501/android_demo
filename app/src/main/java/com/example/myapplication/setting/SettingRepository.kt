package com.example.myapplication.setting

import android.content.Context
import androidx.core.content.edit

class SettingRepository(context: Context) {

    private val prefs = context.getSharedPreferences(
        "app_setting_test",
        Context.MODE_PRIVATE
    )

    companion object {
        const val KEY_DARK_MODE_ENABLED = "KEY_DARK_MODE_ENABLED"
    }

    fun saveDarkMode(enable: Boolean) {
        prefs.edit() { putBoolean(KEY_DARK_MODE_ENABLED, enable) }
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE_ENABLED, false)
    }

    fun clear() {
        prefs.edit() {
            clear()
        }
    }
}