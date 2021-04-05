package fragments

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.*
import com.google.gson.GsonBuilder
import com.sergek.yandex2.R
import com.sergek.yandex2.classes.DB_classes.DB_charts
import com.sergek.yandex2.classes.classes_for_a_saving.Recomm_save
import com.sergek.yandex2.databinding.ListOfTrendsBinding
import com.sergek.yandex2.interfases.Request_recomm
import fragments.adapters.Adapter_trends
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class Stocks_Trends_Fragment( symbol_stock: String): Fragment() {

    companion object{
        lateinit var dbCharts: DB_charts
    }
    var index_on = false


    var symbol_stock_1 = symbol_stock

    var item_bar : ArrayList<Recomm_save> = ArrayList()

    private var adapter_trends : Adapter_trends = Adapter_trends(item_bar)

    private lateinit var binding: ListOfTrendsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       binding = ListOfTrendsBinding.inflate(layoutInflater)

        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        binding.listTrends.layoutManager = layoutManager
        binding.listTrends.setHasFixedSize(true)
        val dividerItemDecoration =
                DividerItemDecoration(binding.listTrends.context, layoutManager.orientation)
        binding.listTrends.addItemDecoration(dividerItemDecoration)

        dbCharts = DB_charts(view!!.context, null,1)

        getdatacharts()




    }


    private fun getdatacharts(){
        if (dbCharts.getcouncharts() == 0 && index_on == false) {


            isOnline(view!!.context)

        }
        else{
            item_bar = ArrayList()
            item_bar.addAll(dbCharts.getcharts(view!!.context))
            adapter_trends = Adapter_trends(item_bar)
            adapter_trends.notifyDataSetChanged()
            binding.listTrends.adapter = adapter_trends
            if (dbCharts.getcouncharts() == 0){
                binding.noChart.text = "No recommendations"
            }

        }
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    runBlocking {
                        withContext(Dispatchers.IO){
                            parseTrends(symbol_stock_1)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    runBlocking {
                        withContext(Dispatchers.IO){
                            parseTrends(symbol_stock_1)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    runBlocking {
                        withContext(Dispatchers.IO){
                            parseTrends(symbol_stock_1)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")

                    return true
                }
            }
        }
        Toast.makeText(view!!.context, "  No internet, please check your internet connection ", Toast.LENGTH_LONG).show()
        return false
    }


    private  suspend fun parseTrends(symbol_stock: String) {
        try{

            val comp_url = "stock/recommendation?symbol=$symbol_stock&token=c1a9orv48v6oifh52gig"

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
//
            // Create Service
            val service = retrofit.create(Request_recomm::class.java)
            CoroutineScope(Dispatchers.IO).launch {

                // Do the GET request and get response
                val response = service.getrecomm(comp_url)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val items = response.body()
                        if (items != null) {
                            for (i in 0 until items.count()) {

                                val  buy_1 = items[i].buy
                                Log.d("buy : ", buy_1.toString() )

                                val  hold_1 = items[i].hold
                                Log.d("hold: ",hold_1.toString() )

                                val period_1 = items[i].period
                                Log.d("period : ",period_1 )

                                val sell_1 = items[i].sell
                                Log.d("sell : ",sell_1.toString() )

                                val  strongBuy_1= items[i].strongBuy
                                Log.d("strongBuy : ",strongBuy_1.toString() )

                                val  strongSell_1 = items[i].strongSell
                                Log.d("strongSell : ", strongSell_1.toString())

                                val  symbol_1 = items[i].symbol
                                Log.d("symbol : ",symbol_1 )

                                val model_1 = Recomm_save(buy_1,hold_1,period_1,sell_1,strongBuy_1,strongSell_1,symbol_1)
                                item_bar.add(model_1)

                                adapter_trends = Adapter_trends(item_bar)
                                adapter_trends.notifyDataSetChanged()
                            }

                            dbCharts.addcharts(view!!.context, item_bar)

                            binding.listTrends.adapter = adapter_trends
                        }
                        if (items?.count() == 0){
                            binding.noChart.text = "No recommendations"
                        }


                    }
                else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        }catch (e: Exception){
            Toast.makeText(view!!.context,"No answer from server , $e", Toast.LENGTH_SHORT).show()
        }
        index_on = true
    }

}