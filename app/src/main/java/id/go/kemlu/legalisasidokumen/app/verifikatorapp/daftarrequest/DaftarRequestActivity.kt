package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarrequest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.home.HomeViewModel
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarrequest.adapter.VerifikasiAdapter
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.data.models.RequestToVerifModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogRequestIkm.DialogRequestIkm
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.GmsStatic
import id.go.kemlu.legalisasidokumen.data.StaticData.STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran.*
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpermohonan.DetailPermohonanActivity
import kotlinx.android.synthetic.main.activity_daftar_request.*
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import id.go.kemlu.legalisasidokumen.data.Preferences

class DaftarRequestActivity : Fragment(), VerifikatorView.View{

    lateinit var presenter : VerifikatorPresenter

    lateinit var daftarLayananAdapter: VerifikasiAdapter
    var layananModels: MutableList<RequestToVerifModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    companion object{
        public fun newInstance(): DaftarRequestActivity {
            val fragment = DaftarRequestActivity()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_daftar_request, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = VerifikatorPresenter(context!!, this)

        daftarLayananAdapter = VerifikasiAdapter(
            context!!,
            layananModels,
            object :
                VerifikasiAdapter.OnVerifikasiAdapter {
                override fun onClick(model: RequestToVerifModel) {
                    presenter.requestDetail(model)
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

    override fun noInternetConnection(isFirst: Boolean) {
        if(isFirst && layananModels.size == 0){
            helper_noconnection.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if(GmsStatic.getSPBoolean(context, Preferences.LAYANAN_TOVERIF_ON_REFRESH)){
            GmsStatic.setSPBoolean(context, Preferences.LAYANAN_TOVERIF_ON_REFRESH, false)
            presenter.requestDataVerifikasi(true)
        }
    }

    override fun onRequestDataVerifikasi(list: MutableList<RequestToVerifModel>, isReload: Boolean) {
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
        helper_noconnection.visibility = View.GONE
        helper_error.visibility = View.GONE
        if(isFirst){
            helper_loading_top.hide()
        } else {
            helper_loading_more.hide()
        }
    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
        helper_noconnection.visibility = View.GONE
    }

    override fun onLoadingDetail() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    override fun onErrorConnection() {

    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
    }

    override fun onRequestDetail(model: RequestModel) {
        /*when(model.status_id){
            STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {

            }
            else -> {
            }
        }*/
        startActivity(Intent(context, DetailPermohonanActivity::class.java).putExtra("data", model))

    }
}
