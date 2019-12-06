package id.go.kemlu.legalisasidokumen.app.buatpermohonan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.buatdokumen.model.DokumenUiModel

class DokumenSayaAdapter (context: Context, list: MutableList<DokumenUiModel>, private val onDokumenSayaAdapter: DokumenSayaAdapter.OnDokumenSayaAdapter) : RecyclerView.Adapter<DokumenSayaAdapter.Holder>() {

    lateinit var onDokumenSayaAdapters: DokumenSayaAdapter.OnDokumenSayaAdapter
    lateinit var context: Context

    init {
        this.context = context
        this.onDokumenSayaAdapters = onDokumenSayaAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DokumenSayaAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_dokumen_saya, parent, false)
        return DokumenSayaAdapter.Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: DokumenSayaAdapter.Holder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnDokumenSayaAdapter{
        fun onDokumenClick(model: DokumenUiModel)
    }

}
