package id.go.kemlu.legalisasidokumen.app.buatdokumen

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.adapters.PhotoAdapter
import id.go.kemlu.legalisasidokumen.app.buatdokumen.model.DokumenUiModel
import id.go.kemlu.legalisasidokumen.data.models.JenisDokumenModel
import id.go.kemlu.legalisasidokumen.data.models.PhotoModel
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogPicker.DialogPicker
import kotlinx.android.synthetic.main.dialog_tambah_dokumen.*
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class TambahDokumenDialog(context: Context?, onDokumenListener: DokumenListener ) : DialogBuilder(context, R.layout.dialog_tambah_dokumen), TambahDokumenView.View {

    lateinit var photoAdapter: PhotoAdapter
    lateinit var presenter: TambahDokumenPresenter
    var onDokumenListener: DokumenListener
    var list_photo = ArrayList<PhotoModel>()
    var dokumenUiModel = DokumenUiModel()
    var isEdited = false
    var position_edited = 0
    var forms: MutableList<Int>

    init {
        this.onDokumenListener = onDokumenListener
        forms = ArrayList()
        setAnimation(R.style.DialogBottomAnimation)
        setFullScreen(dialog.lay_dialog)
        setGravity(Gravity.BOTTOM)

        initContent(onDokumenListener)

        show()
    }

    override fun onRequestLembaga(list: MutableList<PickerModel>) {
        DialogPicker(context, "Pilih Lembaga", false, list, object : DialogPicker.OnPickerListener{
            override fun onItemClick(pickerModel: PickerModel) {
                dokumenUiModel.official_sinstitution_id = pickerModel.id
                dokumenUiModel.certifier_institution = pickerModel.name
                dialog.edt_instansi.setText(pickerModel.name)
            }
        })
    }

    private fun initContent(onDokumenListener: DokumenListener){
        setFormsToValidate()

        presenter = TambahDokumenPresenter(context, this)

        list_photo.add(PhotoModel())
        photoAdapter = PhotoAdapter(
            getContext(),
            list_photo,
            true,
            object : PhotoAdapter.OnPhotoAdapter {
                override fun onPhotoClick(view: View, photoModel: PhotoModel) {

                }


                override fun onDeleteClick(position: Int) {
                    list_photo.removeAt(position)
                    photoAdapter.notifyDataSetChanged()
                }

                override fun onAddPhoto() {
                    onDokumenListener.onAddPhoto()
                }

            })

        with(dialog){
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = photoAdapter

            val onCLick = View.OnClickListener {
                when(it.id){
                    R.id.img_ok -> {
                        validate()
                    }
                    R.id.edt_jenisdokumen -> {
                        presenter.requestJenisDokumen()
                    }
                    R.id.edt_instansi -> {
                        presenter.requestLembaga()
                    }
                    R.id.img_back -> {
                        AlertDialogBuilder(context,
                            "Apakah Anda yakin ingin kembali?",
                            "Ya",
                            "Tidak",
                            object : AlertDialogBuilder.OnAlertDialog{
                                override fun onPositiveButton(dialog: DialogInterface?) {
                                    dismiss()
                                }

                                override fun onNegativeButton(dialog: DialogInterface?) {

                                }

                            })

                    }
                }
            }
            img_ok.setOnClickListener(onCLick)
            edt_jenisdokumen.setOnClickListener(onCLick)
            edt_jenisdokumen.setOnClickListener(onCLick)
            edt_instansi.setOnClickListener(onCLick)
        }

    }

    private fun validate(){
        if(list_photo.size == 1){
            GmsStatic.ToastShort(context, "Tambahkan minimal 1 file terlebih dahulu")
        } else {
            var list_uri = ArrayList<Uri>()
            var list_base64 = ArrayList<String>()
            for (x in list_photo){
                if(!x.base64.equals("")){
                    list_base64.add(x.base64)
                    list_uri.add(x.uri)
                }
            }
            dokumenUiModel.setForms(dialog.edt_namapengesah.text.toString(),
                dialog.edt_nomorpengesahan.text.toString(),
                dialog.edt_instansi.text.toString())
            dokumenUiModel.setFiles(list_base64, list_uri)
            if(isEdited){
                onDokumenListener.onEdit(position_edited, dokumenUiModel)
            } else {
                onDokumenListener.onSubmit(dokumenUiModel)
            }
        }
    }

    fun editDokumen(position: Int, dokumenUiModel: DokumenUiModel){
        isEdited = true
        position_edited = position
        this.dokumenUiModel = dokumenUiModel
        for (x in dokumenUiModel.file_uri){
            val bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), x)
            list_photo.add(PhotoModel("", GmsStatic.convertToBase64(bitmap), x))
        }
        photoAdapter.notifyDataSetChanged()
        dialog.edt_jenisdokumen.setText(dokumenUiModel.document_name)
        dialog.edt_namapengesah.setText(dokumenUiModel.certifier_name)
        dialog.edt_nomorpengesahan.setText(dokumenUiModel.certifier_number)
        dialog.edt_instansi.setText(dokumenUiModel.certifier_institution)
    }


    interface DokumenListener {
        fun onAddPhoto()
        fun onSubmit(dokumenUiModel: DokumenUiModel)
        fun onEdit(position: Int, dokumenUiModel: DokumenUiModel)
    }

    public fun setPhoto(uri: Uri){
        val bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
        list_photo.add(PhotoModel("", GmsStatic.convertToBase64(bitmap), uri))
        photoAdapter.notifyDataSetChanged()
    }

    override fun onRequestJenisDokumen(list: MutableList<JenisDokumenModel>) {
        var datas = ArrayList<PickerModel>()
        for(inst in list){
            datas.add(PickerModel(inst.document_type_id, inst.document_name, inst.institution_id))
        }
        DialogPicker(context,
            "Pilih Jenis Dokumen",
            false,
            datas,
            object : DialogPicker.OnPickerListener{
                override fun onItemClick(pickerModel: PickerModel) {
                    dialog.edt_jenisdokumen.setText(pickerModel.name)
                    dokumenUiModel.document_type_id = pickerModel.id
                    dokumenUiModel.document_name = pickerModel.name
                }
            })
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    override fun onFailedRequestSomething(message: String) {
        GmsStatic.ToastShort(context, message)
    }

    private fun setFormsToValidate() {
        forms.add(R.id.edt_jenisdokumen)
        forms.add(R.id.edt_nomorpengesahan)
        forms.add(R.id.edt_namapengesah)
        forms.add(R.id.edt_instansi)
    }
}
