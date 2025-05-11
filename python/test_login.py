import os
import time
import pytest
import uiautomator2 as u2

# 測試目標應用包名及 APK 路徑（請將 APK 放在專案根目錄）
PACKAGE = "com.example.myapplication"
APK_PATH = os.path.abspath(os.path.join(os.path.dirname(__file__), "app-debug.apk"))


@pytest.fixture(scope="session")
def d():
    """
    連線至 Android 裝置（模擬器或實體），測試結束後停止應用
    """
    device = u2.connect()
    yield device
    device.app_stop(PACKAGE)


@pytest.fixture(autouse=True)
def clean_app(d):
    """
    每個測試前：停止並移除舊版 App，重新安裝 APK，清除資料
    每個測試後：停止應用，確保測試環境乾淨
    """
    # 停止並卸載舊版 App
    try:
        d.app_stop(PACKAGE)
    except Exception:
        pass
    try:
        d.app_uninstall(PACKAGE)
    except Exception:
        pass

    # 安裝最新版 APK
    assert os.path.exists(APK_PATH), f"APK not found at {APK_PATH}"
    d.app_install(APK_PATH)

    # 清除應用資料
    d.app_clear(PACKAGE)
    # 等待應用安裝與資料清除完成
    time.sleep(2)

    yield
    # 測試後停止應用
    d.app_stop(PACKAGE)


def test_login_success(d):
    """
    測試使用正確帳密登入顯示 Login Success 的 Toast 訊息
    """
    # 啟動應用程式
    d.app_start(PACKAGE)
    time.sleep(2)

    # 輸入使用者名稱與密碼
    d(resourceId="com.example.myapplication:id/etUsername").set_text("admin")
    d(resourceId="com.example.myapplication:id/etPassword").set_text("1234")

    # 點擊登入按鈕
    d(resourceId="com.example.myapplication:id/btnLogin").click()

    # 抓取 Toast 訊息
    toast_message = d.toast.get_message(3.0)
    print("成功登入的 Toast 訊息:", toast_message)

    # 驗證結果
    assert (
        "Login Success" in toast_message
    ), f"預期包含 'Login Success'，但得到: {toast_message}"


def test_login_fail(d):
    """
    測試使用錯誤帳密登入顯示 Login Fail 的 Toast 訊息
    """
    # 啟動應用程式
    d.app_start(PACKAGE)
    time.sleep(2)

    # 輸入錯誤的使用者名稱與密碼
    d(resourceId="com.example.myapplication:id/etUsername").set_text("user")
    d(resourceId="com.example.myapplication:id/etPassword").set_text("wrongpassword")

    # 點擊登入按鈕
    d(resourceId="com.example.myapplication:id/btnLogin").click()

    # 抓取 Toast 訊息
    toast_message = d.toast.get_message(3.0)
    print("登入失敗的 Toast 訊息:", toast_message)

    # 驗證結果
    assert (
        "Login Fail" in toast_message
    ), f"預期包含 'Login Fail'，但得到: {toast_message}"
