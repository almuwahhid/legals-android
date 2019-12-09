package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DetailPembayaranToVerifPresenter(context: Context, view: DetailPembayaranToVerifView.View) : BasePresenter(context), DetailPembayaranToVerifView.Presenter {

    var view: DetailPembayaranToVerifView.View

    init{
        this.view = view
    }

    override fun updatePembayaran(request_id: String, status_id: String, note: String) {
        GmsRequest.POST(EndPoints.stringUpdateDocumentStatus(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onUpdatePembayaran(true, response.getString("message"))
                    } else {
                        view!!.onUpdatePembayaran(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onUpdatePembayaran(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onUpdatePembayaran(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["request_id"] = request_id
                param["status_id"] = status_id
                param["note"] = note
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