package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpermohonan

import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import lib.gmsframeworkx.base.BaseView

interface DetailPermohonanView {
    interface Presenter{
        fun requestDetailPermohonan(requestModel: RequestModel)
    }

    interface View: BaseView{
        fun onRequestDetailPermohonan(requestModel: RequestModel)
    }

}