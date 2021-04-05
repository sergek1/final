package fragments.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sergek.yandex2.Crypto_activity
import com.sergek.yandex2.R
import com.sergek.yandex2.StocksActivity
import com.sergek.yandex2.classes.classes_for_a_saving.Data_news_save
import com.sergek.yandex2.classes.classes_for_a_saving.crypto_save
import com.sergek.yandex2.databinding.ElementOfCryptoBinding
import com.sergek.yandex2.databinding.ElementOfNewsBinding
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.ZoneId

class Adapter_crypto(private val cryptosave: ArrayList<crypto_save>): RecyclerView.Adapter<CustomViewHolder_crypto>(){

    private lateinit var binding: ElementOfCryptoBinding

    override fun getItemCount(): Int {
        return  cryptosave.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder_crypto {
        //создаем эелемент ресайклера
        binding = ElementOfCryptoBinding.inflate(LayoutInflater.from(parent.context))
        return CustomViewHolder_crypto(binding.root)

    }


    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun onBindViewHolder(holder: CustomViewHolder_crypto, position: Int) {

        binding.cryptoName.text = cryptosave[position].description_c
        binding.dispTic.text = cryptosave[position].displaySymbol_c
        binding.ticSym.text = cryptosave[position].symbol_c

    }


}

class CustomViewHolder_crypto(val view : View): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            val intent = Intent(view.context,Crypto_activity::class.java)
            val symbol_crypto= view.findViewById<TextView>(R.id.tic_sym)
            val execute_symbol = Bundle()
            execute_symbol.putString("symbol_crypto", symbol_crypto.text.toString())
            intent.putExtras(execute_symbol)
            view.context.startActivity(intent)
            Toast.makeText(view.context, "Crypto ${symbol_crypto.text} is  cliked", Toast.LENGTH_SHORT).show()

        }
    }

}