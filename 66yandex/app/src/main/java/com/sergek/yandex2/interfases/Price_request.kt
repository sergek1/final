package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Name_and_country
import com.sergek.yandex2.classes.Price_company
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Price_request {
    @GET
    suspend fun getPrice(@Url url: String?): Response<Price_company>
}