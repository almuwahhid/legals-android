package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.BitmapTransform
import kotlinx.android.synthetic.main.activity_detail_pembayaran_to_verif.*
import kotlinx.android.synthetic.main.toolbar_white.*

class DetailPembayaranToVerifActivity : LegalisasiActivity(), DetailPembayaranToVerifView.View {

    lateinit var model : RequestModel
    lateinit var presenter : DetailPembayaranToVerifPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pembayaran_to_verif)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as RequestModel
        } else {
            finish()
        }

        presenter = DetailPembayaranToVerifPresenter(context, this)
        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            toolbar_title.setText("Detail Pembayaran")
        }

        tv_status.setText(model.status_detail)
        tv_nopermohonan.setText(model.group_no)
        tv_namapemohon.setText(model.request_name)
        tv_tglverifikasi.setText(model.open_date)
        tv_tglverifikasi.setText(model.payment_request_date)
        tv_jumlahdokumen.setText(model.document.size)
        tv_va.setText(model.rekening)
        Picasso.with(context)
            .load(if(model.image_path.equals(""))"google.com" else model.image_path)
            .placeholder(R.drawable.bg_grey)
            .transform(BitmapTransform())
            .into(img_buktitransfer)

        btn_setuju.setOnClickListener({

        })

        btn_tolak.setOnClickListener({

        })
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {

    }

    override fun onLoading() {

    }
}
