package id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat

import android.content.Context
import android.util.Log
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.*
import id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat.model.AddPejabatUiModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DialogAddPejabatPresenter(context: Context, view : DialogAddPejabatView.View) : BasePresenter(context), DialogAddPejabatView.Presenter {

    var view : DialogAddPejabatView.View
    init {
        this.view = view
    }

    override fun addPejabat(addPejabatUiModel: AddPejabatUiModel) {
        GmsRequest.POST(EndPoints.stringRequestAddPejabat(), context, object : GmsRequest.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view!!.onAddPejabat(true, response.getString("message"))
                    } else {
                        view!!.onAddPejabat(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    view!!.onAddPejabat(false, response.getString("message"))
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onAddPejabat(false, "Bermasalah dengan Server")
            }

            override fun requestParam(): Map<String, String> {
                val param = HashMap<String, String>()
                param["name"] = addPejabatUiModel.name
                param["nip"] = addPejabatUiModel.nip
                param["position_name"] = addPejabatUiModel.position_name
                param["official_institution_id"] = addPejabatUiModel.official_institution_id
                param["official_institution_name"] = addPejabatUiModel.official_institution_name
                param["level_id"] = addPejabatUiModel.level_id
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

    override fun requestJabatan() {
        GmsRequest.GET(EndPoints.stringDaftarJabatan(), context, object : GmsRequest.OnGetRequest {
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
                            val x = gson.fromJson(result.getJSONObject(i).toString(), OfficialPositionModel::class.java)
                            list.add(PickerModel(""+x.position_id, x.position_name))
                        }
                        view!!.onRequestJabatan(list)
                    } else {
                        view!!.onAddPejabat(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onAddPejabat(false, "Bermasalah dengan Server")
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
                        view!!.onAddPejabat(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onAddPejabat(false, "Bermasalah dengan Server")
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