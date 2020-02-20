package id.go.kemlu.legalisasidokumen.app.detaillayanan

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.theartofdev.edmodo.cropper.CropImage
import id.go.kemlu.legalisasidokumen.BuildConfig
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.adapters.DokumenSayaAdapter
import id.go.kemlu.legalisasidokumen.app.home.HomeActivity
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen.DetailDokumenToVerifActivity
import id.go.kemlu.legalisasidokumen.data.Preferences
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.DokumenModel
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogImagePicker
import id.go.kemlu.legalisasidokumen.dialogs.DialogUploadBuktiBayar.DialogUploadBuktiBayar
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiPermissionActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import kotlinx.android.synthetic.main.layout_buktipembayaran.*
import kotlinx.android.synthetic.main.layout_detail_permohonan.*
import kotlinx.android.synthetic.main.layout_kualitas_layanan.*
import kotlinx.android.synthetic.main.layout_kwitansi.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface
import lib.gmsframeworkx.easyphotopicker.DefaultCallback
import lib.gmsframeworkx.easyphotopicker.EasyImage
import lib.gmsframeworkx.utils.GmsStatic
import java.io.File

class DetailLayananActivity : LegalisasiPermissionActivity(), DetailLayananView.View {

    lateinit var presenter : DetailLayananPresenter
    lateinit var model : RequestModel
    val COMMENT_WEBVIEW_STYLE = "<html> <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'> <body  style=\"text-align:left;background-color: transparent;font-size:13px;color:#616161;font-family:'Roboto'\"> %s </body></Html>"
    val mime = "text/html"
    val encoding = "utf-8"
    var isKualitasLayananDone = false
    var stringKualitasLayanan = ""
    lateinit var clipboard: ClipboardManager

    var needStart = false

    lateinit var menu : Menu
    lateinit var dialoguploadbukti : DialogUploadBuktiBayar

    protected var RequiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    protected var RequiredPermissions2 = arrayOf(
        Manifest.permission.CAMERA,
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
            initComponent()
        } else {
            if(intent.hasExtra("raw_data")){
                needStart = true
                val notif = intent.getSerializableExtra("data") as NotifikasiModel
                presenter.requestDetailNotifikasi(""+notif.strNotifGroupNo, ""+notif.intNotifid)
            } else {
                finish()
            }
        }

    }

    private fun initComponent(){
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
                toolbar_title.setText("Bukti Pembayaran")
                initBuktiPembayaran()
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

    override fun onRequestDetailNotifikasi(requestModel: RequestModel) {
        model = requestModel
        initComponent()
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
                    stringKualitasLayanan = "3"
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
                    stringKualitasLayanan = "1"
                    img_kualitas1.setImageResource(R.drawable.ic_smile_def_3);
                    img_kualitas2.setImageResource(R.drawable.ic_smile_def_2);
                    img_kualitas3.setImageResource(R.drawable.ic_smile_1);
                }
            }
        }

        img_kualitas1.setOnClickListener(onClickKualitas)
        img_kualitas2.setOnClickListener(onClickKualitas)
        img_kualitas3.setOnClickListener(onClickKualitas)

        tv_kwitansi_nama.setText(": "+model.request_name)
        tv_kwitansi_date.setText(""+model.request_date)
        tv_kwitansi_nopermohonan.setText(": "+model.group_no)
        tv_kwitansi_totalbiaya.setText(": Rp. "+model.total_price)
        tv_kwitansi_jumlahdokumen.setText(": "+model.document.size)
        tv_kwitansi_transferke.setText(": "+model.bank)
        tv_kwitansi_va.setText(": "+model.rekening)

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
        if(model.detail_notification != null){
            webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, model.detail_notification.strNotifDesc), mime, encoding);
        }

    }

    private fun initBuktiPembayaran(){
        layout_buktipembayaran.visibility = View.VISIBLE
        clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        tv_bukti_nopermohonan.setText(""+model.group_no)
        tv_bukti_tglverif.setText(""+model.open_date)
        tv_bukti_jumlahdokumen.setText(""+model.document.size)
        tv_bukti_totalbayar.setText(""+model.total_price)
        tv_bukti_norek.setText(""+model.rekening)
        tv_rekeningpembayaran.setText(""+model.bank)

        tv_mandiri_mobile.setOnClickListener({
            val launchIntent = getPackageManager().getLaunchIntentForPackage("com.bankmandiri.mandirionline");
            if (launchIntent != null) {
               startActivity(launchIntent)
            } else {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.bankmandiri.mandirionline")
                        )
                    )
                } catch (exception: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bankmandiri.mandirionline")))
                }

            }
        })

        tv_bukti_salin.setOnClickListener({
            val clip = ClipData.newPlainText(BuildConfig.application_name, model.trans_no)
            clipboard.setPrimaryClip(clip)
            GmsStatic.ToastShort(context, "Nomot VA telah disalin ke clipboard")
        })

        val adapter = DokumenSayaAdapter(context, model.document, object: DokumenSayaAdapter.OnDokumenSayaAdapter{
            override fun onDokumenClick(doc: DokumenModel) {
                startActivity(Intent(context, DetailDokumenToVerifActivity::class.java).putExtra("data", doc).putExtra("parent", model))
            }
        })
        rv_bukti.layoutManager = LinearLayoutManager(context)
        rv_bukti.adapter = adapter

        if(model.document.size==0){
            tv_dokumendisetujui.visibility = View.GONE
        }

        btn_bukti_uploadbukti.setOnClickListener({
            dialoguploadbukti = DialogUploadBuktiBayar(context, model, object: DialogUploadBuktiBayar.OnDialogUploadBuktiBayar{
                override fun onpickBukti() {
                    askCompactPermissions(RequiredPermissions2, object : PermissionResultInterface {
                        override fun permissionDenied() {

                        }

                        override fun permissionGranted() {
                            DialogImagePicker(context, object : DialogImagePicker.OnDialogImagePicker{
                                override fun onCameraClick() {
                                    EasyImage.openCamera(this@DetailLayananActivity, 0)
                                }

                                override fun onFileManagerClick() {
                                    EasyImage.openGallery(this@DetailLayananActivity, 0)
                                }

                            })
                        }
                    })
                }

                override fun onAfterUploadBukti() {
                    GmsStatic.setSPBoolean(context, Preferences.LAYANAN_ON_REFRESH, true)
                    finish()
                }

            })
        })
    }

    private fun initVerifikasiPembayaran(){
        layout_detail_permohonan.visibility = View.VISIBLE
        tv_country.text = model.country_name
        tv_nomorpermohonan.text = model.group_no

        webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, narasiPermohonanTerkirim()), mime, encoding);
        if(model.detail_notification != null){
            webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, model.detail_notification.strNotifDesc), mime, encoding);
        }
    }

    private fun initPermohonanDitolak(){
        layout_detail_permohonan.visibility = View.VISIBLE
        tv_country.text = model.country_name
        tv_nomorpermohonan.text = model.group_no
        webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, narasiPermohonanDitolak()), mime, encoding);
        if(model.detail_notification != null){
            webview.loadData(String.format(COMMENT_WEBVIEW_STYLE, model.detail_notification.strNotifDesc), mime, encoding);
        }
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
            android.R.id.home->{
                if(needStart){
                    startActivity(Intent(context, HomeActivity::class.java))
                } else {
                    finish()
                }
                true
            }
            R.id.action_buktibayarvalid -> {
                if(!isKualitasLayananDone){
                    if(stringKualitasLayanan.equals("")){
                        GmsStatic.ToastShort(context, "Mohon berikan penilaian Anda terlebih dahulu untuk membantu meningkatkan layanan kami")
                    } else {
                        if(edt_kualitas.text.toString().equals("")){
                            GmsStatic.ToastShort(context, "Mohon tambahkan komentar Anda terlebih dahulu untuk membantu meningkatkan layanan kami")
                        } else {
                            presenter.requestKualitasLayanan(""+model.online_request_id, stringKualitasLayanan, edt_kualitas.text.toString())
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

    override fun onAfterSendingKualitasLayanan(message: String) {
        GmsStatic.ToastShort(context, message)
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

    override fun onErrorSendData(message: String) {
        GmsStatic.ToastShort(context, message)
    }

    private fun startCropActivity(uri: Uri) {
        CropImage.activity(uri)
//            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this@DetailLayananActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                Log.d("ikiopo", "")
                val uri = result.uri
                val bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
                dialoguploadbukti.setImage(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error.toString()
                GmsStatic.ToastShort(context, "" + error)
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback(){
                override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {
                    startCropActivity(Uri.fromFile(imageFiles[0]))
                }
            })
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback(){
            override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {

            }
        })
    }
}