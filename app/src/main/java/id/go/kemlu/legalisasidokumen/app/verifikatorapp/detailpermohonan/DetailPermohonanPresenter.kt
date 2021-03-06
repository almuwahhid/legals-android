package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpermohonan

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DetailPermohonanPresenter(context: Context, view: DetailPermohonanView.View) : BasePresenter(context), DetailPermohonanView.Presenter {

    var view: DetailPermohonanView.View

    init {
        this.view = view
    }

    override fun requestDetailPermohonan(model: RequestModel) {
        GmsRequest.GET(EndPoints.stringDetailRequestByGroup(""+model.group_no), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val result = response.getJSONObject("result")
                        view.onRequestDetailPermohonan(gson.fromJson(result.toString(), RequestModel::class.java))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
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