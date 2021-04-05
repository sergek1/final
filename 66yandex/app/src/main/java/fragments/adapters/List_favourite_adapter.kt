package fragments.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sergek.yandex2.R
import com.sergek.yandex2.StocksActivity
import com.sergek.yandex2.classes.DB_classes.DB_main_stock
import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import com.sergek.yandex2.databinding.ElementOfLitsFavouriteBinding
import com.sergek.yandex2.databinding.FavouriteFragmentBinding

class List_favourite_adapter(private val stock_f: ArrayList<Main_stock_save>): RecyclerView.Adapter<CustomViewHolder_fav>() {
    companion object {
        lateinit var db_stock_f: DB_main_stock
        private lateinit var itemstock_fv: ArrayList<Main_stock_save>
        private lateinit var adapter_f: List_favourite_adapter
    }
    private lateinit var binding: ElementOfLitsFavouriteBinding

    private lateinit var binding_2: FavouriteFragmentBinding



    override fun getItemCount(): Int {
        return stock_f.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder_fav {
        //создаем эелемент ресайклера
        binding = ElementOfLitsFavouriteBinding.inflate(LayoutInflater.from(parent.context))
        binding_2 = FavouriteFragmentBinding.inflate(LayoutInflater.from(parent.context))
        return CustomViewHolder_fav(binding.root, binding_2.root)

    }

    override fun onBindViewHolder(holder: CustomViewHolder_fav, position: Int) {
        binding.currencyText.text = stock_f[position].currency
        binding.descriptionText.text = stock_f[position].description
        binding.displaySymbolText.text = stock_f[position].displaySymbol

        binding.addFav.setOnClickListener {

            itemstock_fv = ArrayList()

            val symbol_cliked = holder.view.findViewById<TextView>(R.id.displaySymbol_text)
            db_stock_f = DB_main_stock(holder.view.context, null, 1)

            var arr = db_stock_f.getmainstock(holder.view.context)
            var index = 0
            var find = false
            while (index < arr.size && find == false) {
                if (arr[index].displaySymbol == symbol_cliked.text.toString()) {
                    find = true
                }
                index++
            }
            db_stock_f.dellfav_2(db_stock_f.getmainstock(holder.view.context)[index - 1])
            Toast.makeText(holder.view.context, "Stock ${symbol_cliked.text} del from favourite", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

class CustomViewHolder_fav(val view : View, val view_2: View): RecyclerView.ViewHolder(view) {


    init {
        view.setOnClickListener {
            val intent = Intent(view.context, StocksActivity::class.java)
            val symbol_cliked = view.findViewById<TextView>(R.id.displaySymbol_text)
            val execute_symbol = Bundle()
            execute_symbol.putString("symbol",symbol_cliked.text.toString())
            intent.putExtras(execute_symbol)
            view.context.startActivity(intent)
            Toast.makeText(view.context, "Stock ${symbol_cliked.text} is  cliked", Toast.LENGTH_LONG).show()

        }
    }



}