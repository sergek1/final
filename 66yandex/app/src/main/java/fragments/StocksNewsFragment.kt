package fragments

import android.content.Context
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
import com.google.gson.GsonBuilder
import com.sergek.yandex2.classes.DB_classes.DB_News
import com.sergek.yandex2.classes.classes_for_a_saving.Data_news_save
import com.sergek.yandex2.databinding.FragmentNews4Binding
import com.sergek.yandex2.interfases.News_request
import fragments.adapters.NewsAdapter
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class StocksNewsFragment(val symbol_stock: String): Fragment() {

    companion object {
        lateinit var dbNews: DB_News
    }


    var index_on = false
    var itemnewskArray: ArrayList<Data_news_save> = ArrayList()

    private var adapter_news = NewsAdapter(itemnewskArray)

    private lateinit var binding: FragmentNews4Binding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNews4Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun get_news_adapter() {
        if (dbNews.getcountstock() == 0 && index_on == false  ) {

            isOnline(view!!.context)

        } else {
            adapter_news = NewsAdapter(dbNews.getmainstock_news(view!!.context))
            adapter_news.notifyDataSetChanged()
            binding.listNews.adapter = adapter_news
            if (dbNews.getcountstock() == 0){
            binding.textView.text = "No updates"
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        binding.listNews.layoutManager = layoutManager
        binding.listNews.setHasFixedSize(true)
        val dividerItemDecoration =
                DividerItemDecoration(binding.listNews.context, layoutManager.orientation)
        binding.listNews.addItemDecoration(dividerItemDecoration)
        dbNews = DB_News(view.context, null, 1)
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        get_news_adapter()

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
                        withContext(Dispatchers.IO) {
                            parseNews(symbol_stock)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseNews(symbol_stock)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseNews(symbol_stock)
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



    private suspend fun parseNews(symbol_stock: String) {
        try {


            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())
            val millis: Long = Date().time
            val ts = millis - 1209600000
            val currentDate_1 = sdf.format(Date(ts))

            val comp_url = "company-news?symbol=$symbol_stock&from=${currentDate_1}&to=${currentDate.toString()}&token=c1a9orv48v6oifh52gig"

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
            val service = retrofit.create(News_request::class.java)
            CoroutineScope(Dispatchers.IO).launch {

                // Do the GET request and get response
                val response = service.getnews(comp_url)

                withContext(Dispatchers.Main) {

                    itemnewskArray = ArrayList()
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(response.body())


                        val items = response.body()
                        if (items != null) {
                            for (i in 0 until items.count()) {

                                val category_1 = items[i].category ?: "No information"
                                Log.d("category: ", category_1)


                                val datetime_1 = items[i].datetime ?: 5
                                Log.d("datetime ", datetime_1.toString())


                                val headline_1 = items[i].headline ?: "No information"
                                Log.d("headline: ", headline_1)

                                val id_1 = items[i].summary ?: "No information"
                                Log.d("id: ", id_1)

                                val image_1 = items[i]?.image ?: "No information"
                                Log.d("image: ", image_1)

                                val related_1 = items[i].related ?: "No information"
                                Log.d("related: ", related_1)

                                val source_1 = items[i].source ?: "No information"
                                Log.d("Type: ", source_1)


                                val model_1 = Data_news_save(category_1, datetime_1, headline_1, id_1, image_1, related_1, source_1)
                                itemnewskArray.add(model_1)



                                adapter_news = NewsAdapter(itemnewskArray)
                                adapter_news.notifyDataSetChanged()

                            }
                            dbNews = DB_News(view!!.context, null, 1)

                            dbNews.addmainstock_news(view!!.context, itemnewskArray)


                        }
                        if (items?.count() == 0) {
                            binding.textView.text = "No updates"
                        }

                        // Pass the Array with data to RecyclerView Adapter
                        binding.listNews.adapter = adapter_news

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        } catch (e: SocketTimeoutException) {
            Toast.makeText(view!!.context, "No answer from server", Toast.LENGTH_LONG).show()
        }
        index_on = true
    }
}
