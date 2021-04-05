package com.sergek.yandex2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sergek.yandex2.classes.DB_classes.DB_Company_profile
import com.sergek.yandex2.classes.DB_classes.DB_News
import com.sergek.yandex2.classes.DB_classes.DB_charts
import com.sergek.yandex2.databinding.ActivityStocksProfileBinding
import fragments.*
import fragments.adapters.ViewPagerAdapterStocks
import java.lang.Exception

class StocksActivity: AppCompatActivity() {

    var symbol: String = ""
    var name_c: String = ""
    var  currency_c : String = ""
    lateinit var db_comp :DB_Company_profile
    lateinit var dbNews: DB_News
    lateinit var dbCharts: DB_charts

    private  lateinit var binding: ActivityStocksProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        symbol = intent.extras?.getString("symbol", "no found symbol") ?: "NULL"
        name_c = intent.extras?.getString("name", "no found name") ?: "NULL"
        currency_c = intent.extras?.getString("currency", "no found currency") ?: "NULL"
        binding = ActivityStocksProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerStocks = findViewById(R.id.viewPagerStocks)
        tabsStocks = findViewById(R.id.tabsStocks)

        db_comp = DB_Company_profile(this,null,1)
        dbNews = DB_News(this,null ,1)
        dbCharts = DB_charts(this,null,1 )
        isOnline(baseContext)

}

    private lateinit var viewPagerStocks: ViewPager
    private lateinit var tabsStocks: TabLayout

    private  fun setUpTabs1(){
        val adapter1 = ViewPagerAdapterStocks(supportFragmentManager)
        adapter1.addFragment(Stocks_Company_Fragment(symbol,name_c, currency_c), "Profile of Company")
        adapter1.addFragment(Stocks_Trends_Fragment(symbol), "Recommendations")
        adapter1.addFragment(StocksNewsFragment(symbol), "Tidings")
        viewPagerStocks.adapter = adapter1
        tabsStocks.setupWithViewPager(viewPagerStocks)

    }

    override fun onDestroy() {
        super.onDestroy()
        db_comp.cleardbstock()
        dbNews.cleardbstock()
        dbCharts.cleardbcharts()

    }

    override fun onStart() {
        super.onStart()
        db_comp.cleardbstock()
        dbNews.cleardbstock()
        dbCharts.cleardbcharts()
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    setUpTabs1()
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    setUpTabs1()
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    setUpTabs1()
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")

                    return true
                }
            }
        }
        Toast.makeText(baseContext, "  No internet, please check your internet connection ", Toast.LENGTH_LONG).show()
        return false
    }

}