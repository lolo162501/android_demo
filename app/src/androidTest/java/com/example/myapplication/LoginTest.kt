package com.example.myapplication

import android.app.UiAutomation
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    private lateinit var device: UiDevice
    private val TIMEOUT: Long = 3000  // 等待超時時間設為 3000 毫秒
    private val packageName = "com.example.myapplication"

    @Before
    fun setUp() {
        // 啟動應用
        val context = InstrumentationRegistry.getInstrumentation().context
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context.packageManager.getLaunchIntentForPackage(packageName)?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(it)
        }
    }

    @Test
    fun testSuccessfulLogin() {
        val usernameField = device.wait(
            Until.findObject(By.res(packageName, "etUsername")),
            TIMEOUT
        )
        usernameField?.text = "admin"

        val passwordField = device.wait(
            Until.findObject(By.res(packageName, "etPassword")),
            TIMEOUT
        )
        passwordField?.text = "1234"

        val loginButton = device.wait(
            Until.findObject(By.res(packageName, "btnLogin")),
            TIMEOUT
        )

        // 利用 UiAutomation 監聽 Toast 的 AccessibilityEvent
        val uiAutomation: UiAutomation =
            InstrumentationRegistry.getInstrumentation().uiAutomation

        val toastEvent = uiAutomation.executeAndWaitForEvent(
            { loginButton?.click() },
            { event: AccessibilityEvent ->
                event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED &&
                        event.text.toString().contains("Login Success")
            },
            TIMEOUT
        )
        assertNotNull("Successful login toast not displayed", toastEvent)
    }

    @Test
    fun testFailedLogin() {
        val usernameField = device.wait(
            Until.findObject(By.res(packageName, "etUsername")),
            TIMEOUT
        )
        usernameField?.text = "user"

        val passwordField = device.wait(
            Until.findObject(By.res(packageName, "etPassword")),
            TIMEOUT
        )
        passwordField?.text = "wrongpassword"

        val loginButton = device.wait(
            Until.findObject(By.res(packageName, "btnLogin")),
            TIMEOUT
        )

        // 利用 UiAutomation 監聽 Toast 的 AccessibilityEvent
        val uiAutomation: UiAutomation =
            InstrumentationRegistry.getInstrumentation().uiAutomation

        val toastEvent = uiAutomation.executeAndWaitForEvent(
            { loginButton?.click() },
            { event: AccessibilityEvent ->
                event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED &&
                        event.text.toString().contains("Login Fail")
            },
            TIMEOUT
        )
        assertNotNull("Failed login toast not displayed", toastEvent)
    }
}
