package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpembayaran

import android.content.Context
import lib.gmsframeworkx.base.BasePresenter

class DetailPembayaranToVerifPresenter(context: Context, view: DetailPembayaranToVerifView.View) : BasePresenter(context), DetailPembayaranToVerifView.Presenter {

    var view: DetailPembayaranToVerifView.View

    init{
        this.view = view
    }
}