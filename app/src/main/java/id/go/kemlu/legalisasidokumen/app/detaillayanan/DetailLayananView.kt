package id.go.kemlu.legalisasidokumen.app.detaillayanan

import lib.gmsframeworkx.base.BaseView

interface DetailLayananView {
    interface Presenter{
        fun requestKualitasLayanan()
    }

    interface View: BaseView{
        fun onAfterSendingKualitasLayanan()
    }
}