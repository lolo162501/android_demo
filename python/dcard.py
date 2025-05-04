import uiautomator2 as u2
import time

MAX_COUNT = 50

def get_element_texts(d, resource_id, max_attempts=15):
    texts = []
    for index in range(max_attempts):
        try:
            element = d(resourceId=resource_id, instance=index)
            if element.exists:
                info = element.info
                text = info.get("text", "").strip()
                if text:
                    texts.append(text)
            else:
                break
        except u2.exceptions.UiObjectNotFoundError:
            break
    return texts


def main():
    collected_posts = []
    d = u2.connect()
    d.app_start("com.sparkslab.dcardreader")
    time.sleep(5)
    while len(collected_posts) < MAX_COUNT:
        # 抓取 UI 元素文字
        title_texts = get_element_texts(d, "com.sparkslab.dcardreader:id/titleTextView")
        excerpt_texts = get_element_texts(
            d, "com.sparkslab.dcardreader:id/excerptTextView"
        )
        print(f"📌 Found {len(title_texts)} titles, {len(excerpt_texts)} excerpts")
        # 合併與過濾資料
        for title, excerpt in zip(title_texts, excerpt_texts):
            combined = f"{title} - {excerpt}"
            if combined and combined not in collected_posts:
                collected_posts.append(combined)
            if len(collected_posts) >= MAX_COUNT:
                break
        # 滑動螢幕
        if len(collected_posts) < MAX_COUNT:
            d.swipe(0.5, 0.8, 0.5, 0.3, 0.2)
            time.sleep(2)
    print("\n🎉 收集完成，共獲得貼文：")
    for i, p in enumerate(collected_posts, 1):
        print(f"{i}. {p}")

if __name__ == "__main__":
    main()
