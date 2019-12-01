package id.go.kemlu.legalisasidokumen.app.daftarlayanan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.DaftarRequest
import kotlinx.android.synthetic.main.adapter_daftar_layanan.view.*

class DaftarLayananAdapter (context: Context, list: MutableList<DaftarRequest>, private val onDaftarLayananAdapter: DaftarLayananAdapter.OnDaftarLayananAdapter) : RecyclerView.Adapter<DaftarLayananAdapter.Holder>() {

    lateinit var list: MutableList<DaftarRequest>
    lateinit var context: Context


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
        fun onLayananClick(model: DaftarRequest)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: DaftarRequest, onDaftarLayananAdapter: OnDaftarLayananAdapter): Unit = with(itemView) {
            card_request.setOnClickListener({
                onDaftarLayananAdapter.onLayananClick(data)
            })
            tv_country.text = data.country_name
            tv_nomorpermohonan.text = data.group_no
            tv_status.text = data.status_detail
            tv_date.text = data.request_date
        }
    }
}


