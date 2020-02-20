package id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat

import id.go.kemlu.legalisasidokumen.data.models.PejabatModel
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat.model.AddPejabatUiModel
import lib.gmsframeworkx.base.BaseView

interface DialogAddPejabatView {
    interface View: BaseView{
        fun onRequestJabatan(list: MutableList<PickerModel>)
        fun onRequestLembaga(list: MutableList<PickerModel>)
        fun onAddPejabat(isSuccess: Boolean, message: String)
    }

    interface Presenter{
        fun requestJabatan()
        fun requestLembaga()
        fun addPejabat(addPejabatUiModel: AddPejabatUiModel)
    }
}