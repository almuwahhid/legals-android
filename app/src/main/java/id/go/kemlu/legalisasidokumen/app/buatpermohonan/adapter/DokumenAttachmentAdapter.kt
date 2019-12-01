package id.go.kemlu.legalisasidokumen.app.buatpermohonan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.buatdokumen.model.DokumenUiModel
import kotlinx.android.synthetic.main.adapter_dokumen_attachment.view.*

class DokumenAttachmentAdapter (list: MutableList<DokumenUiModel>, private val onListener: OnListener) : RecyclerView.Adapter<DokumenAttachmentAdapter.Holder>() {

    var list: MutableList<DokumenUiModel>

    init {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DokumenAttachmentAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_dokumen_attachment, parent, false)
        return Holder(layoutView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DokumenAttachmentAdapter.Holder, position: Int) {
        holder.bindTo(position, list.get(position), onListener)
    }

    interface OnListener{
        fun onDeleteDokumen(position: Int, dokumenUiModel: DokumenUiModel)
        fun onClickDokumen(position: Int, dokumenUiModel: DokumenUiModel)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(position: Int, data: DokumenUiModel, onListener: OnListener): Unit = with(itemView) {
            setOnClickListener({
                onListener.onClickDokumen(position, data)
            })
            tv_title.setText(data.document_name)
            img_delete.setOnClickListener({
                onListener.onDeleteDokumen(position, data)
            })
        }
    }

}