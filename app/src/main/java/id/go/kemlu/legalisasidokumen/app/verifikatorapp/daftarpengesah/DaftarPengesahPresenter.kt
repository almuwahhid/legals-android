package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.PengesahModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class DaftarPengesahPresenter(context: Context, view: DaftarPengesahView.View) : BasePresenter(context), DaftarPengesahView.Presenter {

    var view: DaftarPengesahView.View
    var page = 1

    init {
        this.view = view
    }

    override fun requestPengesah(isReload: Boolean) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringDaftarPengesah(""+page), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<PengesahModel>()
                        val data = response.getJSONArray("result")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), PengesahModel::class.java)
                            )
                        }
                        page++
                        view.onRequestPengesah(list, isReload)
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

    override fun requestPejabat(isReload: Boolean) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringDaftarPejabat(""+page), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<PengesahModel>()
                        val data = response.getJSONArray("result")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), PengesahModel::class.java)
                            )
                        }
                        page++
                        view.onRequestPengesah(list, isReload)
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
                view!!.onFailedRequestSomething(if(page == 1)true else false, "Bermasalah dengan Server")
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