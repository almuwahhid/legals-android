package id.go.kemlu.legalisasidokumen.app.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.home.HomeActivity
import id.go.kemlu.legalisasidokumen.app.lupapassword.LupaPasswordDialog
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.VerifikatorActivity
import id.go.kemlu.legalisasidokumen.app.register.RegisterActivity
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.GmsStatic
import java.util.ArrayList

class LoginActivity : LegalisasiActivity(), LoginView.View {

    var gson: Gson? = null;
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        gson = Gson()
        if(LegalisasiFunction.checkUserPreference(this)){
            val usermodel = LegalisasiFunction.getUserPreference(context)
            if(usermodel.user_role.equals("User APPS")){
                startActivity(Intent(context, HomeActivity::class.java))
            } else {
                startActivity(Intent(context, VerifikatorActivity::class.java))
            }
            finish()
        }

        presenter = LoginPresenter(context, this)
        setFormsToValidate()
        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)

        tv_lupapassword.setOnClickListener({
            LupaPasswordDialog(context)
        })
        btn_login.setOnClickListener({
            if(!toggle_login.isDisplaying(lay_password)){
                presenter.authEmail(edt_email.text.toString())
            } else {
                presenter.requestLogin(LoginUiModel(edt_email.text.toString(), edt_password.text.toString()))
            }

        })
        tv_daftar.setOnClickListener({
            startActivity(Intent(context, RegisterActivity::class.java))
        })

        edt_email.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkKeyChanges(edt_email).let {
                    initButtonStyle(it)
                }
            }
        })

        edt_password.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkKeyChanges(edt_password).let {
                    initButtonStyle(it)
                }
            }
        })
    }

    private fun initButtonStyle(isUnable: Boolean){
        if(isUnable){
            btn_login.setBackground(resources.getDrawable(R.drawable.button_main))
            btn_login.setClickable(true)
        } else {
            btn_login.setBackground(resources.getDrawable(R.drawable.button_disable))
            btn_login.setClickable(false)
        }
    }

    private fun checkKeyChanges(edt: EditText): Boolean {
        return if (edt.getText().toString().equals("")) {
            false
        } else {
            true
        }
    }


    override fun onSuccessLogin(model: UserModel) {
        LegalisasiFunction.setUserPreference(this, gson!!.toJson(model))
        if(model.user_role.equals("User APPS")){
            startActivity(Intent(context, HomeActivity::class.java))
        } else {
            startActivity(Intent(context, VerifikatorActivity::class.java))
        }

        finish()
    }

    override fun onFailedLogin(message: String) {
//        tv_loading_top.setText(message)
//        helper_loading_top.showForAWhile(this)
        GmsStatic.ToastShort(context, message)
    }

    override fun onErrorConnection() {
        tv_loading_top.setText("Bermasalah dengan server")
        helper_loading_top.showForAWhile(this)
    }

    override fun onHideLoading() {
        GmsStatic.hideLoadingDialog(this)
    }

    override fun onLoading() {
        lay_noemail.visibility = View.GONE
        GmsStatic.showLoadingDialog(this, R.drawable.ic_logo)
    }

    private fun validate() {
        if (GmsStatic.isFormValid(this, window.decorView, forms)) {
            presenter.requestLogin(LoginUiModel(edt_email.text.toString(), edt_password.text.toString()))
        }
    }

    internal var forms: MutableList<Int> = ArrayList()
    private fun setFormsToValidate() {
        forms.add(R.id.edt_password)
        forms.add(R.id.edt_email)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val keyaction = event!!.getAction()
        if(keyaction == KeyEvent.ACTION_DOWN){
            if(toggle_login.isDisplaying(lay_password)){
                toFirstStep()
            } else {
                finish()
            }
        }
        return true
    }

    override fun onSuccessAuthEmail(message: String) {
        toggle_login.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_left)
        initButtonStyle(false)
        btn_login.setText("M A S U K")
        toggle_login.hide(form_email)
        toggle_login.display(lay_password)
        edt_password.requestFocus()
        GmsStatic.ToastShort(context, message)
    }

    private fun toFirstStep(){
        toggle_login.setInOutAnimation(R.anim.pull_in_left, R.anim.push_out_right)
        initButtonStyle(true)
        toggle_login.hide(lay_password)
        toggle_login.display(form_email)
        edt_email.requestFocus()
        btn_login.setText("SELANJUTNYA")
    }

    override fun onFailedAuthEmail(message: String) {
        lay_noemail.visibility = View.VISIBLE
        tv_email_error.setText(message)
    }

    override fun onRegisterAuthEmail(email: String) {
        startActivity(Intent(context, RegisterActivity::class.java).putExtra("data", email))
    }

    override fun onForgotAuthEmail(email: String) {
        LupaPasswordDialog(context).initEmail(email)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                if(toggle_login.isDisplaying(lay_password)){
                    toFirstStep()
                } else {
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
