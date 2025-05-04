from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import time
from datetime import datetime


def main():
    desired_caps = {
        'platformName': 'Android',
        'deviceName': 'emulator-5554',
        'automationName': 'UiAutomator2',
        'appPackage': 'com.android.chrome',
        'appActivity': 'com.google.android.apps.chrome.Main',
        'noReset': True,
        'newCommandTimeout': 180,
        'autoGrantPermissions': True
    }

    print("連接到 Appium 伺服器...")
    options = UiAutomator2Options().load_capabilities(desired_caps)
    driver = webdriver.Remote('http://localhost:4723', options=options)
    print("已成功連接至裝置")

    try:
        time.sleep(3)
        handle_chrome_popups(driver)

        print("點擊搜尋框...")
        click_search_bar(driver)

        search_keyword = "日本料理"
        print(f"輸入搜尋關鍵字: {search_keyword}")
        search_input = find_search_input(driver)
        if search_input:
            search_input.clear()
            search_input.send_keys(search_keyword)
            time.sleep(1)
        else:
            print("找不到搜尋輸入框，使用 set_value 傳入文字")
            driver.set_value(find_search_input(driver), search_keyword)

        print("執行搜尋...")
        driver.press_keycode(66)
        time.sleep(5)

        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        screenshot_path = f"appium_chrome_{search_keyword}_{timestamp}.png"
        driver.get_screenshot_as_file(screenshot_path)
        print(f"已保存搜尋結果截圖: {screenshot_path}")

        print("開始收集搜尋結果...")
        search_results = collect_search_results(driver)

        print(f"\n搜尋「{search_keyword}」的結果:")
        for i, result in enumerate(search_results, 1):
            print(f"{i}. {result}\n")

        result_filename = f"appium_japanese_restaurants_{timestamp}.txt"
        with open(result_filename, "w", encoding="utf-8") as f:
            f.write(f"搜尋「{search_keyword}」的結果 ({datetime.now().strftime('%Y-%m-%d %H:%M:%S')}):\n\n")
            for i, result in enumerate(search_results, 1):
                f.write(f"{i}. {result}\n\n")
        print(f"已將結果保存到文件: {result_filename}")

    except Exception as e:
        print(f"發生錯誤: {e}")
        error_screenshot = f"error_screenshot_{datetime.now().strftime('%Y%m%d_%H%M%S')}.png"
        driver.get_screenshot_as_file(error_screenshot)
        print(f"錯誤截圖已保存: {error_screenshot}")

    finally:
        print("關閉 Chrome 瀏覽器和 Appium 會話...")
        driver.quit()


def handle_chrome_popups(driver):
    try:
        accept_buttons = [
            (AppiumBy.ID, "com.android.chrome:id/terms_accept"),
            (AppiumBy.ID, "com.android.chrome:id/positive_button"),
            (AppiumBy.XPATH, "//*[@text='接受並繼續']"),
            (AppiumBy.XPATH, "//*[@text='Accept & continue']"),
            (AppiumBy.XPATH, "//*[@text='同意']"),
            (AppiumBy.XPATH, "//*[@text='接受']")
        ]

        for by, value in accept_buttons:
            try:
                WebDriverWait(driver, 2).until(EC.element_to_be_clickable((by, value))).click()
                print(f"點擊了接受按鈕: {value}")
                time.sleep(1)
                break
            except TimeoutException:
                continue

        no_thanks_buttons = [
            (AppiumBy.ID, "com.android.chrome:id/negative_button"),
            (AppiumBy.XPATH, "//*[@text='不用了，謝謝']"),
            (AppiumBy.XPATH, "//*[@text='No thanks']"),
            (AppiumBy.XPATH, "//*[@text='否']"),
            (AppiumBy.XPATH, "//*[@text='取消']")
        ]

        for by, value in no_thanks_buttons:
            try:
                WebDriverWait(driver, 2).until(EC.element_to_be_clickable((by, value))).click()
                print(f"點擊了拒絕按鈕: {value}")
                time.sleep(1)
                break
            except TimeoutException:
                continue

        other_buttons = [
            (AppiumBy.XPATH, "//*[@text='略過']"),
            (AppiumBy.XPATH, "//*[@text='稍後']"),
            (AppiumBy.XPATH, "//*[@text='Skip']"),
            (AppiumBy.XPATH, "//*[@text='Later']")
        ]

        for by, value in other_buttons:
            try:
                WebDriverWait(driver, 1).until(EC.element_to_be_clickable((by, value))).click()
                print(f"點擊了按鈕: {value}")
                time.sleep(1)
            except TimeoutException:
                continue

        # 第 1 個新增：略過登入
        try:
            skip_signin = WebDriverWait(driver, 3).until(
                EC.element_to_be_clickable((AppiumBy.XPATH, "//*[@text='在未登入帳戶的狀態下使用']"))
            )
            skip_signin.click()
            print("點擊了『在未登入帳戶的狀態下使用』")
            time.sleep(1)
        except TimeoutException:
            pass

        # 第 2 個新增：廣告隱私頁按下「我瞭解了」
        try:
            got_it_button = WebDriverWait(driver, 3).until(
                EC.element_to_be_clickable((AppiumBy.XPATH, "//*[@text='我瞭解了']"))
            )
            got_it_button.click()
            print("點擊了『我瞭解了』按鈕")
            time.sleep(1)
        except TimeoutException:
            pass

    except Exception as e:
        print(f"處理彈窗時出現例外: {e}")


def click_search_bar(driver):
    search_bar_selectors = [
        (AppiumBy.ID, "com.android.chrome:id/url_bar"),
        (AppiumBy.ID, "com.android.chrome:id/search_box_text"),
        (AppiumBy.XPATH, "//android.widget.EditText[@resource-id='com.android.chrome:id/url_bar']"),
        (AppiumBy.XPATH, "//android.widget.EditText[@resource-id='com.android.chrome:id/search_box_text']"),
        (AppiumBy.XPATH, "//android.widget.EditText[contains(@content-desc, '搜尋')]"),
        (AppiumBy.XPATH, "//android.widget.EditText[contains(@content-desc, 'Search')]")
    ]

    for by, value in search_bar_selectors:
        try:
            element = WebDriverWait(driver, 2).until(EC.element_to_be_clickable((by, value)))
            element.click()
            print(f"成功點擊搜尋框: {value}")
            time.sleep(1)
            return True
        except TimeoutException:
            continue

    # Fallback：如果前面都沒找到，就用 W3C mobile tap 在螢幕上方中間點一下
    print("嘗試使用 mobile: tap 在螢幕上方中間點擊…")
    size = driver.get_window_size()
    x, y = size['width'] // 2, 100
    driver.execute_script('mobile: tap', {'x': x, 'y': y})
    time.sleep(1)
    return False


def find_search_input(driver):
    search_input_selectors = [
        (AppiumBy.ID, "com.android.chrome:id/url_bar"),
        (AppiumBy.ID, "com.android.chrome:id/search_box_text"),
        (AppiumBy.XPATH, "//android.widget.EditText")
    ]

    for by, value in search_input_selectors:
        try:
            element = WebDriverWait(driver, 2).until(EC.presence_of_element_located((by, value)))
            print(f"找到搜尋輸入框: {value}")
            return element
        except TimeoutException:
            continue

    return None


def collect_search_results(driver):
    results = []
    max_results = 30

    selectors = [
        (AppiumBy.XPATH, "//android.view.View[@resource-id='rso']//android.view.View"),
        (AppiumBy.XPATH, "//android.widget.TextView[string-length(@text) > 20]"),
        (AppiumBy.XPATH, "//android.view.View[string-length(@text) > 20]"),
        (AppiumBy.XPATH, "//*[string-length(@text) > 20]")
    ]

    while len(results) < max_results:
        new_item = False

        for by, sel in selectors:
            try:
                elems = driver.find_elements(by, sel)
            except Exception:
                continue
            for e in elems:
                txt = e.text.strip()
                if txt and len(txt) > 15 and not is_navigation_text(txt) and txt not in results:
                    results.append(txt)
                    new_item = True
                    if len(results) >= max_results:
                        break
            if len(results) >= max_results:
                break

        if not new_item:
            break

        # 滑動一屏繼續抓
        if len(results) < max_results:
            try:
                w, h = driver.get_window_size().values()
                driver.swipe(w // 2, h * 3 // 4, w // 2, h // 4, 800)
                time.sleep(2)
            except Exception as e:
                print(f"滑動時出錯: {e}")
                break

    if not results:
        results.append("無法獲取搜尋結果，可能是網頁結構變更或 Appium 限制，請檢查截圖確認。")

    return results


def is_navigation_text(text):
    nav_keywords = ["Chrome", "搜尋", "地圖", "圖片", "新聞", "更多", "http"]
    return any(k in text for k in nav_keywords)


if __name__ == "__main__":
    main()
