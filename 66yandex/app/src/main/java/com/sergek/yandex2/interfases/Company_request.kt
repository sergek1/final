package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Name_and_country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Company_request {
    @GET
    suspend fun getcompany(@Url url: String?): Response<Name_and_country>

}