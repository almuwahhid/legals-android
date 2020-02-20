package id.go.kemlu.legalisasidokumen.app.detaillayanan

import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import lib.gmsframeworkx.base.BaseView

interface DetailLayananView {
    interface Presenter{
        fun requestKualitasLayanan(reqid: String, ikm_index: String, ikm_comment: String)
        fun requestDetailNotifikasi(id: String, notif_id: String)
    }

    interface View: BaseView{
        fun onAfterSendingKualitasLayanan(message: String)
        fun onErrorSendData(message: String)
        fun onRequestDetailNotifikasi(requestModel: RequestModel)
    }
}