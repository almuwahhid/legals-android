package id.go.kemlu.legalisasidokumen.app.detaillayanan

import lib.gmsframeworkx.base.BaseView

interface DetailLayananView {
    interface Presenter{
        fun requestKualitasLayanan(reqid: String, ikm_index: String, ikm_comment: String)
    }

    interface View: BaseView{
        fun onAfterSendingKualitasLayanan(message: String)
        fun onErrorSendData(message: String)
    }
}