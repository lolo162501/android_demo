package com.example.myapplication.product

class ProductRepository {
    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(): ProductRepository {
            return instance ?: synchronized(this) {
                instance ?: ProductRepository().also { instance = it }
            }
        }
    }

    fun getProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "冷萃咖啡",
                price = 145,
                description = "採用全新的冰咖啡製作方法-冷水萃取，在室溫20小時慢速萃取，使冷萃冰咖啡的口感醇厚而馥郁，有別於傳統用熱水快速沖煮和加冰塊急速降溫製成的冰咖啡。同時，選用專門為冷萃咖啡所調配而成的冷萃綜合咖啡豆，帶有芳香、濃郁滑順的口感，蘊含著獨特的巧克力與柑橘的風味調性，更帶出咖啡本身的甘甜與滑順，是咖啡老饕必嚐的一款星巴克典藏之作!",
                imageUrl = null
            ),
            Product(
                id = 2,
                name = "摩卡可可碎片星冰樂",
                price = 175,
                description = "同樣為暢銷口味，結合星巴克咖啡、乳品、摩卡醬及可可碎片，以鮮奶油與摩卡醬裝飾，多層次口感及濃郁巧克力風味，獨具魅力。",
                imageUrl = null
            ),
            Product(
                id = 3,
                name = "焦糖千層蛋塔",
                price = 125,
                description = "經典的花朵型酥皮千層蛋塔，底部擠上焦香感十足的濃郁焦糖醬，再灌入香濃布蕾蛋液。",
                imageUrl = null
            ),
            Product(
                id = 4,
                name = "巧克力布朗尼",
                price = 65,
                description = "濃郁厚實的巧克力布朗尼蛋糕，上面撒上巧克力碎塊與巧克力醬，會融化於口中。濃郁的巧克力風味非常適合冬天的時節。",
                imageUrl = null
            ),
            Product(
                id = 5,
                name = "經典起司蛋糕",
                price = 120,
                description = "使用濃郁的Cream Cheese及酸奶創造出綿密香濃的乳酪蛋糕，再加上底層的消化餅乾，增添整體口感。",
                imageUrl = null
            )
        )
    }
}