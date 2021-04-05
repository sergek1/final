package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Name_and_country(
    @SerializedName("country")
    val country: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("ipo")
    val ipo: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("weburl")
    val weburl: String
)