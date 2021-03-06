package id.go.kemlu.legalisasidokumen.app.daftarlayanan

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class DaftarLayananPresenter(context: Context?, daftarLayananView: DaftarLayananView.View) : BasePresenter(context), DaftarLayananView.Presenter {

    var view: DaftarLayananView.View
    var page = 1

    init {
        this.view = daftarLayananView
    }

    override fun requestDaftarLayanan(isReload: Boolean, status_id: Int) {
        if(isReload){
           page = 1
        }
        GmsRequest.GET(EndPoints.stringDaftarRequest(""+page), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<RequestModel>()
                        val result = response.getJSONObject("result")
                        val data = result.getJSONArray("list_online_request")
                        for (i in 0 until data.length()) {
                            val requestModel = gson.fromJson(data.getJSONObject(i).toString(), RequestModel::class.java)
                            if(status_id == 0){
                                list.add(
                                    requestModel
                                )
                            } else {
                                if(requestModel.status_id == status_id){
                                    list.add(
                                        requestModel
                                    )
                                }
                            }
                        }
                        if(list.size>0){
                            page++
                            view.onRequestDaftarLayanan(list, isReload)
                        } else {
                            view!!.onFailedRequestMore(if(page == 1)true else false, "Data saat ini tidak tersedia.")
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
                Log.d("error", error)
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

    override fun getDetailRequest(requestModel: RequestModel) {
        GmsRequest.GET(EndPoints.stringDetailRequestByGroup(""+requestModel.group_no), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoadingDetail()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val result = response.getJSONObject("result")
                        view.onGetDetailRequest(gson.fromJson(result.toString(), RequestModel::class.java))
                    } else {
                        view!!.onFailedRequestMore(false, response.getString("message"))
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