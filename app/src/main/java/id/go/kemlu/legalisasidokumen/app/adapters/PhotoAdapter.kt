package id.go.kemlu.legalisasidokumen.app.adapters

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PhotoModel
import id.go.kemlu.legalisasidokumen.utils.BitmapTransform
import kotlinx.android.synthetic.main.adapter_photo.view.*
import lib.gmsframeworkx.utils.AlertDialogBuilder

class PhotoAdapter (context: Context, list: MutableList<PhotoModel>, isEditable: Boolean, private val onPhotoCLick: OnPhotoAdapter) : RecyclerView.Adapter<PhotoAdapter.Holder>() {

    lateinit var list: MutableList<PhotoModel>
    lateinit var context: Context
    var isEditable: Boolean

    init {
        this.context = context
        this.list = list
        this.isEditable = isEditable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_photo, parent, false)
        return Holder(layoutView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindTo(position, list.get(position), onPhotoCLick)
    }

    interface OnPhotoAdapter{
        fun onPhotoClick(view: View, photoModel: PhotoModel)
        fun onDeleteClick(position: Int)
        fun onAddPhoto()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(position: Int, data: PhotoModel, onPhotoCLick: OnPhotoAdapter): Unit = with(itemView) {
            img_delete.setOnClickListener({
                AlertDialogBuilder(context,
                                    "Apakah Anda yakin ingin menghapus foto ini?",
                                    "Ya",
                                    "Tidak",
                                    object : AlertDialogBuilder.OnAlertDialog{
                                        override fun onPositiveButton(dialog: DialogInterface?) {
                                            onPhotoCLick.onDeleteClick(position)
                                        }

                                        override fun onNegativeButton(dialog: DialogInterface?) {

                                        }

                                    })

            })

            if(isEditable){
                when(position){
                    0 ->{
                        img_delete.visibility = View.GONE
                        lay_upload.setOnClickListener({
                            Log.d("here", "hulala")
                            onPhotoCLick.onAddPhoto()
                        })
                    }
                    else -> {
                        Log.d("here", "hulalas")
                        img_delete.visibility = View.VISIBLE
                        lay_upload.setOnClickListener({
                            onPhotoCLick.onPhotoClick(img, data)
                        })
                        if(data.url.equals("")){
                            Picasso.with(context)
                                .load(data.uri)
                                .placeholder(R.drawable.bg_grey)
                                .transform(BitmapTransform())
                                .into(img)
                        } else {
                            Picasso.with(context)
                                .load(data.url)
                                .placeholder(R.drawable.bg_grey)
                                .transform(BitmapTransform())
                                .into(img)
                        }
                    }
                }
            } else {
                img_delete.visibility = View.GONE
                lay_upload.setOnClickListener({
                    onPhotoCLick.onPhotoClick(img, data)
                })
                if(!data.url.equals("")){
                    Picasso.with(context)
                        .load(data.uri)
                        .placeholder(R.drawable.bg_grey)
                        .transform(BitmapTransform())
                        .into(img)
                } else {
                    Picasso.with(context)
                        .load(data.url)
                        .placeholder(R.drawable.bg_grey)
                        .transform(BitmapTransform())
                        .into(img)
                }
            }
        }
    }
}
