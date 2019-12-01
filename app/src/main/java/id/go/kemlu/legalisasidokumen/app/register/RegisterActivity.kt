package id.go.kemlu.legalisasidokumen.app.register

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.uny.riset.ride.menu.register.RegisterPresenter
import id.ac.uny.riset.ride.menu.register.RegisterView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.PhotoPreview.PhotoPreviewActivity
import id.go.kemlu.legalisasidokumen.app.adapters.PhotoAdapter
import id.go.kemlu.legalisasidokumen.app.login.LoginActivity
import id.go.kemlu.legalisasidokumen.app.register.model.RegisterUiModel
import id.go.kemlu.legalisasidokumen.data.models.PhotoModel
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogImagePicker
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiPermissionActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import id.go.kemlu.legalisasidokumen.utils.avatarview.AvatarPlaceholder
import id.go.kemlu.legalisasidokumen.utils.avatarview.loader.PicassoLoader
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface
import lib.gmsframeworkx.easyphotopicker.DefaultCallback
import lib.gmsframeworkx.easyphotopicker.EasyImage
import lib.gmsframeworkx.utils.GmsStatic
import java.io.File
import java.util.ArrayList

class RegisterActivity : LegalisasiPermissionActivity(), RegisterView.View {
    var registerUiModel = RegisterUiModel()
    var presenter: RegisterPresenter? = null
    lateinit var photoAdapter: PhotoAdapter
    lateinit var list_photo: MutableList<PhotoModel>

    val imageLoader = PicassoLoader()

    protected var RequiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            it!!.setTitle("")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        toolbar_title.setText("Pendaftaran")

        if(intent.hasExtra("data")){
            edt_email.setText(intent.getStringExtra("data"))
        }

        list_photo = ArrayList()
        list_photo.add(PhotoModel())
        photoAdapter = PhotoAdapter(
            context,
            list_photo,
            true,
            object : PhotoAdapter.OnPhotoAdapter {
                override fun onDeleteClick(position: Int) {
                    list_photo.removeAt(position)
                    photoAdapter.notifyDataSetChanged()
                }

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
                            activity,
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


                override fun onAddPhoto() {
                    Log.d("RegisterAct", "this : "+list_photo.size)
                    if (list_photo.size < 2){
                        askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                            override fun permissionDenied() {

                            }

                            override fun permissionGranted() {
                                DialogImagePicker(context, object : DialogImagePicker.OnDialogImagePicker {
                                    override fun onCameraClick() {
                                        EasyImage.openCamera(this@RegisterActivity, 0)
                                    }

                                    override fun onFileManagerClick() {
                                        EasyImage.openGallery(this@RegisterActivity, 0)
                                    }

                                })
                            }

                        })
                    } else {
                        GmsStatic.ToastShort(context, "Hapus terlebih dahulu identitas Anda")
                    }
                }

            })
        rv_photo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_photo.adapter = photoAdapter

        presenter = RegisterPresenter(context, this)
        setFormsToValidate()

        tv_masuk.setOnClickListener({
            finish()
            startActivity(Intent(context, LoginActivity::class.java))
        })

        btn_kirim.setOnClickListener({
            validate()
        })

        val refreshableAvatarPlaceholder = AvatarPlaceholder("BL")
        imageLoader.loadImage(avatar_profil, refreshableAvatarPlaceholder, "www.google.com")
    }

    override fun onSubmitRegister(isSuccess: Boolean, message: String) {
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
        GmsStatic.showLoadingDialog(this, R.drawable.ic_logo)
    }

    private fun validate() {
        if(list_photo.size < 2){
            GmsStatic.ToastShort(context, "Tambahkan terlebih dahulu identitas Anda")
        } else {
            if (GmsStatic.isFormValid(this, window.decorView, forms)) {
                if(edt_password.text.toString().equals(edt_konfirmasipassword.text.toString())){
                    registerUiModel = RegisterUiModel(edt_namalengkap.text.toString(),
                        edt_identitas.text.toString(),
                        edt_telp.text.toString(),
                        edt_email.text.toString(),
                        edt_password.text.toString(),
                        edt_konfirmasipassword.text.toString())
                    for(x in list_photo){
                        if(!x.base64.equals("")){
                            registerUiModel.user_photo = x.base64
                        }
                    }
                    presenter!!.submitRegister(registerUiModel)
                } else {
                    GmsStatic.ToastShort(context, "Password belum sesuai")
                    edt_konfirmasipassword.setError("Password belum sesuai")
                }
//
            }
        }
    }

    internal var forms: ArrayList<Int> = ArrayList()
    private fun setFormsToValidate() {
        forms.add(R.id.edt_namalengkap)
        forms.add(R.id.edt_telp)
        forms.add(R.id.edt_email)
        forms.add(R.id.edt_password)
        forms.add(R.id.edt_konfirmasipassword)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback(){
            override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {
                Log.d("ikiopo", "")
                val uri = Uri.fromFile(imageFiles.get(0))
                val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
                list_photo.add(PhotoModel("", GmsStatic.convertToBase64(bitmap), uri))
                photoAdapter.notifyDataSetChanged()
            }
        })
    }
}
