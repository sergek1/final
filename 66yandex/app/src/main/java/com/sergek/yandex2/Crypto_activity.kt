package com.sergek.yandex2

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.GsonBuilder
import com.sergek.yandex2.classes.classes_for_a_saving.Crypto_data_chart_save
import com.sergek.yandex2.databinding.CryptoChartsBinding
import com.sergek.yandex2.interfases.Request_crypto_data_chart
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Crypto_activity: AppCompatActivity() {

    var crypto_symbol = ""

    private lateinit var binding: CryptoChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CryptoChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        crypto_symbol = intent.extras?.getString("symbol_crypto", "no found symbol") ?: "NULL"
        isOnline(baseContext)


    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    parsecrypto(crypto_symbol)
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    parsecrypto(crypto_symbol)
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    parsecrypto(crypto_symbol)
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")

                    return true
                }
            }
        }
        Toast.makeText(baseContext, "  No internet, please check your internet connection ", Toast.LENGTH_LONG).show()
        return false
    }

    private  fun parsecrypto(symbol_crypto :String) {


        val to: Long = Date().time/1000
        val from = to - 3450000
        try{

            val conn_comp = "crypto/candle?symbol=$symbol_crypto&resolution=D&from=$from&to=$to&token=c1a9orv48v6oifh52gig"



            val clientSetup = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                    .readTimeout(10, TimeUnit.MINUTES) // read timeout
                    .build()
            // Create Retrofit
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://finnhub.io/api/v1/")
                    .client(clientSetup)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            // Create Service
            val service = retrofit.create(Request_crypto_data_chart::class.java)

            val job = CoroutineScope(Dispatchers.IO)
            job.launch {

                // Do the GET request and get response
                val response = service.getcrypto_data_chart(conn_comp)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library

                        val gson = GsonBuilder().setPrettyPrinting().create()

                        val  c_12= response.body()?.c ?: listOf()


                        val  h_12= response.body()?.h ?: listOf()


                        val  l_12= response.body()?.l ?: listOf()


                        val  o_12= response.body()?.o ?: listOf()


                        val  t_12= response.body()?.t ?: listOf()


                        val  v_12= response.body()?.v ?: listOf()
                        Log.d("v : ",v_12.toString() )

                        val item_candle = Crypto_data_chart_save(c_12, h_12, l_12, o_12, t_12, v_12)

                        if (item_candle.c.size != 0) {
                            val lin_c = ArrayList<Entry>()
                            val lin_h = ArrayList<Entry>()

                            val xvalue = ArrayList<String>()

                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                            for (ii in 0 until item_candle.c.size) {
                                lin_c.add(Entry(item_candle.c[ii].toFloat(), ii + 1))
                                lin_h.add(Entry(item_candle.h[ii].toFloat(), ii + 2))
                                xvalue.add(sdf.format(Date(item_candle.t[ii] * 1000.toLong())))
                            }

                            val linedataset_c = LineDataSet(lin_c, "Currency Price, $")
                            val linedataset_h = LineDataSet(lin_h, "High Price, $")

                            linedataset_c.color = Color.BLACK
                            linedataset_h.color = Color.RED
//                            linedataset_v.color = Color.BLUE

                            linedataset_c.lineWidth = 2f
                            linedataset_h.lineWidth = 2f

                            linedataset_h.mode = LineDataSet.Mode.CUBIC_BEZIER
                            linedataset_c.mode = LineDataSet.Mode.CUBIC_BEZIER


                            linedataset_h.setDrawCircles(false)
                            linedataset_c.setDrawCircles(false)


                            var finaldata = ArrayList<LineDataSet>()
                            finaldata.add(linedataset_c)
                            finaldata.add(linedataset_h)

                            val data = LineData(xvalue, finaldata as List<LineDataSet>?)
                            data.setValueTextSize(15f)



                            binding.lineCryptoChart.data = data
                            binding.lineCryptoChart.animateXY(300, 300)
                            binding.lineCryptoChart.xAxis.textSize = 10f
                            binding.lineCryptoChart.axisLeft.textSize = 15f
                            binding.lineCryptoChart.axisRight.textSize = 15f

                            val legend: Legend = binding.lineCryptoChart.legend
                            legend.textSize = 15f
                            legend.position = Legend.LegendPosition.LEFT_OF_CHART
                        }
                        else{
                            binding.noCandles.text = "No candles data "
                        }


                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
                delay(1L)
                job.cancel()
            }
        } catch (e: java.lang.NullPointerException){
            Toast.makeText(baseContext, "No answer from server", Toast.LENGTH_LONG).show()
        }
    }


}