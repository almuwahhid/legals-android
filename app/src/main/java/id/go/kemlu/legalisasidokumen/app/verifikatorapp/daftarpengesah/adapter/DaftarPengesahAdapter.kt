package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PengesahModel
import kotlinx.android.synthetic.main.adapter_daftar_pengesah.view.*

class DaftarPengesahAdapter(context: Context, list: MutableList<PengesahModel>, private val onDaftarLayananAdapter: DaftarPengesahAdapter.OnClick) : RecyclerView.Adapter<DaftarPengesahAdapter.Holder>() {
    var list: MutableList<PengesahModel>
    var context: Context


    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaftarPengesahAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_daftar_pengesah, parent, false)
        return DaftarPengesahAdapter.Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DaftarPengesahAdapter.Holder, position: Int) {
        holder.bind(position, list.get(position), onDaftarLayananAdapter)
    }

    public interface OnClick{
        fun onClick(model: PengesahModel)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: PengesahModel, onNotifikasiClick: OnClick): Unit = with(itemView) {
            setOnClickListener({
                onNotifikasiClick.onClick(data)
            })
            tv_status.text = data.official_position
            tv_nama.text = data.official_name
            tv_lokasi.text = data.oi_name
        }
    }

}
