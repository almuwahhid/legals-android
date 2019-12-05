package id.go.kemlu.legalisasidokumen.app.notifikasi

import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel
import lib.gmsframeworkx.base.BaseView

interface NotifikasiView {
    interface View: BaseView{
        fun onRequestNotifikasi(list: MutableList<NotifikasiModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
    }
    interface Presenter{
        fun requestNotifikasi(isReload: Boolean)
    }
}