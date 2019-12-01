package id.go.kemlu.legalisasidokumen.app.daftarlayanan

import id.go.kemlu.legalisasidokumen.data.models.DaftarRequest
import lib.gmsframeworkx.base.BaseView

interface DaftarLayananView {
    interface Presenter{
        fun requestDaftarLayanan(isReload: Boolean)
    }

    interface View: BaseView {
        fun onRequestDaftarLayanan(list: MutableList<DaftarRequest>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
    }
}