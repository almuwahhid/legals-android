package id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogAddPejabat.model.AddPejabatUiModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogPicker.DialogPicker
import kotlinx.android.synthetic.main.dialog_add_pejabat.*
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic
import java.util.ArrayList

class DialogAddPejabat(context: Context, type: String, onDialogAddPejabat: OnDialogAddPejabat) : DialogBuilder(context, R.layout.dialog_add_pejabat), DialogAddPejabatView.View {

    var onDialogAddPejabat: OnDialogAddPejabat
    var presenter: DialogAddPejabatPresenter
    var model = AddPejabatUiModel()
    internal var forms : MutableList<Int>

    init {
        this.onDialogAddPejabat = onDialogAddPejabat
        presenter = DialogAddPejabatPresenter(context, this)
        forms = ArrayList()
        setFormsToValidate()

        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)
            tv_title.setText("Tambah "+type)

            edt_lembaga.setOnClickListener({
                presenter.requestLembaga()
            })

            edt_jabatan.setOnClickListener({
                presenter.requestJabatan()
            })

            btn_kirim.setOnClickListener({
                if (GmsStatic.isFormValid(context, window.decorView, forms)) {
                    model.name = edt_namalengkap.text.toString()
                    model.nip = edt_nip.text.toString()
                    model.level_id = if(type.equals("Pejabat")) "1" else "2"
                    presenter.addPejabat(model)
                }
            })
        }
        show()
    }
    private fun setFormsToValidate() {
        forms.add(R.id.edt_jabatan)
        forms.add(R.id.edt_lembaga)
        forms.add(R.id.edt_namalengkap)
        forms.add(R.id.edt_nip)
    }

    override fun onRequestJabatan(list: MutableList<PickerModel>) {
        DialogPicker(context, "Pilih Lembaga", false, list, object : DialogPicker.OnPickerListener{
            override fun onItemClick(pickerModel: PickerModel) {
                model.position_name = pickerModel.name
//                model.official_institution_name = pickerModel.name
                dialog.edt_jabatan.setText(pickerModel.name)
            }
        })
    }

    override fun onRequestLembaga(list: MutableList<PickerModel>) {
        DialogPicker(context, "Pilih Lembaga", false, list, object : DialogPicker.OnPickerListener{
            override fun onItemClick(pickerModel: PickerModel) {
                model.official_institution_id = pickerModel.id
                model.official_institution_name = pickerModel.name
                dialog.edt_lembaga.setText(pickerModel.name)
            }
        })
    }

    override fun onAddPejabat(isSuccess: Boolean, message: String) {
        GmsStatic.ToastShort(context, message)
        if(isSuccess){
            dismiss()
            onDialogAddPejabat.onSuccessAddPejabat()
        }
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }

    interface OnDialogAddPejabat{
        fun onSuccessAddPejabat()
    }
}
