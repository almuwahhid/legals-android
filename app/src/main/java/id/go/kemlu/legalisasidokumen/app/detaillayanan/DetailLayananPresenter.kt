package id.go.kemlu.legalisasidokumen.app.detaillayanan

import android.content.Context
import lib.gmsframeworkx.base.BasePresenter

class DetailLayananPresenter(context: Context, view : DetailLayananView.View) : BasePresenter(context) {

    var view : DetailLayananView.View

    init {
        this.view = view

    }
}