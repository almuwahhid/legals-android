package id.go.kemlu.legalisasidokumen.app.buatdokumen

import id.go.kemlu.legalisasidokumen.data.models.JenisDokumenModel
import lib.gmsframeworkx.base.BaseView

interface TambahDokumenView {
    interface Presenter{
        fun requestJenisDokumen()
    }
    interface View: BaseView {
        fun onRequestJenisDokumen(list: MutableList<JenisDokumenModel>)
        fun onFailedRequestSomething(message: String)
    }
}