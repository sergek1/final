package com.sergek.yandex2.interfases

import com.sergek.yandex2.classes.Main_data_stockItem
import retrofit2.Response
import retrofit2.http.GET


interface Reques_main_data_stock {
    @GET("stock/symbol?exchange=US&token=c1a9orv48v6oifh52gig")
    suspend fun getStocks(): Response<List<Main_data_stockItem>>
}
