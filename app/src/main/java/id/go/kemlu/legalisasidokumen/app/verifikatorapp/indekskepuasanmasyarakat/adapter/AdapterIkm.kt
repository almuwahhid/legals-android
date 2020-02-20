package id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.IKMCommentModel
import kotlinx.android.synthetic.main.adapter_ikm.view.*

class AdapterIkm (context: Context, list: MutableList<IKMCommentModel>) : RecyclerView.Adapter<AdapterIkm.Holder>() {

    var list: MutableList<IKMCommentModel>
    var context: Context


    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterIkm.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ikm, parent, false)
        return AdapterIkm.Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterIkm.Holder, position: Int) {
        holder.bind(position, list.get(position))
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, data: IKMCommentModel): Unit = with(itemView) {
            tv_date.setText(data.dtm_req)
            tv_name.setText(data.request_name)
            tv_no_grup.setText(data.group_no)
            img_smile.setImageResource(
                when(data.ikm){
                    1 -> R.drawable.ic_smile_1
                    2 -> R.drawable.ic_smile_2
                    3 -> R.drawable.ic_smile_3
                    else -> R.drawable.ic_smile_1
                }
            )
            tv_comment.setText(if(data.ikm_comment.equals("")) "-" else data.ikm_comment)
        }
    }

}
