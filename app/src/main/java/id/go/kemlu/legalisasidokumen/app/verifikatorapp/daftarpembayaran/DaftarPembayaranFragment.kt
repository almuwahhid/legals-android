package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.GmsStatic
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran.adapter.AdapterDaftarPembayaran
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_daftar_pembayaran.*
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran.DetailPembayaranToVerifActivity

class DaftarPembayaranFragment : Fragment(), DaftarPembayaranView.View {

    lateinit var presenter : DaftarPembayaranPresenter
    lateinit var daftarLayananAdapter: AdapterDaftarPembayaran
    var layananModels: MutableList<PembayaranToVerifModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    var isNeedToRefresh = false

    companion object{
        public fun newInstance(): DaftarPembayaranFragment {
            val fragment = DaftarPembayaranFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_daftar_pembayaran, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = DaftarPembayaranPresenter(context!!, this)

        daftarLayananAdapter = AdapterDaftarPembayaran(
            context!!,
            layananModels,
            object :
                AdapterDaftarPembayaran.OnPembayaranClick {
                override fun onClick(model: PembayaranToVerifModel) {
                    isNeedToRefresh = true
                    startActivity(Intent(context, DetailPembayaranToVerifActivity::class.java).putExtra("data", model))
                }
            })

        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = daftarLayananAdapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestDaftarLayanan(false)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestDaftarLayanan(true)
        }

        presenter.requestDaftarLayanan(true)

    }

    override fun onResume() {
        super.onResume()
        if(isNeedToRefresh){
            isNeedToRefresh = false
            presenter.requestDaftarLayanan(true)
        }
    }

    override fun onRequestDaftarLayanan(list: MutableList<PembayaranToVerifModel>, isReload: Boolean) {
        helper_nodata.visibility = View.GONE
        helper_error.visibility = View.GONE
        endlessRecyclerViewScrollListener.resetState()
        if(isReload){
            layananModels.clear()
        }

        layananModels.addAll(list)
        daftarLayananAdapter.notifyDataSetChanged()
    }

    override fun onFailedRequestSomething(isFirst: Boolean, message: String) {
        if(isFirst){
            helper_error.visibility = View.VISIBLE
            tv_error.setText(message)
        } else {
            GmsStatic.ToastShort(context, message)
        }
    }

    override fun onFailedRequestMore(isFirst: Boolean, message : String) {
        if(isFirst){
            layananModels.clear()
            daftarLayananAdapter.notifyDataSetChanged()
            helper_nodata.visibility = View.VISIBLE
            tv_nodata.text = message
        }
    }

    override fun onHideLoading(isFirst: Boolean) {
        helper_nodata.visibility = View.GONE
        helper_error.visibility = View.GONE
        if(isFirst){
            helper_loading_top.hide()
        } else {
            helper_loading_more.hide()
        }
    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onErrorConnection() {

    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
    }
}
