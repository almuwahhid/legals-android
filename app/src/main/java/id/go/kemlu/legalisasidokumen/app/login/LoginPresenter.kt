package id.go.kemlu.legalisasidokumen.app.login

import android.content.Context
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class LoginPresenter(context: Context?, view: LoginView.View) : BasePresenter(context), LoginView.Presenter {
    override fun authEmail(email: String) {
        GmsRequest.POST(EndPoints.stringAuthEmail(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        if(response.getInt("email_status") == 0){
                            view!!.onFailedLogin(response.getString("message"))
                            view!!.onRegisterAuthEmail(email)
                        } else if(response.getInt("email_status") == 2){
                            view!!.onFailedLogin(response.getString("message"))
                            view!!.onForgotAuthEmail(email)
                        } else {
                            view!!.onSuccessAuthEmail(response.getString("message"))
                        }
                    } else {
                        view!!.onFailedLogin(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedLogin("Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["user_email"] = email
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }

    internal var view: LoginView.View? = null
    init {
        this.view = view
    }
    override fun requestLogin(loginUiModel: LoginUiModel) {
        GmsRequest.POST(EndPoints.stringLogin(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onSuccessLogin(gson!!.fromJson(response.getJSONObject("result").toString(), UserModel::class.java))
                    } else {
                        view!!.onFailedLogin(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedLogin("Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["username"] = loginUiModel.username
                param["password"] = loginUiModel.password
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }
}