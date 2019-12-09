package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import lib.gmsframeworkx.base.BaseView

interface DetailPembayaranToVerifView {
    interface View : BaseView{
        fun onUpdatePembayaran(isSuccess: Boolean, message: String)
    }
    interface Presenter{
        fun updatePembayaran(request_id: String, status_id: String, note: String)
    }
}