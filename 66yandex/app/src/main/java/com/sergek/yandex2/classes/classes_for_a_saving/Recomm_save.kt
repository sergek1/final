package com.sergek.yandex2.classes.classes_for_a_saving

data class Recomm_save(
        val buy: Int,
        val hold: Int,
        val period: String,
        val sell: Int,
        val strongBuy: Int,
        val strongSell: Int,
        val symbol: String
)