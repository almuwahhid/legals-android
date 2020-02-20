package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpejabat

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah.DaftarPengesahPresenter
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah.DaftarPengesahView
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah.adapter.DaftarPengesahAdapter
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpengesah.DetailPengesahDialog
import id.go.kemlu.legalisasidokumen.data.models.PejabatModel
import id.go.kemlu.legalisasidokumen.data.models.PengesahModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat.DialogAddPejabat
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_daftar_pejabat.*
import kotlinx.android.synthetic.main.layout_helper.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class DaftarPejabatActivity : LegalisasiActivity(), DaftarPengesahView.View {

    lateinit var adapter: DaftarPengesahAdapter
    var layananModels: MutableList<PengesahModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    internal lateinit var presenter:DaftarPengesahPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_pejabat)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("Daftar Pejabat")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Daftar Pejabat")

        presenter = DaftarPengesahPresenter(context, this)
        adapter = DaftarPengesahAdapter(context!!, layananModels, object : DaftarPengesahAdapter.OnClick{
            override fun onClick(model: PengesahModel) {
                DetailPengesahDialog(context, model).initPejabat()
            }
        })

        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestPejabat(false)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestPejabat(true)
        }

        fab_add.setOnClickListener({
            DialogAddPejabat(context, "Pejabat", object: DialogAddPejabat.OnDialogAddPejabat{
                override fun onSuccessAddPejabat() {
                    presenter.requestPejabat(true)
                }
            })
        })

        presenter.requestPejabat(true)
    }

    override fun onRequestPengesah(list: MutableList<PengesahModel>, isReload: Boolean) {
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

    override fun noInternetConnection(isFirst: Boolean) {
        if(isFirst && layananModels.size == 0){
            helper_noconnection.visibility = View.VISIBLE
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
        helper_error.visibility = View.GONE
        helper_noconnection.visibility = View.GONE
        if(isFirst){
            helper_loading_top.hide()
        } else {
            helper_loading_more.hide()
        }
    }

    override fun onHideLoading() {
        helper_noconnection.visibility = View.GONE
    }

    override fun onErrorConnection() {

    }

    override fun onLoading() {
        helper_loading_top.show()
    }
}
