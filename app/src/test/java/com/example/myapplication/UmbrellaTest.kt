package com.example.myapplication

import org.junit.Assert
import org.junit.Test

class UmbrellaTest {
//    @Test
//    fun totalPrice() {
//        val umbrella = Umbrella()
//        val actual = umbrella.totalPrice(5, 200)
//        val expected = 900
//        Assert.assertEquals(expected, actual)
//    }

    @Test
    fun totalPrice_sunny() {
        val umbrella = Umbrella()
        val stubWeather = StubWeather()
        stubWeather.fakeIsSunny = true
        val actual = umbrella.totalPrice(5, 200, stubWeather)
        val expected = 900
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun totalPrice_raining() {
        val umbrella = Umbrella()
        val stubWeather = StubWeather()
        stubWeather.fakeIsSunny = false
        val actual = umbrella.totalPrice(5, 200, stubWeather)
        val expected = 1000
        Assert.assertEquals(expected, actual)
    }

}