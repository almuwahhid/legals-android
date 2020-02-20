package id.go.kemlu.legalisasidokumen.app.buatdokumen

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.JenisDokumenModel
import id.go.kemlu.legalisasidokumen.data.models.OfficialInstitutionModel
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class TambahDokumenPresenter(context: Context?, view: TambahDokumenView.View) : BasePresenter(context), TambahDokumenView.Presenter {
    var view : TambahDokumenView.View

    init {
        this.view = view
    }

    override fun requestLembaga() {
        GmsRequest.GET(EndPoints.stringDaftarInstansi(), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                Log.d("asdResulti ", ""+response.toString())
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        var list = ArrayList<PickerModel>()
                        val result = response.getJSONArray("result")
                        for (i in 0 until result.length()) {
                            val x = gson.fromJson(result.getJSONObject(i).toString(), OfficialInstitutionModel::class.java)
                            list.add(PickerModel(""+x.official_institution_id, x.official_institution_name))
                        }
                        view!!.onRequestLembaga(list)
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
                Log.d("gmsHeader", "onResponse: "+ LegalisasiFunction.getUserPreference(context).access_token)
                val param = HashMap<String, String>()
                param.put("Authorization", "Bearer "+ LegalisasiFunction.getUserPreference(context).access_token)
                return param
            }
        })
    }

    override fun requestJenisDokumen() {
        GmsRequest.GET(EndPoints.stringReferensiTipeDokumen(), context, object : GmsRequest.OnGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val list = ArrayList<JenisDokumenModel>()
                        val data = response.getJSONArray("result")
                        for (i in 0 until data.length()) {
                            list.add(
                                gson.fromJson(data.getJSONObject(i).toString(), JenisDokumenModel::class.java)
                            )
                        }
                        view.onRequestJenisDokumen(list)
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