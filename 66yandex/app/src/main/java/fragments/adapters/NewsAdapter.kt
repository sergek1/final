package fragments.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.sergek.yandex2.classes.classes_for_a_saving.Data_news_save

import com.sergek.yandex2.databinding.ElementOfNewsBinding
import com.squareup.picasso.Picasso
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val newssave: ArrayList<Data_news_save>): RecyclerView.Adapter<CustomViewHolder_news>(){

    private lateinit var binding: ElementOfNewsBinding

    override fun getItemCount(): Int {
        return  newssave.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder_news {
        //создаем эелемент ресайклера
        binding = ElementOfNewsBinding.inflate(LayoutInflater.from(parent.context))
        return CustomViewHolder_news(binding.root)

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CustomViewHolder_news, position: Int) {
        val dt = Instant.ofEpochSecond(newssave[position].datetime.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()


        binding.categotyText.text = newssave[position].category
        binding.datatimeText.text =dt.toString()
        binding.headlineText.text = newssave[position].headline
        binding.idText.text = newssave[position].summary
        if (newssave[position].image != ""){
            Picasso.get().load(newssave[position].image).into(binding.imganeNews)
        }
        binding.imageText.text = newssave[position].image
        binding.relaitedText.text = newssave[position].related
        binding.surceText.text = newssave[position].source
    }


}

class CustomViewHolder_news(val view : View): RecyclerView.ViewHolder(view) {


}