#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import time
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException


def main():
    # 1. 用 UiAutomator2Options 設定 capabilities
    opts = UiAutomator2Options()
    opts.platformName = "Android"
    opts.deviceName = "emulator-5554"  # 請替換為實際裝置序號
    opts.udid = "emulator-5554"
    opts.automationName = "UiAutomator2"
    opts.appPackage = "com.android.chrome"
    opts.appActivity = "com.google.android.apps.chrome.Main"
    opts.noReset = True
    opts.autoGrantPermissions = True

    # 2. 建立 Appium 連線
    print("連接裝置並啟動 App: com.android.chrome")
    driver = webdriver.Remote("http://127.0.0.1:4723/wd/hub", options=opts)
    print("Session ID:", driver.session_id)
    print("當前包／畫面：", driver.current_package, driver.current_activity)
    driver.activate_app("com.android.chrome")
    time.sleep(5)

    try:
        # 等待 Chrome 完全啟動
        time.sleep(5)

        # 處理常見首次開啟彈窗
        if check_and_click(driver, "com.android.chrome:id/signin_fre_dismiss_button"):
            print("在未登入帳戶的狀態下使用")

        if check_and_click(driver, "com.android.chrome:id/ack_button"):
            print("我瞭解了")

        if check_and_click(driver, "com.android.chrome:id/terms_accept"):
            print("點擊「接受並繼續」")

        if check_and_click(driver, "com.android.chrome:id/negative_button"):
            print("點擊「不用了，謝謝」")

        # 點擊搜尋框
        print("點擊搜尋框")
        found_search_box = False

        search_box_ids = [
            "com.android.chrome:id/url_bar",
            "com.android.chrome:id/search_box_text",
        ]

        for search_id in search_box_ids:
            try:
                search_box = WebDriverWait(driver, 5).until(
                    EC.element_to_be_clickable((AppiumBy.ID, search_id))
                )
                search_box.click()
                found_search_box = True
                break
            except TimeoutException:
                continue

        if not found_search_box:
            print("找不到搜尋框元件")
            return

        time.sleep(2)

        # 輸入搜尋關鍵字
        search_keyword = "魚油"
        print(f"輸入搜尋關鍵字：{search_keyword}")

        try:
            edit_text = WebDriverWait(driver, 5).until(
                EC.presence_of_element_located(
                    (AppiumBy.CLASS_NAME, "android.widget.EditText")
                )
            )
            edit_text.clear()
            edit_text.send_keys(search_keyword)
            time.sleep(1)
        except TimeoutException:
            print("無法找到輸入框")
            return

        # 按下搜尋鍵
        print("按下搜尋鍵")
        driver.press_keycode(66)  # Android Enter 鍵碼

        time.sleep(3)

        print(f"成功搜尋「{search_keyword}」")

        # 截圖保存結果
        driver.get_screenshot_as_file("chrome_fish_oil_search_appium.png")
        print("已保存截圖：chrome_fish_oil_search_appium.png")

        # 等待幾秒以便查看搜尋結果
        time.sleep(5)

    finally:
        # 關閉 Chrome 與 Appium 會話
        print("關閉 Chrome 與 Appium 會話")
        driver.quit()


def check_and_click(driver, resource_id, timeout=3):
    """嘗試尋找並點擊具有特定 resource_id 的元素"""
    try:
        element = WebDriverWait(driver, timeout).until(
            EC.element_to_be_clickable((AppiumBy.ID, resource_id))
        )
        element.click()
        time.sleep(1)
        return True
    except TimeoutException:
        return False


if __name__ == "__main__":
    main()
