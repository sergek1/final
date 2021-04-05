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
import com.google.gson.GsonBuilder
import com.sergek.yandex2.classes.DB_classes.DB_Company_profile
import com.sergek.yandex2.classes.classes_for_a_saving.Name_save
import com.sergek.yandex2.classes.classes_for_a_saving.Price_save
import com.sergek.yandex2.databinding.CompanyFragment1Binding
import com.sergek.yandex2.interfases.Company_request
import com.sergek.yandex2.interfases.Price_request
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class Stocks_Company_Fragment(val symbol_stock: String,val name: String, val currency_c :  String ): Fragment() {

    companion object{
        lateinit var db_comp : DB_Company_profile
    }
    private  var item_comp_1 : ArrayList<Name_save> = ArrayList()
    private  var item_comp_2 : ArrayList<Price_save> = ArrayList()

    private lateinit var binding: CompanyFragment1Binding




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CompanyFragment1Binding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db_comp = DB_Company_profile(view!!.context,null,1)

        view_comp_prof()

    }


    fun view_comp_prof() {
        if (db_comp.getcountstock() == 0) {

            isOnline(view!!.context)

        } else {


            db_comp.getcomp_1(view!!.context)
            db_comp.getcomp_2(view!!.context)
            item_comp_1 = ArrayList()
            item_comp_2 = ArrayList()
            if (view!!.context != null) {

                item_comp_1.addAll(db_comp.getcomp_1(view!!.context))
                item_comp_2.addAll(db_comp.getcomp_2(view!!.context))
                if (item_comp_1.size > 0 && item_comp_2.size > 0) {
                    if (item_comp_1[0].country != "") {
//                        Toast.makeText(view!!.context, "  ${item_comp_1[0].name} in DB", Toast.LENGTH_LONG).show()
                        if (item_comp_1[0].logo != "") {
                            Picasso.get().load(item_comp_1[0].logo).into(binding.logoCompany)
                        }
                            binding.companyText.text = item_comp_1[0].name
                            binding.countryText.text = item_comp_1[0].country
                            binding.currencyText.text = item_comp_1[0].currency
                            binding.ipoText.text = item_comp_1[0].ipo
                            binding.phone.text = item_comp_1[0].phone
                            binding.weburlText.text = item_comp_1[0].weburl
                            binding.current1PriceText.text = " $ ${item_comp_2[1].c.toString()}"
                            binding.highPriceText.text = " $ ${item_comp_2[1].h.toString()}"
                            binding.lowPriceText.text = " $ ${item_comp_2[1].l.toString()}"
                            binding.openPriceText.text = " $ ${item_comp_2[1].o.toString()}"

                        } else {
                            if (item_comp_1[1].logo != "") {
                                Picasso.get().load(item_comp_1[1].logo).into(binding.logoCompany)
                            }
                                binding.companyText.text = item_comp_1[1].name
                                binding.countryText.text = item_comp_1[1].country
                                binding.currencyText.text = item_comp_1[1].currency
                                binding.ipoText.text = item_comp_1[1].ipo
                                binding.phone.text = item_comp_1[1].phone
                                binding.weburlText.text = item_comp_1[1].weburl
                                binding.current1PriceText.text = " $ ${item_comp_2[0].c.toString()}"
                                binding.highPriceText.text = " $ ${item_comp_2[0].c.toString()}"
                                binding.lowPriceText.text = " $ ${item_comp_2[0].c.toString()}"
                                binding.openPriceText.text =" $ ${item_comp_2[0].c.toString()}"

                        }
                    }
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
                        withContext(Dispatchers.IO) {
                            parseCompany(symbol_stock,name, currency_c)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseCompany(symbol_stock,name, currency_c)
                        }
                    }
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            parseCompany(symbol_stock,name, currency_c)
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



     private suspend fun parseCompany(symbol_stock: String, name: String, currency_c : String)  {

         val name_3 = name
        try{

    val conn_comp = "stock/profile2?symbol=$symbol_stock&token=c1a9orv48v6oifh52gig"


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
        val service = retrofit.create(Company_request::class.java)

            val job = CoroutineScope( Dispatchers.IO)
                job.launch {

                        // Do the GET request and get response
                        val response = service.getcompany(conn_comp)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {

                                // Convert raw JSON to pretty JSON using GSON library

                                val gson = GsonBuilder().setPrettyPrinting().create()


                                val country_1 = response.body()?.country ?: "No information"
                                Log.d("country_1: ", country_1)
                                binding.countryText.text = country_1

                                val currency_1 = response.body()?.currency ?: currency_c
                                Log.d("currency_1 : ", currency_1)
                                binding.currencyText.text = currency_1

                                val ipo_1 = response.body()?.ipo ?: "No information"
                                Log.d("ipo_1: ", ipo_1)
                                binding.ipoText.text = ipo_1

                                val name_1 = response.body()?.name ?: name_3
                                Log.d("name_1: ", name_1)
                                binding.companyText.text = name_1

                                val phone_1 = response.body()?.phone ?: "No information"
                                Log.d("phone_1: ", phone_1)
                                binding.phone.text = phone_1

                                val tiker_1 = response.body()?.ticker ?: "No information"
                                Log.d("tiker_1: ", tiker_1)

                                val weburl_1 = response.body()?.weburl ?: "No information"
                                Log.d("weburl_1: ", weburl_1)
                                binding.weburlText.text = weburl_1

                                val logo_1 = response.body()?.logo ?: "No information"
                                if (logo_1 != "") {
                                    Picasso.get().load(logo_1).into(binding.logoCompany)
                                }

                                val item_comp_11 = Name_save(country_1, currency_1, ipo_1, logo_1, name_1, phone_1, tiker_1, weburl_1)
                                item_comp_1.add(item_comp_11)
                                db_comp.addcopm_1(view!!.context, item_comp_1)

                            } else {

                                Log.e("RETROFIT_ERROR", response.code().toString())

                            }
                        }

                        delay(1L)
                        job.cancel()
        }
        }catch (e: java.lang.NullPointerException){
        Toast.makeText(view!!.context,"No answer from server", Toast.LENGTH_LONG).show()
    }
                 parsePrice(symbol_stock)



    }
private suspend fun parsePrice(symbol_stock: String) {
    try{

    var conn_comp_price = "quote?symbol=$symbol_stock&token=c1a9orv48v6oifh52gig"

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
    val service = retrofit.create(Price_request::class.java)

            val job = CoroutineScope(Dispatchers.IO)
            job.launch {

                // Do the GET request and get response
                val response = service.getPrice(conn_comp_price)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library

                        val gson = GsonBuilder().setPrettyPrinting().create()


                        val c_1 = response.body()?.c ?: -1.0
                        binding.current1PriceText.text = "$ $c_1"

                        val h_1 = response.body()?.h ?: -1.0
                        binding.highPriceText.text = "$ $h_1"

                        val l_1 = response.body()?.l ?: -1.0
                        binding.lowPriceText.text = "$ $l_1"

                        val o_1 = response.body()?.o ?: -1.0
                        binding.openPriceText.text = "$ $o_1"


                        val item_comp_22 = Price_save(c_1, h_1, l_1, o_1, 0.0)
                        try {
                            item_comp_2.add(item_comp_22)
                            db_comp.addcopm_2(view!!.context, item_comp_2)
                        } catch (r: java.lang.NullPointerException) {
                        }
                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
                delay(1L)
                job.cancel()
            }
    }catch (e: java.lang.NullPointerException){
        Toast.makeText(view!!.context,"No answer from server", Toast.LENGTH_LONG).show()
    }
    }

}


