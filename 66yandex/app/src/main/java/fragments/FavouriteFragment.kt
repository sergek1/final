package fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergek.yandex2.R
import com.sergek.yandex2.classes.DB_classes.DB_main_stock
import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import com.sergek.yandex2.databinding.FavouriteFragmentBinding
import fragments.adapters.List_favourite_adapter


class FavouriteFragment : Fragment() {

    companion object{
        lateinit var db_stock_f: DB_main_stock
    }

    private lateinit var binding :FavouriteFragmentBinding
    private  var item_f_array : ArrayList<Main_stock_save> = ArrayList()
    lateinit var  adapter_f: List_favourite_adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouriteFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(view?.context)

        binding.listFavourite.layoutManager = layoutManager
        binding.listFavourite.setHasFixedSize(true)
        val dividerItemDecoration =
                DividerItemDecoration(binding.listFavourite.context, layoutManager.orientation)
        view?.context?.let {
            ContextCompat.getDrawable(it, R.drawable.line_divider)
                    ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
        }
        binding.listFavourite.addItemDecoration(dividerItemDecoration)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        db_stock_f= DB_main_stock(view.context, null, 1)
         val arr = db_stock_f.getmainstock(view.context)
        item_f_array = ArrayList()
        for (k in 0..(arr.size - 1)){

            if (arr[k].favourite == 1){
                item_f_array.add(arr[k])

            }
        }
        adapter_f = List_favourite_adapter(item_f_array)
        adapter_f.notifyDataSetChanged()
        binding.listFavourite.adapter = adapter_f

    }


}

