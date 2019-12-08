package id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.IKMCommentModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject

class IKMPresenter(context: Context, view: IKMView.View) : BasePresenter(context), IKMView.Presenter {

    var view: IKMView.View
    var page = 1

    init {
        this.view = view
    }

    override fun requestComment(isReload: Boolean, date1: String, date2: String) {
        if(isReload){
            page = 1
        }
        GmsRequest.GET(EndPoints.stringGetIKMComment(date1, date2, ""+page), context, object : GmsRequest.OnGetRequest {
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
                        val list = ArrayList<IKMCommentModel>()
                        val result = response.getJSONObject("result")
                        val data = result.getJSONArray("data_ikm_comment")
                        for (i in 0 until data.length()) {
                            val requestModel = gson.fromJson(data.getJSONObject(i).toString(), IKMCommentModel::class.java)
                            list.add(
                                requestModel
                            )
                        }
                        if(list.size>0){
                            page++
                            view.onRequestIkm(list, isReload)
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