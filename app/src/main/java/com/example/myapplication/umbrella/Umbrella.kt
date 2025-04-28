package com.example.myapplication.umbrella

class Umbrella {
    fun totalPrice(count: Int, price: Int, weather: IWeather): Int {
        val isSunny = weather.isSunny()
        var total = count * price
        if (isSunny) {
            total = (total * 0.9).toInt()
        }
        return total
    }
}