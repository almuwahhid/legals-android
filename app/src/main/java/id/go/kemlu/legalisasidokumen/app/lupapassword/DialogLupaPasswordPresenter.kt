package id.ac.uny.riset.ride.utils.dialogs.DialogLupaPassword

import android.content.Context
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DialogLupaPasswordPresenter(context: Context?, view: DialogLupaPasswordView.View) : BasePresenter(context), DialogLupaPasswordView.Presenter {
    internal var view: DialogLupaPasswordView.View? = null
    init {
        this.view = view
    }
    override fun requestLupaPassword(email: String) {
        GmsRequest.POST(EndPoints.stringLupaPassword(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if(response.getBoolean("success")){
                        view!!.onRequestLupaPassword(true, response.getString("message"))
                    } else {
                        view!!.onRequestLupaPassword(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onErrorConnection()
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param.put("data", email)
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }
}