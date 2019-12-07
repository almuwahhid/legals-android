package id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.PhotoPreview.PhotoPreviewActivity
import id.go.kemlu.legalisasidokumen.app.adapters.PhotoAdapter
import id.go.kemlu.legalisasidokumen.data.StaticData
import id.go.kemlu.legalisasidokumen.data.models.DokumenModel
import id.go.kemlu.legalisasidokumen.data.models.PhotoModel
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import kotlinx.android.synthetic.main.activity_detail_dokumen_to_verif.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class DetailDokumenToVerifActivity : LegalisasiActivity(), DetailDokumenToVerifView.View {

    lateinit var model : DokumenModel
    lateinit var reqmodel : RequestModel
    lateinit var photoAdapter: PhotoAdapter
    var list_photo = ArrayList<PhotoModel>()
    lateinit var presenter : DetailDokumenToVerifView.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dokumen_to_verif)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as DokumenModel
            reqmodel = intent.getSerializableExtra("parent") as RequestModel
        } else {
            finish()
        }

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        tv_jenisdokumen.setText(model.document_name)
        tv_nomorpengesahan.setText(model.document_legal_no)
        tv_status.setText(model.status_detail)
        tv_instansilembaga.setText(model.status_detail)

        for (i in model.images){
            list_photo.add(PhotoModel(i.image_url))
        }

        if(list_photo.size > 1){
            photoAdapter = PhotoAdapter(
                getContext(),
                list_photo,
                false,
                object : PhotoAdapter.OnPhotoAdapter {
                    override fun onPhotoClick(view: View, photoModel: PhotoModel) {
                        val data = Bundle()
                        if (!photoModel.url.equals("")) {
                            data.putString("image", photoModel.url)
                            data.putString("type", "image")
                        } else {
                            data.putString("image", photoModel.uri.toString())
                            data.putString("type", "uri")
                        }


                        if (!LegalisasiFunction.isPreLolipop()) {
                            //                    Pair<View, String> pair1 = Pair.create(view, view.getTransitionName());
                            val options = ActivityOptions.makeSceneTransitionAnimation(
                                this@DetailDokumenToVerifActivity,
                                view,
                                ViewCompat.getTransitionName(view)
                            )
                            startActivity(
                                Intent(context, PhotoPreviewActivity::class.java).putExtras(data),
                                options.toBundle()
                            )
                        } else {
                            startActivity(Intent(context, PhotoPreviewActivity::class.java).putExtras(data))
                        }
                    }

                    override fun onDeleteClick(position: Int) {

                    }

                    override fun onAddPhoto() {

                    }

                })

            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = photoAdapter
        }

        val onClick = View.OnClickListener {
            var type = "ACCEPT"
            var msg = ""
            when(it.id){
                R.id.btn_setuju -> {
                    type = "ACCEPT"
                    msg = "Apakah Anda yakin ingin menyetujui dokumen ini?"
                }
                R.id.btn_tolak -> {
                    msg = "Apakah Anda yakin ingin menolak dokumen ini?"
                    type = "REJECT"
                }
            }

            AlertDialogBuilder(context,
                msg,
                "Ya",
                "Tidak",
                object : AlertDialogBuilder.OnAlertDialog{
                    override fun onPositiveButton(dialog: DialogInterface?) {
                        presenter.updateDocument(""+reqmodel.online_request_id, ""+model.document_id, type, edt_keterangan.text.toString())
                    }

                    override fun onNegativeButton(dialog: DialogInterface?) {

                    }

                })

        }

        btn_setuju.setOnClickListener(onClick)
        btn_tolak.setOnClickListener(onClick)

        when(model.status_id){
            StaticData.STATUS_MENUGGGU_VERIFIKASI -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.orange_400))
            }
            StaticData.STATUS_MENUNGGU_PEMBAYARAN -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.purple_400))

            }
            StaticData.STATUS_BUKTIBAYAR_TIDAKVALID -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.orange_400))

            }
            StaticData.STATUS_MENUNGGU_VERIFIKASIPEMBAYARAN -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.yellow_400))

            }
            StaticData.STATUS_BUKTIBAYAR_VALID -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.green_400))

            }
            StaticData.STATUS_PERMOHONAN_DITOLAK -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.red_400))
            }
            else -> {
                card_status.setCardBackgroundColor(context.resources.getColor(R.color.orange_400))
            }
        }
    }

    override fun onUpdateDocument(isSuccess: Boolean, message: String) {
        GmsStatic.ToastShort(context, message)
        if(isSuccess){
            finish()
        }
    }

    override fun onErrorConnection() {

    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(context)
    }

    override fun onLoading() {
        GmsStatic.showLoadingDialog(context, R.drawable.ic_logo)
    }
}
