package id.go.kemlu.legalisasidokumen.app.notifikasi

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.detaillayanan.DetailLayananActivity
import id.go.kemlu.legalisasidokumen.app.notifikasi.adapter.NotifikasiAdapter
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifikasi.*
import kotlinx.android.synthetic.main.layout_helper.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class NotifikasiActivity : LegalisasiActivity(), NotifikasiView.View {


    lateinit var adapter: NotifikasiAdapter
    var layananModels: MutableList<NotifikasiModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    internal lateinit var presenter:NotifikasiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("Notifikasi")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Notifikasi")

        presenter = NotifikasiPresenter(context, this)
        adapter = NotifikasiAdapter(context!!, layananModels, object : NotifikasiAdapter.OnNotifikasiClick{
            override fun onClick(model: NotifikasiModel) {
                presenter.requestDetailNotifikasi(""+model.strNotifGroupNo)
            }
        })
        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestNotifikasi(false)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestNotifikasi(true)
        }

        presenter.requestNotifikasi(true)
    }

    override fun onRequestNotifikasi(list: MutableList<NotifikasiModel>, isReload: Boolean) {
        endlessRecyclerViewScrollListener.resetState()
        if(isReload){
            layananModels.clear()
        }

        layananModels.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun onFailedRequestSomething(isFirst: Boolean, message: String) {
        if(isFirst){
            helper_error.visibility = View.VISIBLE
            tv_error.setText(message)
        } else {
            GmsStatic.ToastShort(context, message)
        }
    }

    override fun onFailedRequestMore(isFirst: Boolean, message: String) {
        if(isFirst){
            helper_nodata.visibility = View.VISIBLE
            tv_nodata.text = message
        }
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
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
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoadingDetail() {
        GmsStatic.showLoadingDialog(this, R.drawable.ic_logo)
    }

    override fun onErrorConnection() {

    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onRequestDetailNotifikasi(model: RequestModel) {
        startActivity(Intent(context, DetailLayananActivity::class.java).putExtra("data", model))
    }
}
