package fragments



import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible


import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.sergek.yandex2.R
import com.sergek.yandex2.classes.DB_classes.DB_main_stock

import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import com.sergek.yandex2.classes.classes_for_a_saving.Price_save
import com.sergek.yandex2.databinding.ListStockFragmentBinding
import com.sergek.yandex2.interfases.Price_request

import com.sergek.yandex2.interfases.Reques_main_data_stock

import fragments.adapters.ListStocksAdapter
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.internal.notify
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class StockFragment() : Fragment() {



    companion object {
        lateinit var db_main_stock: DB_main_stock
    }


    var itemstockArray: ArrayList<Main_stock_save> = ArrayList()
    lateinit var adapter: ListStocksAdapter

    private lateinit var binding: ListStockFragmentBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        binding = ListStockFragmentBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun viewmainstock() {
        binding.progBar.visibility
        if (db_main_stock.getcountstock() < 26600) {
            db_main_stock.cleardbstock()
            isOnline(view!!.context)
        } else {
            itemstockArray = ArrayList()
            itemstockArray.addAll(db_main_stock.getmainstock(view!!.context))
            if (itemstockArray.size > 0) {
                adapter = ListStocksAdapter(itemstockArray)
                binding.listStock.adapter = adapter
                Toast.makeText(view!!.context, " Found ${itemstockArray.size} stocks", Toast.LENGTH_LONG).show()

            }
            binding.progBar.isGone


        }
    }
        private fun setupRecyclerView() {
            val layoutManager = LinearLayoutManager(view?.context)

            binding.listStock.layoutManager = layoutManager
            binding.listStock.setHasFixedSize(true)
            val dividerItemDecoration =
                    DividerItemDecoration(binding.listStock.context, layoutManager.orientation)
            view?.context?.let {
                ContextCompat.getDrawable(it, R.drawable.line_divider)
                        ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
            }
            binding.listStock.addItemDecoration(dividerItemDecoration)

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            db_main_stock = DB_main_stock(view!!.context, null, 1)

            viewmainstock()
            val layoutManager = LinearLayoutManager(view?.context)
            binding.listStock.layoutManager = layoutManager
            setupRecyclerView()


            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                var index = 0
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (itemstockArray.size == 0)
                        itemstockArray.addAll(db_main_stock.getmainstock(view!!.context))
                    binding.search.clearFocus()
                    var itemstockArray_f :ArrayList<Main_stock_save> = ArrayList()
                    while ( index < itemstockArray.size) {
                        if (query.toString().toLowerCase() in itemstockArray[index].displaySymbol.toLowerCase()  ) {
                            itemstockArray_f.add(itemstockArray[index])
                        }else if (query.toString().toLowerCase() in itemstockArray[index].description.toLowerCase()){
                            itemstockArray_f.add(itemstockArray[index])
                        }
                        index++
                    }
                    if (itemstockArray_f.size == 0) {
                        Toast.makeText(context, "Stock $query no found", Toast.LENGTH_SHORT).show()

                    } else {
                        var item_index: ArrayList<Main_stock_save> = ArrayList()
                        item_index.addAll(itemstockArray_f)
                        adapter = ListStocksAdapter(item_index)
                        binding.listStock.adapter = adapter
                        adapter.notifyDataSetChanged()
                        Toast.makeText(context, "Stock $query found", Toast.LENGTH_SHORT).show()

                    }
                    itemstockArray_f  = ArrayList()
                    index = 0
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText == "" || newText == null) {
                        adapter = ListStocksAdapter(itemstockArray)
                        binding.listStock.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                    return false
                }

            })

        }


    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseJSON()
                            }
                    }
                    Toast.makeText(view!!.context, "The data is uploaded to the phone database from the internet, please wait 1-2 minutes", Toast.LENGTH_LONG).show()

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseJSON()

                        }
                    }
                    Toast.makeText(view!!.context, "The data is uploaded to the phone database from the internet, please wait 1-2 minutes", Toast.LENGTH_LONG).show()
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseJSON()
                            }
                    }
                    Toast.makeText(view!!.context, "The data is uploaded to the phone database from the internet, please wait 1-2 minutes", Toast.LENGTH_LONG).show()

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")

                    return true
                }
            }
        }
        Toast.makeText(view!!.context, "  No internet, please check your internet connection ", Toast.LENGTH_LONG).show()
        return false
    }


        private fun parseJSON() {

            try {

                val clientSetup = OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                        .readTimeout(10, TimeUnit.MINUTES) // read timeout
                        .build()

                val retrofit = Retrofit.Builder()
                        .baseUrl("https://finnhub.io/api/v1/")
                        .client(clientSetup)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()


                // Create Service
                val service = retrofit.create(Reques_main_data_stock::class.java)


                CoroutineScope(Dispatchers.IO).launch {

                    // Do the GET request and get response
                    val response = service.getStocks()

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {

                            // Convert raw JSON to pretty JSON using GSON library

                            val items = response.body()
                            if (items != null) {

                                for (i in 0 until items.count()) {

                                    val currency_1 = items[i].currency ?: "N/A"
                                    Log.d("Currency: ", currency_1)


                                    val description_1 = items[i].description ?: "N/A"
                                    Log.d("Description ", description_1)


                                    val displaySymbol_1 = items[i].displaySymbol ?: "N/A"
                                    Log.d("DisplaySymbol: ", displaySymbol_1)

                                    val model =
                                            Main_stock_save(i, currency_1, description_1, displaySymbol_1, 0)
                                    itemstockArray.add(model)





                                    adapter = ListStocksAdapter(itemstockArray)
                                    adapter.notifyDataSetChanged()

                                }

                            }

                            // Pass the Array with data to RecyclerView Adapter
                            binding.listStock.adapter = adapter
                            Toast.makeText(view!!.context, "DB is download", Toast.LENGTH_SHORT).show()
                            db_main_stock.addmainstock(view!!.context, itemstockArray)


                        } else {

                            Log.e("RETROFIT_ERROR", response.code().toString())

                        }
                    }
                }
            } catch (e: ConnectTimeoutException) {
                Toast.makeText(view!!.context, "No answer from server", Toast.LENGTH_LONG).show()
            }
        }

}

