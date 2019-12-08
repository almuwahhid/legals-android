package id.go.kemlu.legalisasidokumen.dialogs.DialogRequestIkm

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.IkmModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class DialogRequestIkmPresenter(context: Context, view: DialogRequestIkmView.View) : BasePresenter(context), DialogRequestIkmView.Presenter {

    var view: DialogRequestIkmView.View

    init {
        this.view = view
    }

    override fun requestIkmByDate(date1: String, date2: String) {
        GmsRequest.GET(EndPoints.stringGetIKM(date1, date2), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onRequestIkm(gson.fromJson(response.getJSONObject("result").toString(), IkmModel::class.java))
                    } else {
                        view!!.onFailedRequestSomething(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestSomething("Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
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