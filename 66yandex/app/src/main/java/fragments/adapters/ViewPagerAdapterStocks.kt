package fragments.adapters

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapterStocks(supportFragmentManager: FragmentManager): FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragmentList1 = ArrayList<Fragment>()
    private val mFragmentTitleList1 = ArrayList<String>()

    override fun getCount(): Int {
        return mFragmentList1.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList1[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList1[position]
    }
    fun addFragment(fragment: Fragment, title: String){
        mFragmentList1.add(fragment)
        mFragmentTitleList1.add(title)
    }

    override fun saveState(): Parcelable? {
        return super.saveState()
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        super.restoreState(state, loader)
    }

}