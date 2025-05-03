package com.example.myapplication.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingViewModel(private val repository: SettingRepository) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        loadInitialSetting()
    }

    fun loadInitialSetting() {
        _isDarkMode.value = repository.isDarkMode()
    }

    fun setDarkMode(enable: Boolean) {
        repository.saveDarkMode(enable)
        _isDarkMode.value = enable
    }
}