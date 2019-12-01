package id.go.kemlu.legalisasidokumen.dialogs.DialogPicker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import kotlinx.android.synthetic.main.  adapter_picker.view.*

class PickerAdapter (list: MutableList<PickerModel>, private val onPickerListener: PickerAdapterListener) : RecyclerView.Adapter<PickerAdapter.Holder>() {

    lateinit var list: MutableList<PickerModel>

    init {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_picker, parent, false)
        return Holder(layoutView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PickerAdapter.Holder, position: Int) {
        holder.bindTo(position, list.get(position))
    }

    interface PickerAdapterListener{
        fun onItemClick(pickerModel: PickerModel, position: Int)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(position: Int, data: PickerModel): Unit = with(itemView) {
            tv_chooser.setTextToHighlight(data.keyword)
            tv_chooser.setTextHighlightColor("#BBDEFB")
            tv_chooser.setCaseInsensitive(true)
            tv_chooser.highlight()

            tv_chooser.setText(data.name)
            setOnClickListener({
                onPickerListener.onItemClick(data, position)
            })
        }
    }
}
