package id.go.kemlu.legalisasidokumen.dialogs.DialogVerifikasiPermohonan

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import kotlinx.android.synthetic.main.dialog_verifikasi_permohonan.*
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class DialogVerifikasiPermohonan(context: Context, requestModel: RequestModel, onVerifikasi: OnVerifikasi) : DialogBuilder(context, R.layout.dialog_verifikasi_permohonan), DialogVerifikasiPermohonanView.View {

    var requestModel: RequestModel
    var presenter : DialogVerifikasiPermohonanPresenter
    var onVerifikasi: OnVerifikasi

    init {
        this.requestModel = requestModel
        this.onVerifikasi = onVerifikasi
        presenter = DialogVerifikasiPermohonanPresenter(context, this)

        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)

            tv_title.setText("Nomor Dokumen : "+requestModel.group_no)
            val onClick = View.OnClickListener {
                if(edt_catatan.text.toString().equals("")){
                    GmsStatic.ToastShort(context, "Tambahkan catatan terlebih dahulu")
                } else {
                    var status = ""
                    var alert = ""
                    when(it.id){
                        R.id.btn_setuju->{
                            status = "ACCEPT"
                            alert = "Apakah Anda yakin ingin menyetujui permohonan ini? "
                        }
                        R.id.btn_tolak->{
                            status = "REJECT"
                            alert = "Apakah Anda yakin ingin menolak permohonan ini? "
                        }
                    }
                    AlertDialogBuilder(context,
                        alert,
                        "Ya",
                        "Tidak",
                        object : AlertDialogBuilder.OnAlertDialog{
                            override fun onPositiveButton(dialog: DialogInterface?) {
                                presenter.verifikasiPermohonan(requestModel, status, edt_catatan.text.toString())
                            }

                            override fun onNegativeButton(dialog: DialogInterface?) {

                            }

                        })


                }
            }

            btn_setuju.setOnClickListener(onClick)
            btn_tolak.setOnClickListener(onClick)
        }
        show()
    }

    override fun onVerifikasiPermohonan(isSuccess: Boolean, message: String) {
        GmsStatic.ToastShort(context, message)
        if(isSuccess){
            dismiss()
            onVerifikasi.onSuccessVerifikasi()
        }
    }

    interface OnVerifikasi{
        fun onSuccessVerifikasi()
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
