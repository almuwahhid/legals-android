package id.go.kemlu.legalisasidokumen.app.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.DokumenModel
import kotlinx.android.synthetic.main.adapter_dokumen_saya.view.*
import id.go.kemlu.legalisasidokumen.data.StaticData

class DokumenSayaAdapter (context: Context, list: MutableList<DokumenModel>, private val onDokumenSayaAdapter: OnDokumenSayaAdapter) : RecyclerView.Adapter<DokumenSayaAdapter.Holder>() {

    var onDokumenSayaAdapters: OnDokumenSayaAdapter
    var context: Context
    var list: MutableList<DokumenModel>

    init {
        this.context = context
        this.list = list
        this.onDokumenSayaAdapters = onDokumenSayaAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_dokumen_saya, parent, false)
        return Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindTo(list.get(position), onDokumenSayaAdapters)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(data: DokumenModel, onDokumenSayaAdapters: OnDokumenSayaAdapter): Unit = with(itemView) {
            lay_adapter.setOnClickListener({
                onDokumenSayaAdapters.onDokumenClick(data)
            })

            tv_dokumen.setText(data.document_name)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                when(data.status_id){
                    StaticData.STATUS_MENUGGGU_VERIFIKASI -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary))
                    }
                    StaticData.STATUS_MENUNGGU_PEMBAYARAN -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.purple_400))
                    }
                    StaticData.STATUS_BUKTIBAYAR_TIDAKVALID -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.orange_400))
                    }
                    StaticData.STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yellow_400))
                    }
                    StaticData.STATUS_BUKTIBAYAR_VALID -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green_400))
                    }
                    StaticData.STATUS_PERMOHONAN_DITOLAK -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red_400))
                    }
                    else -> {
                        lay_status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary))
                    }
                }
            }
        }
    }

    interface OnDokumenSayaAdapter{
        fun onDokumenClick(model: DokumenModel)
    }

}
