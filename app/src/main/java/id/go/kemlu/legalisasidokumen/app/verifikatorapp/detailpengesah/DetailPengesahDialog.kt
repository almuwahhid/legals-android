package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detailpengesah

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PengesahModel
import kotlinx.android.synthetic.main.dialog_detail_pengesah.*
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class DetailPengesahDialog(context: Context, model: PengesahModel) : DialogBuilder(context, R.layout.dialog_detail_pengesah) {

    init {
        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullWidth(lay_dialog)
            setGravity(Gravity.BOTTOM)

            tv_nip.setText(if(model.official_nip.equals("")) "-" else model.official_nip)
            tv_jabatan.setText(if(model.official_position.equals("")) "-" else model.official_position)
            tv_instansilembaga.setText(if(model.oi_name.equals("")) "-" else model.oi_name)
            tv_nama.setText(if(model.official_name.equals("")) "-" else model.official_name)
            tv_tglaktif.setText(if(model.create_date.equals("")) "-" else model.create_date)

            switching.isChecked = model.is_active
        }

        show()
    }
}
