package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Main_data_stockItem(
    @SerializedName("currency")
    val currency: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("displaySymbol")
    val displaySymbol: String
)