package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Recomm_trendsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Request_recomm {
    @GET
    suspend fun getrecomm(@Url url: String?): Response<List<Recomm_trendsItem>>
}