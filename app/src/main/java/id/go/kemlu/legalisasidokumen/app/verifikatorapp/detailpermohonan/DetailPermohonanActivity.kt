package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpermohonan

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.adapters.DokumenSayaAdapter
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen.DetailDokumenToVerifActivity
import id.go.kemlu.legalisasidokumen.data.models.DokumenModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import kotlinx.android.synthetic.main.activity_detail_permohonan.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class DetailPermohonanActivity : LegalisasiActivity(), DetailPermohonanView.View {

    lateinit var model:RequestModel
    lateinit var adapter : DokumenSayaAdapter
    lateinit var presenter : DetailPermohonanPresenter
    var list : MutableList<DokumenModel> = ArrayList()
    var isNeedtoUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_permohonan)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as RequestModel
        } else {
            finish()
        }

        presenter = DetailPermohonanPresenter(context, this)
        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            toolbar_title.setText("Detail Permohonan")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        adapter = DokumenSayaAdapter(context, list, object: DokumenSayaAdapter.OnDokumenSayaAdapter{
            override fun onDokumenClick(dokumenModel: DokumenModel) {
                isNeedtoUpdate = true
                startActivity(Intent(context, DetailDokumenToVerifActivity::class.java).putExtra("data", dokumenModel).putExtra("parent", model))
            }
        })

        initData(model)
    }

    private fun initData(model: RequestModel){
        list.clear()
        list.addAll(model.document)
        adapter.notifyDataSetChanged()
        tv_nopermohonan.setText(""+model.group_no)
        tv_bukti_tglverif.setText(""+model.open_date)
        tv_bukti_jumlahdokumen.setText(""+model.document.size)
        rv_bukti.layoutManager = LinearLayoutManager(context)
        rv_bukti.adapter = adapter
        if(model.document.size == 0){
            tv_dokumendisetujui.visibility = View.GONE
        }
    }

    override fun onRequestDetailPermohonan(requestModel: RequestModel) {
        model = requestModel
        initData(requestModel)
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(this, R.drawable.ic_logo)
    }

    override fun onResume() {
        super.onResume()
        if(isNeedtoUpdate){
            isNeedtoUpdate = false
            presenter.requestDetailPermohonan(model)
        }
    }
}
