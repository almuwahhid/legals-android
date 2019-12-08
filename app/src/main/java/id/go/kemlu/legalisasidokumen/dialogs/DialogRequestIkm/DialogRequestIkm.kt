package id.go.kemlu.legalisasidokumen.dialogs.DialogRequestIkm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat.IKMActivity
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.IkmModel
import kotlinx.android.synthetic.main.dialog_request_ikm.*
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class DialogRequestIkm(context: Context, onRequestIkm: OnRequestIkm) : DialogBuilder(context, R.layout.dialog_request_ikm), DialogRequestIkmView.View {

    var onRequestIkm: OnRequestIkm
    var date1 = ""
    var date2 = ""
    var presenter : DialogRequestIkmPresenter

    init {
        this.onRequestIkm = onRequestIkm
        this.presenter = DialogRequestIkmPresenter(context, this)

        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)

            edt_tglawal.setOnClickListener({
                onRequestIkm.onClickDate(StaticData.IKM_DATE_1)
            })

            edt_tglakhir.setOnClickListener({
                onRequestIkm.onClickDate(StaticData.IKM_DATE_2)
            })

            btn_kirim.setOnClickListener({
                if(date1.equals("")){

                } else if(date2.equals("")){

                } else {
                    presenter.requestIkmByDate(date1, date2)
                }
            })
        }
        show()
    }

    interface OnRequestIkm{
        fun onClickDate(key: Int)
        fun onIkmRequested(ikmModel: IkmModel, date1: String, date2: String)
    }

    public fun onDateClicked(tgl: String, key: Int){
        when(key){
            StaticData.IKM_DATE_1->{
                dialog.edt_tglawal.setText(tgl.split("-")[2]+" "+GmsStatic.monthName(tgl.split("-")[1].toInt()-1)+" "+tgl.split("-")[0])
                date1 = tgl
            }
            StaticData.IKM_DATE_2->{
                dialog.edt_tglakhir.setText(tgl.split("-")[2]+" "+GmsStatic.monthName(tgl.split("-")[1].toInt()-1)+" "+tgl.split("-")[0])
                date2 = tgl
            }
        }
        if(!date1.equals("") && !date2.equals("")){
            dialog.btn_kirim.setBackground(context.resources.getDrawable(R.drawable.button_main))
            dialog.btn_kirim.setClickable(true)
        }
    }

    override fun onRequestIkm(ikmModel: IkmModel) {
        dismiss()
        onRequestIkm.onIkmRequested(ikmModel, date1, date2)
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
}
