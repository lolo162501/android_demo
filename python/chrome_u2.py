import uiautomator2 as u2
import time


def main():
    # 連接裝置（預設使用 USB 連接）
    d = u2.connect()

    # 啟動 Chrome 瀏覽器
    app_package = "com.android.chrome"
    print("啟動 App:", app_package)
    d.app_start(app_package)
    time.sleep(5)

    if d(resourceId="com.android.chrome:id/signin_fre_dismiss_button").exists:
        print("在未登入帳戶的狀態下使用")
        d(resourceId="com.android.chrome:id/signin_fre_dismiss_button").click()
        time.sleep(2)

    if d(resourceId="com.android.chrome:id/ack_button").exists:
        print("我瞭解了")
        d(resourceId="com.android.chrome:id/ack_button").click()
        time.sleep(2)

    if d(resourceId="com.android.chrome:id/terms_accept").exists:
        print("點擊「接受並繼續」")
        d(resourceId="com.android.chrome:id/terms_accept").click()
        time.sleep(2)

    # 如果出現同步處理帳戶的對話框，點擊「不用了，謝謝」
    if d(resourceId="com.android.chrome:id/negative_button").exists:
        print("點擊「不用了，謝謝」")
        d(resourceId="com.android.chrome:id/negative_button").click()
        time.sleep(2)

    # 點擊搜尋框
    print("點擊搜尋框")
    if d(resourceId="com.android.chrome:id/url_bar").wait(timeout=5):
        d(resourceId="com.android.chrome:id/url_bar").click()
    elif d(resourceId="com.android.chrome:id/search_box_text").wait(timeout=5):
        d(resourceId="com.android.chrome:id/search_box_text").click()
    else:
        print("找不到搜尋框元件")
        return

    time.sleep(2)

    # 輸入搜尋關鍵字
    search_keyword = "魚油"
    print(f"輸入搜尋關鍵字：{search_keyword}")
    d.clear_text()
    d.send_keys(search_keyword)
    time.sleep(1)

    # 按下搜尋鍵
    print("按下搜尋鍵")
    # 或者使用鍵盤的Enter鍵
    d.press("enter")

    time.sleep(3)

    print(f"成功搜尋「{search_keyword}」")

    # 可選：截圖保存結果
    d.screenshot("chrome_fish_oil_search.png")
    print("已保存截圖：chrome_fish_oil_search.png")

    # 建議：等待幾秒以便查看搜尋結果
    time.sleep(5)

    # 關閉 Chrome
    print("關閉 Chrome")
    d.app_stop(app_package)


if __name__ == "__main__":
    main()
