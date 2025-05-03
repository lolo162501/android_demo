package com.example.myapplication

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.setting.SettingRepository
import com.example.myapplication.setting.SettingViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SettingTest {
    private lateinit var repository: SettingRepository
    private lateinit var viewModel: SettingViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = SettingRepository(context)
        repository.clear()
        viewModel = SettingViewModel(repository)
    }

    @After
    fun clear(){
        repository.clear()
    }

    @Test
    fun update_DarkMode_UpdatesRepositoryAndViewModel() = runBlocking {
        // Arrange: 準備要設定的值
        val shouldEnableDarkMode = true

        // Act: 透過 ViewModel 設定夜間模式
        viewModel.setDarkMode(shouldEnableDarkMode)

        // Assert 1: 檢查 ViewModel 的狀態是否已更新
        // 使用 .first() 來獲取 StateFlow 的最新值 (在 runBlocking 中)
        assertEquals("ViewModel state should be updated", shouldEnableDarkMode, viewModel.isDarkMode.value)

        // Assert 2: 直接檢查 Repository (也就是 SharedPreferences) 的值是否也被正確儲存
        // 這一步確認了 ViewModel 和 Repository 的整合是成功的
        assertTrue("Repository should have saved the correct value", repository.isDarkMode())
    }

    @Test
    fun load_InitialSetting_ReturnsDefault() = runBlocking {
        // Arrange: 不需要做特別設定，@Before 已經清空 SharedPreferences

        // Act: ViewModel 在 init 時會自動載入，或者我們手動再載入一次確保流程
        viewModel.loadInitialSetting()

        // Assert 1: 檢查 ViewModel 的狀態是否為預設值 (false)
        assertEquals("ViewModel state should be default value", false, viewModel.isDarkMode.value)

        // Assert 2: 檢查 Repository 的值是否也為預設值 (false)
        assertFalse("Repository value should be default", repository.isDarkMode())
    }

    @Test
    fun get_InitialSetting_ReturnsRepositoryValue() = runBlocking {
        // Arrange: 先手動透過 Repository 儲存一個非預設的值
        val savedValue = true
        repository.saveDarkMode(savedValue)

        // Act: 重新建立 ViewModel (模擬 Activity 重建或 App 重啟後載入)
        // 或是直接讓現有 ViewModel 重新載入
        val newViewModel = SettingViewModel(repository) // 創建新實例會觸發 init 中的 loadInitialSetting

        // Assert: 檢查新 ViewModel 的狀態是否為之前儲存的值
        assertEquals("New ViewModel should load the saved value", savedValue, newViewModel.isDarkMode.value)
    }
}