package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah

import id.go.kemlu.legalisasidokumen.data.models.PengesahModel
import lib.gmsframeworkx.base.BaseView

interface DaftarPengesahView {
    interface View: BaseView {
        fun onRequestPengesah(list: MutableList<PengesahModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
    }
    interface Presenter{
        fun requestPengesah(isReload: Boolean)
        fun requestPejabat(isReload: Boolean)
    }
}