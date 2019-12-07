package id.go.kemlu.legalisasidokumen.dialogs.DialogUploadBuktiBayar

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.DokumenModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import kotlinx.android.synthetic.main.activity_dialog_upload_bukti_bayar.*
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class DialogUploadBuktiBayar(context: Context, model: RequestModel, onDialogUploadBuktiBayar: OnDialogUploadBuktiBayar) : DialogBuilder(context, R.layout.activity_dialog_upload_bukti_bayar), DialogUploadBuktiView.View {

    var model: RequestModel
    var onDialogUploadBuktiBayar: OnDialogUploadBuktiBayar
    var stringBase64 = ""
    var presenter : DialogUploadBuktiPresenter

    init{
        this.model = model
        this.onDialogUploadBuktiBayar = onDialogUploadBuktiBayar

        presenter = DialogUploadBuktiPresenter(context, this)

        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)

            lay_upload.setOnClickListener({
                onDialogUploadBuktiBayar.onpickBukti()
            })

            btn_bukti_uploadbukti.setOnClickListener({
                if(stringBase64.equals("")){
                    GmsStatic.ToastShort(context, "Tambahkan bukti pembayaran terlebih dahulu")
                } else {
                    presenter.uploadBukti(stringBase64, ""+model.online_request_id)
                }
            })

            tv_title.setText("Nomor Dokumen : "+model.group_no)
        }

        show()
    }

    public fun setImage(bitmap: Bitmap) {
        dialog.img_buktibayar.setImageBitmap(bitmap)
        stringBase64 = GmsStatic.convertToBase64(bitmap)
    }

    interface OnDialogUploadBuktiBayar{
        fun onpickBukti()
        fun onAfterUploadBukti()
    }

    override fun onUploadBukti(isSuccess: Boolean, message: String) {
        GmsStatic.ToastShort(context, message)
        if(isSuccess){
            dismiss()
            onDialogUploadBuktiBayar.onAfterUploadBukti()
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
}
