package id.go.kemlu.legalisasidokumen.dialogs.DialogVerifikasiPermohonan

import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import lib.gmsframeworkx.base.BaseView

interface DialogVerifikasiPermohonanView {
    interface View: BaseView{
        fun onVerifikasiPermohonan(isSuccess: Boolean, message: String)
    }

    interface Presenter{
        fun verifikasiPermohonan(requestModel: RequestModel, status: String, note : String)
    }
}