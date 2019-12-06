package id.go.kemlu.legalisasidokumen.app.notifikasi

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel
import id.go.kemlu.legalisasidokumen.data.models.*
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class NotifikasiPresenter(context: Context, view: NotifikasiView.View) : BasePresenter(context), NotifikasiView.Presenter {

    var view: NotifikasiView.View
    var page = 1

    init {
        this.view = view
    }

    override fun requestNotifikasi(isReload: Boolean) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringNotifikasi(""+page), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<NotifikasiModel>()
                        val result = response.getJSONObject("result")
                        val data = result.getJSONArray("list_notif")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), NotifikasiModel::class.java)
                            )
                        }
                        page++
                        view.onRequestNotifikasi(list, isReload)
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

    override fun requestDetailNotifikasi(id: String) {
        GmsRequest.GET(EndPoints.stringDetailRequestByGroup(""+id), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoadingDetail()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val result = response.getJSONObject("result")
                        view.onRequestDetailNotifikasi(gson.fromJson(result.toString(), RequestModel::class.java))
                    } else {
                        view!!.onFailedRequestSomething(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestSomething(false, "Bermasalah dengan Server")
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