package id.go.kemlu.legalisasidokumen.app.daftarlayanan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.daftarlayanan.adapter.DaftarLayananAdapter
import id.go.kemlu.legalisasidokumen.app.home.HomeViewModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_daftar_layanan.*
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.GmsStatic
import id.go.kemlu.legalisasidokumen.app.detaillayanan.DetailLayananActivity

class DaftarLayananFragment : Fragment(), DaftarLayananView.View {


    lateinit var daftarLayananAdapter: DaftarLayananAdapter
    var layananModels: MutableList<RequestModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    internal lateinit var presenter:DaftarLayananPresenter
    internal var status_id: Int = 0

    companion object{
        public fun newInstance(viewModel: HomeViewModel, status_id: Int): DaftarLayananFragment {
            val fragment = DaftarLayananFragment()
            fragment.status_id = status_id
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_daftar_layanan, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
        helper_loading_more.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom)

        presenter = DaftarLayananPresenter(context, this)
        daftarLayananAdapter = DaftarLayananAdapter(context!!, layananModels, object : DaftarLayananAdapter.OnDaftarLayananAdapter{
            override fun onLayananClick(model: RequestModel) {
                presenter.getDetailRequest(model)
            }
        })
        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = daftarLayananAdapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestDaftarLayanan(false, status_id)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestDaftarLayanan(true, status_id)
        }

        presenter.requestDaftarLayanan(true, status_id)

    }

    override fun onGetDetailRequest(model: RequestModel) {
        startActivity(Intent(context, DetailLayananActivity::class.java).putExtra("data", model))
    }

    override fun onRequestDaftarLayanan(list: MutableList<RequestModel>, isReload: Boolean) {
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

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoadingDetail() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
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
}
