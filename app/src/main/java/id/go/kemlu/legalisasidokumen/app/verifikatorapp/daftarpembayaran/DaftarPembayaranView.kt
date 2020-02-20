package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran

import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import lib.gmsframeworkx.base.BaseView

interface DaftarPembayaranView {
    interface Presenter{
        fun requestDaftarLayanan(isReload: Boolean)
    }

    interface View: BaseView{
        fun onRequestDaftarLayanan(list: MutableList<PembayaranToVerifModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
        fun noInternetConnection(isFirst: Boolean)
    }
}