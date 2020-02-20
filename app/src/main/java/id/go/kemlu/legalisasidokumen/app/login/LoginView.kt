package id.go.kemlu.legalisasidokumen.app.login

import id.go.kemlu.legalisasidokumen.data.models.UserModel
import lib.gmsframeworkx.base.BaseView

interface LoginView {
    interface Presenter{
        abstract fun requestLogin(loginUiModel: LoginUiModel)
        abstract fun authEmail(email: String)
        abstract fun authGoogle(token: String)
        abstract fun oauthFacebook(token: String)
        abstract fun updateTokenFirebase(token: String, model: UserModel)
    }

    interface View: BaseView {
        abstract fun onSuccessLogin(model: UserModel)
        abstract fun onSuccessAuthEmail(message: String, name: String, email: String)
        abstract fun onRegisterAuthEmail(email: String)
        abstract fun onForgotAuthEmail(email: String)
        abstract fun onFailedLogin(message: String)
        abstract fun onFailedLoginThirdParty(message: String)
        abstract fun onFailedAuthEmail(message: String)
        abstract fun onSuccessUpdateTokenFirebase(model: UserModel)
    }
}