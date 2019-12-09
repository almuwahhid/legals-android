package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpembayaran.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.app.daftarlayanan.adapter.DaftarLayananAdapter
import id.go.kemlu.legalisasidokumen.data.models.PembayaranToVerifModel
import kotlinx.android.synthetic.main.adapter_verifikasi.view.*
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.StaticData.*

class AdapterDaftarPembayaran (context: Context, list: MutableList<PembayaranToVerifModel>, private val onDaftarLayananAdapter: OnPembayaranClick) : RecyclerView.Adapter<AdapterDaftarPembayaran.Holder>() {


    var list: MutableList<PembayaranToVerifModel>
    var context: Context


    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_verifikasi, parent, false)
        return Holder(
            layoutView,
            viewType
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position, list.get(position), onDaftarLayananAdapter)
    }

    interface OnPembayaranClick{
        fun onClick(model: PembayaranToVerifModel)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: PembayaranToVerifModel, onVerifikasiAdapter: OnPembayaranClick): Unit = with(itemView) {
            card_request.setOnClickListener({
                onVerifikasiAdapter.onClick(data)
            })
            tv_country.text = data.country_name
            tv_namapemohon.text = data.request_name
            tv_status.text = data.status_detail
            tv_date.text = data.dtm_req

            when(data.status_id){
                STATUS_MENUGGGU_VERIFIKASI -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                    tv_status.setTextColor(context.resources.getColor(R.color.colorPrimary))
                }
                STATUS_MENUNGGU_PEMBAYARAN -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.purple_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.purple_400))
                }
                STATUS_BUKTIBAYAR_TIDAKVALID -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.orange_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.orange_400))
                }
                STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.yellow_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.yellow_400))
                }
                STATUS_BUKTIBAYAR_VALID -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.green_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.green_400))
                }
                STATUS_PERMOHONAN_DITOLAK -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.red_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.red_400))
                }
                else -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                    tv_status.setTextColor(context.resources.getColor(R.color.colorPrimary))
                }
            }
        }
    }
}