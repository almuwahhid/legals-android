package id.go.kemlu.legalisasidokumen.dialogs.DialogUploadBuktiBayar

import lib.gmsframeworkx.base.BaseView

interface DialogUploadBuktiView {
    interface View: BaseView{
        fun onUploadBukti(isSuccess: Boolean, message: String)
    }
    interface Presenter{
        fun uploadBukti(base64: String, reqid: String)
    }
}