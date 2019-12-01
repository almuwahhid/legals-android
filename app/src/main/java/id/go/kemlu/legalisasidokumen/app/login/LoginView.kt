package id.go.kemlu.legalisasidokumen.app.login

import id.go.kemlu.legalisasidokumen.data.models.UserModel
import lib.gmsframeworkx.base.BaseView

interface LoginView {
    interface Presenter{
        abstract fun requestLogin(loginUiModel: LoginUiModel)
        abstract fun authEmail(email: String)
    }

    interface View: BaseView {
        abstract fun onSuccessLogin(model: UserModel)
        abstract fun onSuccessAuthEmail(message: String)
        abstract fun onRegisterAuthEmail(email: String)
        abstract fun onForgotAuthEmail(email: String)
        abstract fun onFailedLogin(message: String)
        abstract fun onFailedAuthEmail(message: String)
    }
}