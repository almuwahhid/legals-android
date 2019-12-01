package id.go.kemlu.legalisasidokumen.app.buatpermohonan

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.app.buatpermohonan.model.BuatPermohonanUiModel
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.CountryModel
import id.go.kemlu.legalisasidokumen.data.models.InstansiModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class BuatPermohonanPresenter(context: Context?, view: BuatPermohonanView.View) : BasePresenter(context), BuatPermohonanView.Presenter {

    var view : BuatPermohonanView.View
    init {
        this.view = view
    }

    override fun submitPermohonan(permohonanUiModel: BuatPermohonanUiModel) {
        GmsRequest.POST(EndPoints.stringBuatPermohonan(), context, object : GmsRequest.OnPostRequest{

            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject?) {
                view!!.onHideLoading()
                try {
                    if (response!!.getBoolean("success")) {
                        view!!.onSuccessSubmitPermohonan(response.getString("message"))
                    } else {
                        view!!.onFailedRequestSomething(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String?) {
                view!!.onHideLoading()
                view!!.onFailedRequestSomething("Bermasalah dengan Server")
            }

            override fun requestParam(): MutableMap<String, String> {
                val param = HashMap<String, String>()
                param.put("data", gson.toJson(permohonanUiModel))
                return param
            }

            /*override fun requestParam(): String {
                Log.d("")
                return gson.toJson(permohonanUiModel)
            }*/

            override fun requestHeaders(): MutableMap<String, String> {
                Log.d("gmsHeader", "onResponse: "+LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+LegalisasiFunction.getUserPreference(context).access_token)
                return param
            }

        })
    }

    override fun requestInstansi() {
        GmsRequest.GET(EndPoints.stringReferensiInstansi(), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val list = ArrayList<InstansiModel>()
                        val data = response.getJSONArray("result")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), InstansiModel::class.java)
                            )
                        }
                        view.onRequestInstansi(list)
                    } else {
                        view!!.onFailedRequestSomething(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestSomething("Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }

    override fun requestCountry() {
        GmsRequest.GET(EndPoints.stringReferensiNegara(), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val list = ArrayList<CountryModel>()
                        val data = response.getJSONArray("result")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), CountryModel::class.java)
                            )
                        }
                        view.onRequestCountry(list)
                    } else {
                        view!!.onFailedRequestSomething(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestSomething("Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                return param
            }

            override fun requestHeaders(): Map<String, String> {
                return HashMap()
            }
        })
    }
}