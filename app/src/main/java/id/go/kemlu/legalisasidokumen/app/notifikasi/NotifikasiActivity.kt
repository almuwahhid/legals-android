package id.go.kemlu.legalisasidokumen.app.notifikasi

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.go.kemlu.legalisasidokumen.R
import kotlinx.android.synthetic.main.toolbar_white.*

class NotifikasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Notifikasi")
    }
}
