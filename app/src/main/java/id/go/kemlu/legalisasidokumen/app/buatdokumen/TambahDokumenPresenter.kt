package id.go.kemlu.legalisasidokumen.app.buatdokumen

import android.content.Context
import id.go.kemlu.legalisasidokumen.data.EndPoints
import id.go.kemlu.legalisasidokumen.data.models.JenisDokumenModel
import lib.gmsframeworkx.base.BasePresenter
import lib.gmsframeworkx.utils.GmsRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class TambahDokumenPresenter(context: Context?, view: TambahDokumenView.View) : BasePresenter(context), TambahDokumenView.Presenter {

    lateinit var view : TambahDokumenView.View

    init {
        this.view = view
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