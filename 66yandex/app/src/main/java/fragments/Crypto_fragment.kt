package fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.sergek.yandex2.classes.classes_for_a_saving.crypto_save
import com.sergek.yandex2.databinding.CryptoListBinding
import com.sergek.yandex2.interfases.Request_crypto
import fragments.adapters.Adapter_crypto
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Crypto_fragment(): Fragment() {

    var item_crypto: ArrayList<crypto_save> = ArrayList()


    private var adapter_crypto = Adapter_crypto(item_crypto)

    private lateinit var binding: CryptoListBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        binding = CryptoListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        binding.listCtypto.layoutManager = layoutManager
        binding.listCtypto.setHasFixedSize(true)
        val dividerItemDecoration =
                DividerItemDecoration(binding.listCtypto.context, layoutManager.orientation)
        binding.listCtypto.addItemDecoration(dividerItemDecoration)

        isOnline(view!!.context)

        binding.searchCrypto.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var index = 0
            //                var find_bool = false
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (item_crypto.size == 0)
                binding.searchCrypto.clearFocus()
                var item_crypto_f :ArrayList<crypto_save> = ArrayList()
                while ( index < item_crypto.size) {
                    if (query.toString().toLowerCase() in item_crypto[index].displaySymbol_c.toLowerCase()  ) {
//                            find_bool = true
                        item_crypto_f.add(item_crypto[index])
                    }else if (query.toString().toLowerCase() in item_crypto[index].symbol_c.toLowerCase()){
                        item_crypto_f.add(item_crypto[index])
                    }
                    index++
                }
                if (item_crypto_f.size == 0) {
                    Toast.makeText(context, "Crypto $query no found", Toast.LENGTH_SHORT).show()

                } else {
                    var item_index: ArrayList<crypto_save> = ArrayList()
                    item_index.addAll(item_crypto_f)
                    adapter_crypto = Adapter_crypto(item_index)
                    binding.listCtypto.adapter = adapter_crypto
                    adapter_crypto.notifyDataSetChanged()
                    Toast.makeText(context, "Crypto $query found", Toast.LENGTH_SHORT).show()

                }
                item_crypto_f  = ArrayList()
                index = 0
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "" || newText == null) {
                    adapter_crypto = Adapter_crypto(item_crypto)
                    binding.listCtypto.adapter = adapter_crypto
                    adapter_crypto.notifyDataSetChanged()
//                    Toast.makeText(context, "Full list stocks", Toast.LENGTH_SHORT).show()
                }

                return false
            }

        })

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
                            parsecrypto()
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parsecrypto()
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parsecrypto()
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


    private suspend fun parsecrypto() {
        try {
            val comp_url = "crypto/symbol?exchange=binance&token=c1a9orv48v6oifh52gig"

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
            val service = retrofit.create(Request_crypto::class.java)
            CoroutineScope(Dispatchers.IO).launch {

                // Do the GET request and get response
                val response = service.getcrypto(comp_url)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library


                        val items = response.body()
                        if (items != null) {
                            for (i in 0 until items.count()) {

                                val description_1 = items[i].description ?: "No information"
                                Log.d(" description: ",description_1 )

                                val displaySymbol_1 = items[i].displaySymbol ?: "No information"
                                Log.d("displaySymbol: ",displaySymbol_1 )

                                val symbol_1 = items[i].symbol ?: "No information"
                                Log.d("symbol: ",symbol_1 )

                                val model_1 = crypto_save(description_1,displaySymbol_1,symbol_1)
                                item_crypto.add(model_1)

                            }
                            adapter_crypto = Adapter_crypto(item_crypto)
                            adapter_crypto.notifyDataSetChanged()
                            binding.listCtypto.adapter = adapter_crypto
                        }

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        } catch (e: java.net.ConnectException) {
            Toast.makeText(view!!.context, "No answer from server", Toast.LENGTH_LONG).show()
        }
    }
}