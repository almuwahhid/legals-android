package id.go.kemlu.legalisasidokumen.app.buatpermohonan

import id.go.kemlu.legalisasidokumen.app.buatpermohonan.model.BuatPermohonanUiModel
import id.go.kemlu.legalisasidokumen.data.models.CountryModel
import id.go.kemlu.legalisasidokumen.data.models.InstansiModel
import lib.gmsframeworkx.base.BaseView

interface BuatPermohonanView {
    interface Presenter{
        fun requestInstansi()
        fun requestCountry()
        fun submitPermohonan(permohonanUiModel: BuatPermohonanUiModel)
    }
    interface View: BaseView{
        fun onRequestInstansi(list: MutableList<InstansiModel>)
        fun onRequestCountry(list: MutableList<CountryModel>)
        fun onFailedRequestSomething(message: String)
        fun onSuccessSubmitPermohonan(message: String)
    }
}