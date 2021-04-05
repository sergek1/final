package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Recomm_trendsItem(
    @SerializedName("buy")
    val buy: Int,
    @SerializedName("hold")
    val hold: Int,
    @SerializedName("period")
    val period: String,
    @SerializedName("sell")
    val sell: Int,
    @SerializedName("strongBuy")
    val strongBuy: Int,
    @SerializedName("strongSell")
    val strongSell: Int,
    @SerializedName("symbol")
    val symbol: String
)