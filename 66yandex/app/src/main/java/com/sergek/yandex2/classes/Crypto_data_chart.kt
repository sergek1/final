package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Crypto_data_chart(
        @SerializedName("c")
    val c: List<Double>,
        @SerializedName("h")
    val h: List<Double>,
        @SerializedName("l")
    val l: List<Double>,
        @SerializedName("o")
    val o: List<Double>,
        @SerializedName("s")
    val s: String,
        @SerializedName("t")
    val t: List<Int>,
        @SerializedName("v")
    val v: List<Double>
)