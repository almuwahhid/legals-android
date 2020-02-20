package id.ac.uny.riset.ride.menu.register

import id.go.kemlu.legalisasidokumen.app.register.model.RegisterUiModel
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import lib.gmsframeworkx.base.BaseView


interface RegisterView {
    interface Presenter{
        fun submitRegister(registerUiModel: RegisterUiModel, isneedPw : Boolean)
    }

    interface View: BaseView {
        fun onSubmitRegister(isSuccess: Boolean, message: String)
    }
}