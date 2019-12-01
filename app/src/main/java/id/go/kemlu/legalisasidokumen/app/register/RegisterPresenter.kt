package id.ac.uny.riset.ride.menu.register

import android.content.Context
import id.go.kemlu.legalisasidokumen.app.register.model.RegisterUiModel
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class RegisterPresenter(context: Context?, view: RegisterView.View): BasePresenter(context), RegisterView.Presenter {
    internal var view: RegisterView.View? = null

    init {
        this.view = view
    }

    override fun  submitRegister(registerUiModel: RegisterUiModel) {
        GmsRequest.POST(EndPoints.stringRegister(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onSubmitRegister(true, response.getString("message"))
                    } else {
                        view!!.onSubmitRegister(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onSubmitRegister(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onSubmitRegister(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["user_name"] = registerUiModel.user_name
                param["user_nip"] = registerUiModel.user_nip
                param["user_phone"] = registerUiModel.user_phone
                param["user_email"] = registerUiModel.user_email
                param["user_password"] = registerUiModel.user_password
                param["user_re_password"] = registerUiModel.user_re_password
                param["user_re_password"] = registerUiModel.user_re_password
                param["user_photo"] = registerUiModel.user_photo
                param["device_type"] = registerUiModel.device_type
                param["user_type"] = registerUiModel.user_type
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }


}