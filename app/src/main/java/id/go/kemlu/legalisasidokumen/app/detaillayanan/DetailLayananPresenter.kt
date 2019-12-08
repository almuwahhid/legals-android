package id.go.kemlu.legalisasidokumen.app.detaillayanan

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DetailLayananPresenter(context: Context, view : DetailLayananView.View) : BasePresenter(context), DetailLayananView.Presenter {

    var view : DetailLayananView.View

    init {
        this.view = view

    }

    override fun requestKualitasLayanan(reqid: String, ikm_index: String, ikm_comment: String) {
        GmsRequest.POST(EndPoints.stringSubmitIKM(), context, object : GmsRequest.OnPostRequest{

            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject?) {
                view!!.onHideLoading()
                try {
                    if (response!!.getBoolean("success")) {
                        view.onAfterSendingKualitasLayanan(response.getString("message"))
                    } else {
                        view!!.onErrorSendData(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String?) {
                view!!.onHideLoading()
                view!!.onErrorSendData("Bermasalah dengan Server")
            }

            override fun requestParam(): MutableMap<String, String> {
                val param = HashMap<String, String>()
                param.put("request_id", reqid)
                param.put("ikm_index", ikm_index)
                param.put("ikm_comment", ikm_comment)
                return param
            }

            /*override fun requestParam(): String {
                Log.d("")
                return gson.toJson(permohonanUiModel)
            }*/

            override fun requestHeaders(): MutableMap<String, String> {
                Log.d("gmsHeader", "onResponse: "+ LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+ LegalisasiFunction.getUserPreference(context).access_token)
                return param
            }

        })

    }
}