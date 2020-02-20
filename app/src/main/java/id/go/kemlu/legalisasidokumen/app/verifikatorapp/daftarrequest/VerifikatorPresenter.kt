package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarrequest

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.data.models.RequestToVerifModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class VerifikatorPresenter(context: Context, view : VerifikatorView.View) : BasePresenter(context),
    VerifikatorView.Presenter {

    var view : VerifikatorView.View
    var page = 1

    init {
        this.view = view
    }

    override fun requestDataVerifikasi(isReload: Boolean) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringDaftarRequestToVerif(""+page, ""), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<RequestToVerifModel>()
                        val result = response.getJSONObject("result")
                        val data = result.getJSONArray("list_request_to_verif")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), RequestToVerifModel::class.java)
                            )
                        }
                        if(list.size>0){
                            page++
                            view.onRequestDataVerifikasi(list, isReload)
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

    override fun requestDetail(model: RequestToVerifModel) {
        GmsRequest.GET(EndPoints.stringDetailRequestByGroup(""+model.group_no), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoadingDetail()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val result = response.getJSONObject("result")
                        view.onRequestDetail(gson.fromJson(result.toString(), RequestModel::class.java))
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