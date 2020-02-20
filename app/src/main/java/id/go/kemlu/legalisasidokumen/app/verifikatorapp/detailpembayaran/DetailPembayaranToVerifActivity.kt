package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.squareup.picasso.Picasso
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.PhotoPreview.PhotoPreviewActivity
import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.BitmapTransform
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
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
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        tv_status.setText(model.status_detail)
        tv_nopermohonan.setText(model.group_no)
        tv_namapemohon.setText(model.request_name)
        tv_tglpembayaran.setText(model.payment_request_date)
        tv_tglverifikasi.setText(model.dtm_verification)
        tv_jumlahdokumen.setText(""+model.doc_qty)
        tv_va.setText(model.rekening)
        Picasso.with(context)
            .load(if(model.payment_image.equals(""))"google.com" else model.payment_image)
            .placeholder(R.drawable.bg_grey)
            .transform(BitmapTransform())
            .into(img_buktitransfer)

        img_buktitransfer.setOnClickListener({
            val data = Bundle()
            if (!model.payment_image.equals("")) {
                data.putString("image", model.payment_image)
                data.putString("type", "image")
            }


            if (!LegalisasiFunction.isPreLolipop()) {
                //                    Pair<View, String> pair1 = Pair.create(view, view.getTransitionName());
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@DetailPembayaranToVerifActivity,
                    img_buktitransfer,
                    ViewCompat.getTransitionName(img_buktitransfer)
                )
                startActivity(
                    Intent(context, PhotoPreviewActivity::class.java).putExtras(data),
                    options.toBundle()
                )
            } else {
                startActivity(Intent(context, PhotoPreviewActivity::class.java).putExtras(data))
            }
        })

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
        GmsStatic.ToastShort(context, message)
        if(isSuccess){
            finish()
        }
    }
}
