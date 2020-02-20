package id.go.kemlu.legalisasidokumen.app.buatpermohonan

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gcacace.signaturepad.views.SignaturePad
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.dialogs.DialogImagePicker
import id.go.kemlu.legalisasidokumen.app.buatdokumen.TambahDokumenDialog
import id.go.kemlu.legalisasidokumen.app.buatdokumen.model.DokumenUiModel
import id.go.kemlu.legalisasidokumen.app.buatpermohonan.adapter.DokumenAttachmentAdapter
import id.go.kemlu.legalisasidokumen.app.buatpermohonan.model.BuatPermohonanUiModel
import id.go.kemlu.legalisasidokumen.data.Preferences
import id.go.kemlu.legalisasidokumen.data.models.CountryModel
import id.go.kemlu.legalisasidokumen.data.models.InstansiModel
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogPicker.DialogPicker
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiPermissionActivity
import kotlinx.android.synthetic.main.activity_buat_permohonan.*
import kotlinx.android.synthetic.main.layout_buatpermohonan.*
import kotlinx.android.synthetic.main.layout_buattandatangan.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface
import lib.gmsframeworkx.easyphotopicker.DefaultCallback
import lib.gmsframeworkx.easyphotopicker.EasyImage
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.GmsStatic
import java.io.File

class BuatPermohonanActivity : LegalisasiPermissionActivity(), BuatPermohonanView.View {

    lateinit var tambahDokumenDialog: TambahDokumenDialog
    lateinit var presenter: BuatPermohonanPresenter
    var listDokumenUiModel: MutableList<DokumenUiModel> = ArrayList()
    var permohonanUiModel = BuatPermohonanUiModel()
    lateinit var dokumenAttachmentAdapter: DokumenAttachmentAdapter

    lateinit var menu : Menu

    var isLastStep = false


    protected var RequiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_permohonan)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Buat Permohonan")


        presenter = BuatPermohonanPresenter(context, this)
        setFormsToValidate()

        val onCLick = View.OnClickListener {
            when(it.id){
                R.id.edt_dokumen -> {
                    initNewDokumen()
                }
                R.id.edt_negaratujuan -> {
                    presenter.requestCountry()
                }
                R.id.edt_instansi -> {
                    presenter.requestInstansi()
                }
            }
        }

        edt_dokumen.setOnClickListener(onCLick)
        edt_negaratujuan.setOnClickListener(onCLick)
        edt_instansi.setOnClickListener(onCLick)

        rv_dokumen.layoutManager = LinearLayoutManager(context)
        dokumenAttachmentAdapter = DokumenAttachmentAdapter(listDokumenUiModel, object : DokumenAttachmentAdapter.OnListener{
            override fun onDeleteDokumen(position : Int, dokumenUiModel: DokumenUiModel) {
                listDokumenUiModel.removeAt(position)
                dokumenAttachmentAdapter.notifyDataSetChanged()
            }

            override fun onClickDokumen(position : Int, dokumenUiModel: DokumenUiModel) {
                initNewDokumen()
                tambahDokumenDialog.editDokumen(position, dokumenUiModel)
            }

        })
        rv_dokumen.adapter = dokumenAttachmentAdapter

        checked_permohonan.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                initButtonSubmitStyle()
            }

        })

        signature_pad.setOnSignedListener(object: SignaturePad.OnSignedListener{
            override fun onStartSigning() {

            }

            override fun onClear() {

            }

            override fun onSigned() {
//                TODO : Signature
                if(signature_pad.signatureBitmap != null){
                    img_clear_sign.visibility = View.VISIBLE
                    permohonanUiModel.signed_file = GmsStatic.convertToBase64(signature_pad.signatureBitmap)
                    initButtonSubmitStyle()
                }
            }

        })

        img_clear_sign.setOnClickListener({
            signature_pad.clear()
            permohonanUiModel.signed_file = ""
            it.visibility = View.GONE
        })

        btn_submit.setOnClickListener({
            validate()
        })
    }

    private fun initButtonSubmitStyle(){
        if(isSignValidate(false)){
            btn_submit.setBackground(resources.getDrawable(R.drawable.button_main))
            btn_submit.setClickable(true)
        } else {
            btn_submit.setBackground(resources.getDrawable(R.drawable.button_disable))
            btn_submit.setClickable(false)
        }
    }

    override fun onSuccessSubmitPermohonan(message: String) {
        GmsStatic.ToastShort(context, message)
        GmsStatic.setSPBoolean(context, Preferences.LAYANAN_ON_REFRESH, true)
        finish()
    }

    private fun initNewDokumen(){
        if(listDokumenUiModel.size < 10){
            tambahDokumenDialog = TambahDokumenDialog(context, object : TambahDokumenDialog.DokumenListener{
                override fun onEdit(position: Int, dokumenUiModel: DokumenUiModel) {
                    listDokumenUiModel.set(position, dokumenUiModel)
                    dokumenAttachmentAdapter.notifyDataSetChanged()
                    tambahDokumenDialog.dismiss()
                }

                override fun onSubmit(dokumenUiModel: DokumenUiModel) {
                    listDokumenUiModel.add(dokumenUiModel)
                    dokumenAttachmentAdapter.notifyDataSetChanged()
                    tambahDokumenDialog.dismiss()
                }

                override fun onAddPhoto() {
                    askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                        override fun permissionDenied() {

                        }

                        override fun permissionGranted() {
                            DialogImagePicker(context, object : DialogImagePicker.OnDialogImagePicker{
                                override fun onCameraClick() {
                                    EasyImage.openCamera(this@BuatPermohonanActivity, 0)
                                }

                                override fun onFileManagerClick() {
                                    EasyImage.openGallery(this@BuatPermohonanActivity, 0)
                                }

                            })
                        }

                    })
                }
            })
        } else {
            GmsStatic.ToastShort(context, "Lampiran dokumen maksimal berjumlah 10")
        }
    }

    override fun onRequestInstansi(list: MutableList<InstansiModel>) {
        var datas : MutableList<PickerModel> = ArrayList<PickerModel>()
        for(inst in list){
            datas.add(PickerModel(""+inst.institution_id, inst.institution_name, inst.institution_code))
        }
        DialogPicker(context,
                     "Pilih Instansi",
                        false,
                        datas,
                        object : DialogPicker.OnPickerListener{
                            override fun onItemClick(pickerModel: PickerModel) {
                                Log.d("institusi", "tes "+pickerModel.id)
                                edt_instansi.setText(pickerModel.name)
                                permohonanUiModel.institution_id = pickerModel.id
                            }
                        })
    }

    override fun onRequestCountry(list: MutableList<CountryModel>) {
        var datas = ArrayList<PickerModel>()
        for(inst in list){
            datas.add(PickerModel(inst.country_id, inst.country_name, inst.country_code))
        }
        DialogPicker(context,
            "Pilih Negara",
            true,
            datas,
            object : DialogPicker.OnPickerListener{
                override fun onItemClick(pickerModel: PickerModel) {
                    edt_negaratujuan.setText(pickerModel.name)
                    permohonanUiModel.country_id = pickerModel.id
                }
            })
    }

    override fun onFailedRequestSomething(message: String) {
        GmsStatic.ToastShort(context, message)
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    private fun startCropActivity(uri: Uri) {
        CropImage.activity(uri)
//            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this@BuatPermohonanActivity)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                Log.d("ikiopo", "")
//                val uri = Uri.fromFile(imageFiles.get(0))
                val uri = result.uri
                tambahDokumenDialog.setPhoto(uri)
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
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.buatpermohonan, menu)
        this.menu = menu
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_buatpermohonan -> {
                validate()
                true
            }
            android.R.id.home -> {
                if(isLastStep){
                    toLastStep(isLastStep)
                } else {
                    AlertDialogBuilder(context,
                        "Apakah Anda yakin ingin kembali?",
                        "Ya",
                        "Tidak",
                        object : AlertDialogBuilder.OnAlertDialog{
                            override fun onPositiveButton(dialog: DialogInterface?) {
                                finish()
                            }

                            override fun onNegativeButton(dialog: DialogInterface?) {

                            }

                        })
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validate(){
        if(listDokumenUiModel.size == 0){
            GmsStatic.ToastShort(context, "Tambahkan minimal 1 dokumen terlebih dahulu")
        } else {
          if(GmsStatic.isFormValid(context, window.decorView, forms)){
              if(!isLastStep){
                  toLastStep(isLastStep)
              } else {
                  if(isSignValidate(true)){
                      permohonanUiModel.document = listDokumenUiModel
                      permohonanUiModel.institution_id = "8";
                      presenter.submitPermohonan(permohonanUiModel)
                  }
              }
          }
        }
    }

    private fun isSignValidate(needToast: Boolean): Boolean{
        if(permohonanUiModel.signed_file.equals("")){
            if(needToast)
                GmsStatic.ToastShort(context, "Tambahkan tanda tangan terlebih dahulu")
            return false
        } else {
            if(checked_permohonan.isChecked){
                return true
            } else {
                if(needToast)
                    GmsStatic.ToastShort(context, "Anda belum menyetujui permohonan")
                return false
            }
        }
    }

    internal var forms: ArrayList<Int> = java.util.ArrayList()
    private fun setFormsToValidate() {
//        forms.add(R.id.edt_instansi)
        forms.add(R.id.edt_negaratujuan)
    }

    private fun toLastStep(isLast: Boolean){
        if(isLast){
            lay_wrapper.setInOutAnimation(R.anim.pull_in_left, R.anim.push_out_right)
            lay_wrapper.displaying(lay_buatpermohonan)
            lay_wrapper.hide(lay_buattandatangan)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_chevron_right_black_24dp))
            menu.getItem(0).setVisible(true)
        } else {
            lay_wrapper.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_left)
            lay_wrapper.displaying(lay_buattandatangan)
            lay_wrapper.hide(lay_buatpermohonan)
//            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_check_black_24dp))
            menu.getItem(0).setVisible(false)
        }
        isLastStep = !isLastStep
    }
}
