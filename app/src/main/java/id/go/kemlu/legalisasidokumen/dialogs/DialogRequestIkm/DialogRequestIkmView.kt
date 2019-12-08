package id.go.kemlu.legalisasidokumen.dialogs.DialogRequestIkm

import id.go.kemlu.legalisasidokumen.data.models.IkmModel
import lib.gmsframeworkx.base.BaseView

interface DialogRequestIkmView {
    interface View: BaseView{
        fun onRequestIkm(ikmModel: IkmModel)
        fun onFailedRequestSomething(message: String)
    }
    interface Presenter{
        fun requestIkmByDate(date1: String, date2: String)
    }
}