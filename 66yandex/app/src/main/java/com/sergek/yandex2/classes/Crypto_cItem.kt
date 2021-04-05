package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Crypto_cItem(
    @SerializedName("description")
    val description: String,
    @SerializedName("displaySymbol")
    val displaySymbol: String,
    @SerializedName("symbol")
    val symbol: String
)