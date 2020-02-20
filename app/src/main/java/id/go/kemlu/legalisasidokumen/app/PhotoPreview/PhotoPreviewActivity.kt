package id.go.kemlu.legalisasidokumen.app.PhotoPreview

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.BitmapTransform
import kotlinx.android.synthetic.main.activity_photo_preview.*
import kotlinx.android.synthetic.main.toolbar_transparent.*

class PhotoPreviewActivity : LegalisasiActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }

        val data = intent.extras

        if (data == null)
            finish()

        when(data.getString("type")){
            "uri" -> {
                val uri = Uri.parse(data.getString("image"))
                Picasso.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_logo)
                    .into(img_preview, object : Callback {
                        override fun onError() {

                        }
                        override fun onSuccess() {
                            progress_bar.setVisibility(View.GONE)
                        }

                        fun onError(e: Exception) {
                            progress_bar.setVisibility(View.GONE)

                        }
                    })
            }
            "image" -> {
                Picasso.with(context)
                    .load(data.getString("image"))
                    .placeholder(R.drawable.ic_logo)
                    .into(img_preview, object : Callback {
                        override fun onError() {

                        }
                        override fun onSuccess() {
                            progress_bar.setVisibility(View.GONE)
                        }

                        fun onError(e: Exception) {
                            progress_bar.setVisibility(View.GONE)

                        }
                    })
            }
        }
    }
}
