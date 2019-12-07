package id.go.kemlu.legalisasidokumen.dialogs.DialogUploadBuktiBayar

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DialogUploadBuktiPresenter(context: Context, view : DialogUploadBuktiView.View) : BasePresenter(context), DialogUploadBuktiView.Presenter {

    var view : DialogUploadBuktiView.View

    init {
        this.view = view
    }

    override fun uploadBukti(base64: String, reqid: String) {
        GmsRequest.POST(EndPoints.stringUploadPaymentTransfer(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onUploadBukti(true, response.getString("message"))
                    } else {
                        view!!.onUploadBukti(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onUploadBukti(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onUploadBukti(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["request_id"] = reqid
                param["image"] = base64
                param["date_time"] = ""
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                Log.d("gmsHeader", "onResponse: "+ LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+ LegalisasiFunction.getUserPreference(context).access_token)
                return param
            }
        })
    }
}