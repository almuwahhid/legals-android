package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class DaftarPembayaranPresenter(context: Context, view: DaftarPembayaranView.View) : BasePresenter(context), DaftarPembayaranView.Presenter {

    var view: DaftarPembayaranView.View
    var page = 1

    init {
        this.view = view
    }

    override fun requestDaftarLayanan(isReload: Boolean) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringDaftarPembayaranToVerif(""+page, ""), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                if(isReload)
                    view!!.onLoading()
                else
                    view!!.onLoadingMore()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading(isReload)
                try {
                    if (response.getBoolean("success")) {
                        val list = ArrayList<PembayaranToVerifModel>()
                        val result = response.getJSONObject("result")
                        val data = result.getJSONArray("list_online_request")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), PembayaranToVerifModel::class.java)
                            )
                        }
                        if(list.size>0){
                            page++
                            view.onRequestDaftarLayanan(list, isReload)
                        } else {
                            view!!.onFailedRequestMore(if(page == 1)true else false, response.getString("message"))
                        }
                    } else {
//                        view!!.onFailedRequestSomething(if(page == 1)true else false, response.getString("message"))
                        view!!.onFailedRequestMore(if(page == 1)true else false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading(isReload)
                if(error.equals("1019") || error.equals("1012")){
                    view!!.noInternetConnection(isReload)
                } else {
                    view!!.onFailedRequestSomething(if(page == 1)true else false, "Bermasalah dengan Server")
                }
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