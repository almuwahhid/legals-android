package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.BitmapTransform
import kotlinx.android.synthetic.main.activity_detail_pembayaran_to_verif.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class DetailPembayaranToVerifActivity : LegalisasiActivity(), DetailPembayaranToVerifView.View {

    lateinit var model : PembayaranToVerifModel
    lateinit var presenter : DetailPembayaranToVerifPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pembayaran_to_verif)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as PembayaranToVerifModel
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
        tv_tglpembayaran.setText(model.request_date)
        tv_tglverifikasi.setText(model.dtm_verification)
        tv_jumlahdokumen.setText(""+model.doc_qty)
        tv_jumlahdokumen.setText("-")
        tv_va.setText(model.rekening)
        Picasso.with(context)
            .load(if(model.image_path.equals(""))"google.com" else model.image_path)
            .placeholder(R.drawable.bg_grey)
            .transform(BitmapTransform())
            .into(img_buktitransfer)

        val onClick = View.OnClickListener {
            if(edt_catatan.text.toString().equals("")){
                GmsStatic.ToastShort(context, "Tambahkan catatan terlebih dahulu")
            } else {
                var status_id = ""
                when(it.id){
                    R.id.btn_setuju->{
                        status_id = "1"
                    }
                    R.id.btn_tolak->{
                        status_id = "3"
                    }
                }
                presenter.updatePembayaran(""+model.ol_id, status_id, edt_catatan.text.toString())

            }
        }

        btn_setuju.setOnClickListener(onClick)

        btn_tolak.setOnClickListener(onClick)
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    override fun onUpdatePembayaran(isSuccess: Boolean, message: String) {

    }
}
