package id.go.kemlu.legalisasidokumen.app.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.go.kemlu.legalisasidokumen.app.daftarlayanan.DaftarLayananFragment

class TabMain(fm: FragmentManager, internal var viewModel: HomeViewModel) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DaftarLayananFragment.newInstance(viewModel, 0)
            1 -> fragment = DaftarLayananFragment.newInstance(viewModel, 110)
            2 -> fragment = DaftarLayananFragment.newInstance(viewModel, 120)
            3 -> fragment = DaftarLayananFragment.newInstance(viewModel, 160)
            4 -> fragment = DaftarLayananFragment.newInstance(viewModel, 190)
        }
        return fragment
    }
}