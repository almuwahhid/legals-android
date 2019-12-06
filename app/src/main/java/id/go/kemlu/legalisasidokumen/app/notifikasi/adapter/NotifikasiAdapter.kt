package id.go.kemlu.legalisasidokumen.app.notifikasi.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel
import kotlinx.android.synthetic.main.adapter_notifikasi.view.*

class NotifikasiAdapter (context: Context, list: MutableList<NotifikasiModel>, private val onDaftarLayananAdapter: NotifikasiAdapter.OnNotifikasiClick) : RecyclerView.Adapter<NotifikasiAdapter.Holder>() {

    var list: MutableList<NotifikasiModel>
    var context: Context


    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_notifikasi, parent, false)
        return NotifikasiAdapter.Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotifikasiAdapter.Holder, position: Int) {
        holder.bind(position, list.get(position), onDaftarLayananAdapter)
    }

    public interface OnNotifikasiClick{
        fun onClick(model: NotifikasiModel)
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: NotifikasiModel, onNotifikasiClick: OnNotifikasiClick): Unit = with(itemView) {
            setOnClickListener({
                onNotifikasiClick.onClick(data)
            })
            tv_status.text = data.strType
            tv_date.text = data.dtmNotif
            tv_keterangan.text = data.strNotifDesc
        }
    }

}
