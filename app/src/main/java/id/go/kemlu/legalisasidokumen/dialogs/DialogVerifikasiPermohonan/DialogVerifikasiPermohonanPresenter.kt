package id.go.kemlu.legalisasidokumen.dialogs.DialogVerifikasiPermohonan

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DialogVerifikasiPermohonanPresenter(context: Context, view : DialogVerifikasiPermohonanView.View) : BasePresenter(context), DialogVerifikasiPermohonanView.Presenter {

    var view : DialogVerifikasiPermohonanView.View

    init {
        this.view = view
    }

    override fun verifikasiPermohonan(requestModel: RequestModel, status: String, note: String) {
        var accepted = 0
        var rejected = 0
        for(i in requestModel.document){
            if(i.status_id == StaticData.STATUS_MENUNGGU_PEMBAYARAN){
                accepted++
            } else if(i.status_id == StaticData.STATUS_PERMOHONAN_DITOLAK){
                rejected++
            }
        }
        GmsRequest.POST(EndPoints.stringUpdateRequestStatus(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onVerifikasiPermohonan(true, response.getString("message"))
                    } else {
                        view!!.onVerifikasiPermohonan(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onVerifikasiPermohonan(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onVerifikasiPermohonan(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["request_id"] = ""+requestModel.online_request_id
                param["status"] = status
                param["doc_count"] = ""+requestModel.document.size
                param["doc_count_accept"] = ""+accepted
                param["doc_count_reject"] = ""+rejected
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