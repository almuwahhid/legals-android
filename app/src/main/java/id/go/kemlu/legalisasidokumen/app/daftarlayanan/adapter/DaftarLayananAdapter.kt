package id.go.kemlu.legalisasidokumen.app.daftarlayanan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import kotlinx.android.synthetic.main.adapter_daftar_layanan.view.*
import id.go.kemlu.legalisasidokumen.data.StaticData

class DaftarLayananAdapter (context: Context, list: MutableList<RequestModel>, private val onDaftarLayananAdapter: DaftarLayananAdapter.OnDaftarLayananAdapter) : RecyclerView.Adapter<DaftarLayananAdapter.Holder>() {

    var list: MutableList<RequestModel>
    var context: Context


    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_daftar_layanan, parent, false)
        return DaftarLayananAdapter.Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DaftarLayananAdapter.Holder, position: Int) {
        holder.bind(position, list.get(position), onDaftarLayananAdapter)
    }

    interface OnDaftarLayananAdapter{
        fun onLayananClick(model: RequestModel)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: RequestModel, onDaftarLayananAdapter: OnDaftarLayananAdapter): Unit = with(itemView) {
            card_request.setOnClickListener({
                onDaftarLayananAdapter.onLayananClick(data)
            })
            tv_country.text = data.country_name
            tv_nomorpermohonan.text = data.group_no
            tv_status.text = data.status_detail
            tv_date.text = data.request_date

            when(data.status_id){
                StaticData.STATUS_MENUGGGU_VERIFIKASI -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                    tv_status.setTextColor(context.resources.getColor(R.color.colorPrimary))
                }
                StaticData.STATUS_MENUNGGU_PEMBAYARAN -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.purple_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.purple_400))
                }
                StaticData.STATUS_BUKTIBAYAR_TIDAKVALID -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.orange_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.orange_400))
                }
                StaticData.STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.yellow_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.yellow_400))
                }
                StaticData.STATUS_BUKTIBAYAR_VALID -> {
                    lay_card.setBackgroundColor(context.resources.getColor(R.color.green_400))
                    tv_status.setTextColor(context.resources.getColor(R.color.green_400))
                }
                StaticData.STATUS_PERMOHONAN_DITOLAK -> {
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


