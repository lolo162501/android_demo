# 匯入所需的模組
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
import time

# 設定 Desired Capabilities
desired_caps = {
    "platformName": "Android",
    "deviceName": "24121JEGR04503",  # 裝置名稱或序號
    "udid": "24121JEGR04503",  # 裝置序號，確保連接到正確的裝置
    "automationName": "UiAutomator2",  # 使用 UiAutomator2 引擎
    "appPackage": "com.android.chrome",  # Chrome 應用的套件名:contentReference[oaicite:5]{index=5}
    "appActivity": "com.google.android.apps.chrome.Main",  # Chrome 主活動:contentReference[oaicite:6]{index=6}
}

# 建立與 Appium Server 的連線 (本機埠 4723)
driver = webdriver.Remote("http://192.168.0.241:4723", options=desired_caps)

# 隱式等待數秒，確保應用啟動和元素可被查找
driver.implicitly_wait(5)

# (可選) 暫停2秒等待 Chrome 首次歡迎頁面彈出
time.sleep(2)

# 檢查並略過 Chrome 首次啟動的提示畫面按鈕
skip_ids = [
    "com.android.chrome:id/signin_fre_dismiss_button",  # 略過登入提示
    "com.android.chrome:id/ack_button",  # 接受提示/確認按鈕
    "com.android.chrome:id/terms_accept",  # 同意條款
    "com.android.chrome:id/negative_button",  # 否定按鈕 (例如不同意/不要同步)
]
for elem_id in skip_ids:
    elements = driver.find_elements(AppiumBy.ID, elem_id)
    if elements:
        elements[0].click()
        time.sleep(1)  # 等待一下再檢查下一個 (確保畫面切換)

# 點擊網址列 (地址/搜尋欄位)
try:
    url_bar = driver.find_element(AppiumBy.ID, "com.android.chrome:id/url_bar")
except:
    url_bar = driver.find_element(AppiumBy.ID, "com.android.chrome:id/search_box_text")
url_bar.click()

# 在網址列輸入搜尋關鍵字「魚油」
url_bar.send_keys("魚油")

# 按下 Enter 鍵執行搜尋 (Android Enter鍵的keycode是66:contentReference[oaicite:7]{index=7})
driver.press_keycode(66)

# 等待搜尋結果載入
time.sleep(3)

# 截圖目前螢幕並存檔
driver.save_screenshot("chrome_fish_oil_search.png")

# 關閉 Chrome 應用程式並結束 session
driver.quit()
