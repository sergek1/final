package com.sergek.yandex2.classes

import com.google.gson.annotations.SerializedName

data class Data_newsItem(
    @SerializedName("category")
    val category: String,
    @SerializedName("datetime")
    val datetime: Int,
    @SerializedName("headline")
    val headline: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("related")
    val related: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("url")
    val url: String
)