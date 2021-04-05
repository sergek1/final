package fragments.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.sergek.yandex2.R
import com.sergek.yandex2.classes.classes_for_a_saving.Data_news_save
import com.sergek.yandex2.classes.classes_for_a_saving.Recomm_save
import com.sergek.yandex2.databinding.ElementOfNewsBinding
import com.sergek.yandex2.databinding.ElementOfTrends2Binding
import com.squareup.picasso.Picasso
import okio.blackholeSink
import java.time.Instant
import java.time.ZoneId


class Adapter_trends(private val trendssave: ArrayList<Recomm_save>): RecyclerView.Adapter<CustomViewHolder_trends>() {

    private lateinit var binding: ElementOfTrends2Binding

    override fun getItemCount(): Int {
        return trendssave.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder_trends {
    //создаем эелемент ресайклера
    binding = ElementOfTrends2Binding.inflate(LayoutInflater.from(parent.context))
    return CustomViewHolder_trends(binding.root)

}
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CustomViewHolder_trends, position: Int) {
        val barentries = ArrayList<Entry>()
        barentries.add(Entry(trendssave[position].buy.toFloat(), 0))
        barentries.add(Entry(trendssave[position].hold.toFloat(), 1))
        barentries.add(Entry(trendssave[position].sell.toFloat(), 2))
        barentries.add(Entry(trendssave[position].strongBuy.toFloat(), 3))
        barentries.add(Entry(trendssave[position].strongSell.toFloat(), 4))


        val xvalues = ArrayList<String>()
        xvalues.add("Buy ")
        xvalues.add("Hold")
        xvalues.add("Sell ")
        xvalues.add("Strong Buy")
        xvalues.add("Strong Sell")

        val bardataset = PieDataSet(barentries,"")
        bardataset.sliceSpace = 2f

        val colors = ArrayList<Int>()
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.LTGRAY)
        colors.add(Color.YELLOW)
         bardataset.colors = colors



        //make a bar data
        val data = PieData(xvalues, bardataset)
        binding.barChartTrends.data = data
        binding.periodName.text = "Period ${trendssave[position].period}"
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT)
        binding.barChartTrends.setCenterTextSize(20f)
        binding.barChartTrends.animateXY(3000,3000)

        val legend : Legend = binding.barChartTrends.legend
        legend.textSize = 15f
        legend.position = Legend.LegendPosition.LEFT_OF_CHART


    }

}

class CustomViewHolder_trends (val view: View): RecyclerView.ViewHolder(view) {

}