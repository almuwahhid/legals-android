package id.go.kemlu.legalisasidokumen.app.daftarlayanan

import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import lib.gmsframeworkx.base.BaseView

interface DaftarLayananView {
    interface Presenter{
        fun requestDaftarLayanan(isReload: Boolean, status_id: Int)
        fun getDetailRequest(requestModel: RequestModel)
    }

    interface View: BaseView {
        fun onRequestDaftarLayanan(list: MutableList<RequestModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
        fun onGetDetailRequest(requestModel: RequestModel)
        fun onLoadingDetail()
        fun noInternetConnection(isFirst: Boolean)
    }
}