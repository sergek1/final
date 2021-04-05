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
import com.sergek.yandex2.databinding.ElementOfListStockBinding
import com.sergek.yandex2.databinding.FavouriteFragmentBinding


class ListStocksAdapter(private val mainstocksave: ArrayList<Main_stock_save>): RecyclerView.Adapter<CustomViewHolder>(){
    companion object {
        lateinit var db_main_stock: DB_main_stock
    }

    private lateinit var adapter_f : List_favourite_adapter

    private lateinit var itemstock_fv : ArrayList<Main_stock_save>

    private lateinit var binding: ElementOfListStockBinding

    private lateinit var binding_1: FavouriteFragmentBinding

    override fun getItemCount(): Int {
       return  mainstocksave.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //создаем эелемент ресайклера
        binding = ElementOfListStockBinding.inflate(LayoutInflater.from(parent.context))
        binding_1 = FavouriteFragmentBinding.inflate(LayoutInflater.from(parent.context))
        return CustomViewHolder(binding.root, binding_1.root)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val stock = mainstocksave.get(position)
        binding.currencyText.text = mainstocksave[position].currency
        binding.descriptionText.text = mainstocksave[position].description
        binding.displaySymbolText.text = mainstocksave[position].displaySymbol

        binding.addFav.setOnClickListener{

            itemstock_fv = ArrayList()

            val symbol_cliked = holder.view.findViewById<TextView>(R.id.displaySymbol_text)
            db_main_stock = DB_main_stock(holder.view.context, null, 1)

            var arr = db_main_stock.getmainstock(holder.view.context)
            var index  = 0
            var find = false
            while (index < arr.size && find == false){
                if (arr[index].displaySymbol == symbol_cliked.text.toString() ){
                    find = true
                }
                index ++
            }
            if  (arr[index - 1].favourite == 0){
                db_main_stock.addfav_2(db_main_stock.getmainstock(holder.view.context)[index - 1])

                Toast.makeText(holder.view.context, "Stock ${db_main_stock.getmainstock(holder.view.context)[index - 1].favourite}, ${db_main_stock.getmainstock(holder.view.context)[index - 1].id} , $find , ${db_main_stock.getmainstock(holder.view.context)[index - 1].displaySymbol} add to favourite", Toast.LENGTH_SHORT).show()
                arr = db_main_stock.getmainstock(holder.view.context)

            }else{
                db_main_stock.dellfav_2(db_main_stock.getmainstock(holder.view.context)[index - 1])

                Toast.makeText(holder.view.context, "Stock ${db_main_stock.getmainstock(holder.view.context)[index - 1].favourite}, ${db_main_stock.getmainstock(holder.view.context)[index - 1].id} , $find ,${db_main_stock.getmainstock(holder.view.context)[index - 1].displaySymbol} del from favourite", Toast.LENGTH_SHORT).show()

                 arr = db_main_stock.getmainstock(holder.view.context)

            }
        }

    }
}

class CustomViewHolder(val view: View, val view_1: View): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, StocksActivity::class.java)
            val symbol_cliked = view.findViewById<TextView>(R.id.displaySymbol_text)
            val name_cliked = view.findViewById<TextView>(R.id.description_text)
            val currency_cliked = view.findViewById<TextView>(R.id.currency_text)
            val execute_symbol = Bundle()
            execute_symbol.putString("symbol", symbol_cliked.text.toString())
            execute_symbol.putString("name", name_cliked.text.toString())
            execute_symbol.putString("currency", currency_cliked.text.toString())
            intent.putExtras(execute_symbol)
            view.context.startActivity(intent)

        }
    }
}




