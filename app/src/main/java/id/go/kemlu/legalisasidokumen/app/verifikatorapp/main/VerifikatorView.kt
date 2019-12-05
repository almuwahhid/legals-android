package id.go.kemlu.legalisasidokumen.app.verifikatorapp.main

import id.go.kemlu.legalisasidokumen.data.models.RequestToVerifModel
import lib.gmsframeworkx.base.BaseView

interface VerifikatorView {
    interface Presenter{
        fun requestDataVerifikasi(isReload: Boolean)
    }
    interface View: BaseView{
        fun onRequestDataVerifikasi(list: MutableList<RequestToVerifModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
    }
}