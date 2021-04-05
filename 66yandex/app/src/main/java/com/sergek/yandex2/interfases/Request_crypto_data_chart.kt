package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Crypto_data_chart
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Request_crypto_data_chart {
    @GET
    suspend fun getcrypto_data_chart(@Url url: String?): Response<Crypto_data_chart>
}