package id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpejabat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity

class DaftarPejabatActivity : LegalisasiActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_pejabat)
    }
}
