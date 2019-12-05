package id.go.kemlu.legalisasidokumen.app.detaillayanan

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiPermissionActivity
import kotlinx.android.synthetic.main.layout_detail_pembayaran.*
import kotlinx.android.synthetic.main.layout_detail_permohonan.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic

class DetailLayananActivity : LegalisasiPermissionActivity(), DetailLayananView.View {

    lateinit var presenter : DetailLayananPresenter
    lateinit var model : RequestModel
    val COMMENT_WEBVIEW_STYLE = "<html> <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'> <body  style=\"text-align:left;background-color: transparent;font-size:13px;color:#616161;font-family:'Roboto'\"> %s </body></Html>"
    val mime = "text/html"
    val encoding = "utf-8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layanan)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);


        presenter = DetailLayananPresenter(context, this)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as RequestModel
        } else {
            finish()
        }

        when(model.status_id){
            110 -> {
                toolbar_title.setText("Permohonan Terkirim")
                initPermohonanTerkirim()
            }
            120 -> {
                toolbar_title.setText("Bukti Pembayaran")
                initBuktiPembayaran()
            }
            121 -> {

            }
            150 -> {
                toolbar_title.setText("Verifikasi Pembayaran")
                initVerifikasiPembayaran()
            }
            160 -> {

            }
            190 -> {
                toolbar_title.setText("Permohonan Ditolak")
                initPermohonanDitolak()
            }
        }


    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(this, R.drawable.ic_logo)
    }

    private fun initPermohonanTerkirim(){
        layout_detail_permohonan.visibility = View.VISIBLE
        tv_country.text = model.country_name
        tv_nomorpermohonan.text = model.group_no
        webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, narasiPermohonanTerkirim()), mime, encoding);
    }

    private fun initBuktiPembayaran(){
        layout_detail_pembayaran.visibility = View.VISIBLE
    }

    private fun initVerifikasiPembayaran(){
        layout_detail_permohonan.visibility = View.VISIBLE
        tv_country.text = model.country_name
        tv_nomorpermohonan.text = model.group_no
    }

    private fun initPermohonanDitolak(){
        layout_detail_permohonan.visibility = View.VISIBLE
        tv_country.text = model.country_name
        tv_nomorpermohonan.text = model.group_no
        webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, narasiPermohonanDitolak()), mime, encoding);
    }

    private fun narasiPermohonanTerkirim(): String{
        return "Permohonan legalisasi dokumen dengan nomor <b>"+model.group_no+"</b> telah dikirim. Telah diterima pada tanggal "+model.request_date+".<br><br>Mohon tunggu hingga proses verifikasi selesai."
    }

    private fun narasiPermohonanDitolak(): String{
        return "Permohonan legalisasi dokumen dengan nomor <b>"+model.group_no+"</b> telah <b>ditolak</b>. <br><br>Alasan penolakan adalah dokumen tidak lengkap."
    }
}