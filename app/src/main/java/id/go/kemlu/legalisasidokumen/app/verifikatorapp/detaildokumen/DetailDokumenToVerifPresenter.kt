package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DetailDokumenToVerifPresenter(context: Context, view: DetailDokumenToVerifView.View) : BasePresenter(context), DetailDokumenToVerifView.Presenter {

    var view: DetailDokumenToVerifView.View

    init {
        this.view = view
    }

    override fun updateDocument(reqid: String, docid: String, status: String, note: String) {
        GmsRequest.POST(EndPoints.stringUpdateDocumentStatus(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onUpdateDocument(true, response.getString("message"))
                    } else {
                        view!!.onUpdateDocument(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onUpdateDocument(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onUpdateDocument(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["request_id"] = reqid
                param["document_id"] = docid
                param["status"] = status
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