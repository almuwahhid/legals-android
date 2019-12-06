package id.go.kemlu.legalisasidokumen.app.tentangaplikasi

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.go.kemlu.legalisasidokumen.BuildConfig
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import kotlinx.android.synthetic.main.activity_tentang_aplikasi.*
import kotlinx.android.synthetic.main.toolbar_white.*

class TentangAplikasiActivity : LegalisasiActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_aplikasi)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Tentang Aplikasi")

        tv_nama.setText(BuildConfig.application_name)
        tv_version.setText(BuildConfig.VERSION_NAME)
        tv_official.setOnClickListener({
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(tv_official.text.toString())))
        })
    }
}
