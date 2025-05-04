import uiautomator2 as u2
import time

def test_login_success(d):
    package = "com.example.myapplication"
    # 啟動應用程式
    d.app_start(package)
    time.sleep(2)
    # 輸入正確的使用者名稱與密碼
    d(resourceId="com.example.myapplication:id/etUsername").set_text("admin")
    d(resourceId="com.example.myapplication:id/etPassword").set_text("1234")
    # 點擊登入按鈕
    d(resourceId="com.example.myapplication:id/btnLogin").click()
    # 抓取 Toast 訊息
    toast_message = d.toast.get_message(3.0)
    print("成功登入的 Toast 訊息:", toast_message)
    # 使用部分匹配檢查是否包含預期文字
    assert "Login Success" in toast_message, f"預期包含 'Login Success'，但得到: {toast_message}"

def test_login_fail(d):
    package = "com.example.myapplication"
    # 重新啟動應用程式以確保獨立測試
    d.app_stop(package)
    d.app_start(package)
    # 輸入錯誤的使用者名稱或密碼
    d(resourceId="com.example.myapplication:id/etUsername").set_text("user")
    d(resourceId="com.example.myapplication:id/etPassword").set_text("wrongpassword")
    # 點擊登入按鈕
    d(resourceId="com.example.myapplication:id/btnLogin").click()
    # 抓取 Toast 訊息
    toast_message = d.toast.get_message(3.0)
    print("登入失敗的 Toast 訊息:", toast_message)
    # 使用部分匹配檢查是否包含預期文字
    assert "Login Fail" in toast_message, f"預期包含 'Login Fail'，但得到: {toast_message}"

if __name__ == "__main__":
    # 連接設備
    d = u2.connect()
    try:
        test_login_success(d)
        print("test_login_success 測試通過")
    except Exception as e:
        print("test_login_success 測試失敗:", e)
    d.app_stop("com.example.myapplication")
    time.sleep(5)
    try:
        test_login_fail(d)
        print("test_login_fail 測試通過")
    except Exception as e:
        print("test_login_fail 測試失敗:", e)
