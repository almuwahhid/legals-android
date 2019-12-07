package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpermohonan

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
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

class DetailPermohonanActivity : LegalisasiActivity() {

    lateinit var model:RequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_permohonan)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as RequestModel
        } else {
            finish()
        }

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        tv_bukti_nopermohonan.setText(""+model.group_no)
        tv_bukti_tglverif.setText(""+model.open_date)
        tv_bukti_jumlahdokumen.setText(""+model.doc_qty)
        rv_bukti.layoutManager = LinearLayoutManager(context)

        val adapter = DokumenSayaAdapter(context, model.document, object: DokumenSayaAdapter.OnDokumenSayaAdapter{
            override fun onDokumenClick(model: DokumenModel) {
                startActivity(Intent(context, DetailDokumenToVerifActivity::class.java).putExtra("data", model))
            }
        })
        rv_bukti.adapter = adapter
        if(model.document.size == 0){
            tv_dokumendisetujui.visibility = View.GONE
        }

    }
}
