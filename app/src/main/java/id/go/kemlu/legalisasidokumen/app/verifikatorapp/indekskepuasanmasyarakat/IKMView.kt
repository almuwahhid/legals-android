package id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat

import id.go.kemlu.legalisasidokumen.data.models.IKMCommentModel
import lib.gmsframeworkx.base.BaseView

interface IKMView {
    interface Presenter{
        fun requestComment(isReload: Boolean, date1: String, date2: String)
    }

    interface View: BaseView{
        fun onRequestIkm(list: MutableList<IKMCommentModel>, isReload: Boolean)
        fun onFailedRequestSomething(isFirst: Boolean, message : String)
        fun onFailedRequestMore(isFirst: Boolean, message : String)
        fun onLoadingMore()
        fun onHideLoading(isFirst: Boolean)
    }
}