import uiautomator2 as u2
import time
from datetime import datetime

def connect_device():
    # 連接裝置
    d = u2.connect()
    app_package = "com.android.settings"
    log(f"啟動 App: {app_package}")
    d.app_start(app_package)
    time.sleep(1)
    return d

def log(message):
    print(f"[{datetime.now().strftime('%H:%M:%S')}] {message}")

def click_if_exists(d, text, timeout=10):
    log(f"等待並點擊『{text}』")
    if d(text=text).wait(timeout=timeout):
        d(text=text).click()
        return True
    else:
        log(f"❌ 找不到『{text}』元件")
        return False

def find_switch(d, texts):
    log("尋找『使用 Wi‑Fi 無線基地台』開關元件")
    for t in texts:
        if d(text=t).exists:
            return d(text=t)
    return None

def click_button(d, text):
    if not click_if_exists(d, text):
        return
    time.sleep(2)


def main():
    try:
        d = connect_device()
        # 導航點擊流程
        click_button(d, "網路與網際網路")
        click_button(d, "無線基地台與網路共用")
        click_button(d, "Wi‑Fi 無線基地台")
        # 處理 Wi‑Fi 開關
        switch_texts = ["使用 Wi‑Fi 無線基地台", "使用 Wi-Fi 無線基地台"]
        switch = find_switch(d, switch_texts)
        if not switch:
            log("❌ 找不到開關元件")
            return
        try:
            if switch.info.get("checked", False): log("✅ 『Wi‑Fi 無線基地台』已經處於開啟狀態")
            else:
                log("🔄 點擊打開『Wi‑Fi 無線基地台』")
                switch.click()
        except Exception as e:
            log(f"⚠️ 無法取得開關狀態，直接嘗試點擊，錯誤訊息: {e}")
        time.sleep(2)
    finally:
        log("🔚 操作完成，返回並關閉設定 App")
        d.press("back")
        time.sleep(1)
        d.app_stop("com.android.settings")


if __name__ == "__main__":
    main()
