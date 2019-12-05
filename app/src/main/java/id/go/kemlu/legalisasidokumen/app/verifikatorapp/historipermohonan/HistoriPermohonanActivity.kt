package id.go.kemlu.legalisasidokumen.app.verifikatorapp.historipermohonan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.VerifikatorPresenter
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.VerifikatorView
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.adapter.VerifikasiAdapter
import id.go.kemlu.legalisasidokumen.data.models.RequestToVerifModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_histori_permohonan.*
import kotlinx.android.synthetic.main.layout_helper.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class HistoriPermohonanActivity : LegalisasiActivity(), VerifikatorView.View {

    lateinit var presenter : VerifikatorPresenter

    lateinit var daftarLayananAdapter: VerifikasiAdapter
    var layananModels: MutableList<RequestToVerifModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histori_permohonan)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("Riwayat Permohonan")
        }

        presenter = VerifikatorPresenter(context, this)

        daftarLayananAdapter = VerifikasiAdapter(context!!, layananModels, object : VerifikasiAdapter.OnVerifikasiAdapter{
            override fun onClick(model: RequestToVerifModel) {
//                startActivity(Intent(context, DetailLayananActivity::class.java).putExtra("data", model))
            }
        })
        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = daftarLayananAdapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestDataVerifikasi(false)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestDataVerifikasi(true)
        }

        presenter.requestDataVerifikasi(true)
    }

    override fun onRequestDataVerifikasi(list: MutableList<RequestToVerifModel>, isReload: Boolean) {
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
            helper_nodata.visibility = View.VISIBLE
            tv_nodata.text = message
        }
    }

    override fun onHideLoading(isFirst: Boolean) {
        helper_nodata.visibility = View.GONE
        if(isFirst){
            helper_loading_top.hide()
        } else {
            helper_loading_more.hide()
        }
    }

    override fun onHideLoading() {

    }

    override fun onErrorConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
    }
}
