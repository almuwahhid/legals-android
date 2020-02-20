package id.go.kemlu.legalisasidokumen.app.buatdokumen

import id.go.kemlu.legalisasidokumen.data.models.JenisDokumenModel
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import lib.gmsframeworkx.base.BaseView

interface TambahDokumenView {
    interface Presenter{
        fun requestJenisDokumen()
        fun requestLembaga()
    }
    interface View: BaseView {
        fun onRequestJenisDokumen(list: MutableList<JenisDokumenModel>)
        fun onRequestLembaga(list: MutableList<PickerModel>)
        fun onFailedRequestSomething(message: String)
    }
}