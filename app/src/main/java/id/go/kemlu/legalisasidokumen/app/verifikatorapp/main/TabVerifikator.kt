package id.go.kemlu.legalisasidokumen.app.verifikatorapp.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran.DaftarPembayaranFragment
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarrequest.DaftarRequestActivity

class TabVerifikator (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DaftarRequestActivity.newInstance()
            1 -> fragment = DaftarPembayaranFragment.newInstance()
        }
        return fragment
    }
}