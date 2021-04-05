package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Data_newsItem
import com.sergek.yandex2.classes.Main_data_stockItem
import com.sergek.yandex2.classes.Name_and_country
import okhttp3.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface News_request {
    @GET
    suspend fun getnews(@Url url: String?): Response<List<Data_newsItem>>

}