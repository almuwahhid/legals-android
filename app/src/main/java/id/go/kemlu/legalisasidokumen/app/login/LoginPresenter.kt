package id.go.kemlu.legalisasidokumen.app.login

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import lib.gmsframeworkx.utils.VolleyMultipartRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class LoginPresenter(context: Context?, view: LoginView.View) : BasePresenter(context), LoginView.Presenter {

    internal var view: LoginView.View? = null
    init {
        this.view = view
    }

    override fun authGoogle(token: String) {
        GmsRequest.POST(EndPoints.stringLoginGoogle(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        if(response.getInt("email_status") == 0){
                            view!!.onFailedLoginThirdParty(response.getString("message"))
                        } else {
                            view!!.onSuccessLogin(gson!!.fromJson(response.getJSONObject("result").toString(), UserModel::class.java))
                        }
                    } else {
                        view!!.onFailedLoginThirdParty(response.getString("message"))
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
                param["id_token"] = token
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }

    override fun updateTokenFirebase(token: String, model: UserModel) {
        GmsRequest.POSTFormData(EndPoints.stringUpdateDeviceToken(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onSuccessUpdateTokenFirebase(model)
                    } else {
                        view!!.onFailedLoginThirdParty(response.getString("message"))
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
                param["device_token"] = token
                param["device_type"] = "ANDROID"
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                Log.d("gmsHeader", "onResponse: "+ LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+ LegalisasiFunction.getUserPreference(context).access_token)
//                param.put("Content-Type", "application/json")
                return param
            }
        })

        /*GmsRequest.POSTMultipart(EndPoints.stringUpdateDeviceToken(), context, object: GmsRequest.OnMultipartRequest{
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onSuccessUpdateTokenFirebase(model)
                    } else {
                        view!!.onFailedLoginThirdParty(response.getString("message"))
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
                param["device_token"] = token
                param["device_type"] = "ANDROID"
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                Log.d("gmsHeader", "onResponse: "+ LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+ LegalisasiFunction.getUserPreference(context).access_token)
                return param
            }

            override fun requestData(): MutableMap<String, VolleyMultipartRequest.DataPart> {
                return HashMap<String, VolleyMultipartRequest.DataPart>()
            }

        })*/
    }

    override fun oauthFacebook(token: String) {
        GmsRequest.POST(EndPoints.stringLoginFacebook(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        if(response.getInt("email_status") == 0){
                            view!!.onFailedLoginThirdParty(response.getString("message"))
                        } else {
                            view!!.onSuccessLogin(gson!!.fromJson(response.getJSONObject("result").toString(), UserModel::class.java))
                        }
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
                param["access_token"] = token
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }

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
                            var result = response.getJSONObject("result")
                            view!!.onSuccessAuthEmail(response.getString("message"), result.getString("user_name"), result.getString("user_email"))
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