package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Crypto_cItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Request_crypto {
    @GET
    suspend fun getcrypto(@Url url: String?): Response<List<Crypto_cItem>>
}