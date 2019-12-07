package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen

import lib.gmsframeworkx.base.BaseView

interface DetailDokumenToVerifView {
    interface View: BaseView{
        fun onUpdateDocument(isSuccess: Boolean, message: String)
    }
    interface Presenter{
        fun updateDocument(reqid: String, docid: String, status: String, note: String)
    }
}