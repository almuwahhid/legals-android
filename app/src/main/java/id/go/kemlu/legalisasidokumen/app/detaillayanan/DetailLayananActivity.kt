package id.go.kemlu.legalisasidokumen.app.detaillayanan

import android.Manifest
import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.StrictMode
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiPermissionActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import kotlinx.android.synthetic.main.activity_kwitansi.*
import kotlinx.android.synthetic.main.layout_detail_pembayaran.*
import kotlinx.android.synthetic.main.layout_detail_permohonan.*
import kotlinx.android.synthetic.main.layout_kualitas_layanan.*
import kotlinx.android.synthetic.main.layout_kwitansi.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface
import lib.gmsframeworkx.utils.GmsStatic

class DetailLayananActivity : LegalisasiPermissionActivity(), DetailLayananView.View {

    lateinit var presenter : DetailLayananPresenter
    lateinit var model : RequestModel
    val COMMENT_WEBVIEW_STYLE = "<html> <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'> <body  style=\"text-align:left;background-color: transparent;font-size:13px;color:#616161;font-family:'Roboto'\"> %s </body></Html>"
    val mime = "text/html"
    val encoding = "utf-8"
    var isKualitasLayananDone = false
    var stringKualitasLayanan = ""

    lateinit var menu : Menu

    protected var RequiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layanan)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

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
            StaticData.STATUS_MENUGGGU_VERIFIKASI -> {
                toolbar_title.setText("Permohonan Terkirim")
                initPermohonanTerkirim()
            }
            StaticData.STATUS_MENUNGGU_PEMBAYARAN -> {
                toolbar_title.setText("Bukti Pembayaran")
                initBuktiPembayaran()
            }
            StaticData.STATUS_BUKTIBAYAR_TIDAKVALID -> {

            }
            StaticData.STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {
                toolbar_title.setText("Verifikasi Pembayaran")
                initVerifikasiPembayaran()
            }
            StaticData.STATUS_BUKTIBAYAR_VALID -> {
                initBuktiBayarValid()
            }
            StaticData.STATUS_PERMOHONAN_DITOLAK -> {
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

    private fun initBuktiBayarValid(){
        lay_kualitaslayanan.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_left)
        lay_kwitansi.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_left)
        initBuktiBayarTitle()

        val onClickKualitas = View.OnClickListener {
            when(it.id){
                R.id.img_kualitas1->{
                    stringKualitasLayanan = "1"
                    img_kualitas1.setImageResource(R.drawable.ic_smile_3);
                    img_kualitas2.setImageResource(R.drawable.ic_smile_def_2);
                    img_kualitas3.setImageResource(R.drawable.ic_smile_def_1);
                }
                R.id.img_kualitas2->{
                    stringKualitasLayanan = "2"
                    img_kualitas1.setImageResource(R.drawable.ic_smile_def_3);
                    img_kualitas2.setImageResource(R.drawable.ic_smile_2);
                    img_kualitas3.setImageResource(R.drawable.ic_smile_def_1);
                }
                R.id.img_kualitas3->{
                    stringKualitasLayanan = "3"
                    img_kualitas1.setImageResource(R.drawable.ic_smile_def_3);
                    img_kualitas2.setImageResource(R.drawable.ic_smile_def_2);
                    img_kualitas3.setImageResource(R.drawable.ic_smile_1);
                }
            }
        }

        img_kualitas1.setOnClickListener(onClickKualitas)
        img_kualitas2.setOnClickListener(onClickKualitas)
        img_kualitas3.setOnClickListener(onClickKualitas)

        tv_kwitansi_nama.setText(": "+model.document_legal_by)
        tv_kwitansi_date.setText(""+model.request_date)
        tv_kwitansi_nopermohonan.setText(": "+model.group_no)
        tv_kwitansi_totalbiaya.setText(": Rp. "+model.total_price)
        tv_kwitansi_jumlahdokumen.setText(": "+model.doc_qty)
        tv_kwitansi_transferke.setText(": "+model.dtm_trans)
        tv_kwitansi_va.setText(": "+model.trans_no)

    }

    private fun initBuktiBayarTitle(){
        if(isKualitasLayananDone){
            toolbar_title.setText("Kwitansi")
        } else {
            toolbar_title.setText("Kualitas Layanan")
            lay_kualitaslayanan.visibility = View.VISIBLE
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(model.status_id == StaticData.STATUS_BUKTIBAYAR_VALID){
            menuInflater.inflate(R.menu.buktibayarvalid, menu)
            this.menu = menu
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_buktibayarvalid -> {
                if(!isKualitasLayananDone){
                    if(stringKualitasLayanan.equals("")){
                        GmsStatic.ToastShort(context, "Mohon berikan penilaian Anda terlebih dahulu untuk membantu meningkatkan layanan kami")
                    } else {
                        if(edt_kualitas.text.toString().equals("")){
                            GmsStatic.ToastShort(context, "Mohon tambahkan komentar Anda terlebih dahulu untuk membantu meningkatkan layanan kami")
                        } else {
                            presenter.requestKualitasLayanan()
                        }
                    }
                } else {
                    askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                        override fun permissionDenied() {

                        }

                        override fun permissionGranted() {
                            val bitmap = LegalisasiFunction.createBitmapFromLayout(lay_kwitansi)
                            LegalisasiFunction.shareImageUri(context, LegalisasiFunction.saveImageExternal(context, bitmap, ""+model.online_request_id))
                        }

                    })
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAfterSendingKualitasLayanan() {
        isKualitasLayananDone = true
        initBuktiBayarTitle()
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_download))
        lay_kualitaslayanan.hide()
        lay_kwitansi.show()

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        lay_kwitansi.setLayoutParams(
            RelativeLayout.LayoutParams(
                width,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }
}