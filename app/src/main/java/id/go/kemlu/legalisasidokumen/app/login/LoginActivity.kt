package id.go.kemlu.legalisasidokumen.app.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.home.HomeActivity
import id.go.kemlu.legalisasidokumen.app.lupapassword.LupaPasswordDialog
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.VerifikatorActivity
import id.go.kemlu.legalisasidokumen.app.register.RegisterActivity
import id.go.kemlu.legalisasidokumen.data.Preferences
import id.go.kemlu.legalisasidokumen.data.models.UserModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import id.go.kemlu.legalisasidokumen.utils.avatarview.AvatarPlaceholder
import id.go.kemlu.legalisasidokumen.utils.avatarview.loader.PicassoLoader
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.GmsStatic
import java.util.*

class LoginActivity : LegalisasiActivity(), LoginView.View {

    var gson: Gson? = null;
    lateinit var presenter: LoginPresenter
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mCallbackManager: CallbackManager
    private val RC_SIGN_IN = 9001
    private var email = ""
    private var third_party = ""
    private var token_id = ""

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(getApplicationContext())
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

        auth = FirebaseAuth.getInstance()
        presenter = LoginPresenter(context, this)
        setFormsToValidate()
        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)

        tv_lupapassword.setOnClickListener({
            LupaPasswordDialog(context)
        })
        btn_login.setOnClickListener({
            if(!toggle_login.isDisplaying(lay_password)){
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString().replace(" ", "")).matches()){
                    presenter.authEmail(edt_email.text.toString())
                } else {
                    edt_email.setError("Format email salah")
                }
            } else {
                presenter.requestLogin(LoginUiModel(edt_email.text.toString(), edt_password.text.toString()))
            }

        })
        tv_daftar.setOnClickListener({
            startActivity(Intent(context, RegisterActivity::class.java))
        })

        card_google.setOnClickListener({
            third_party = "GOOGLE"
            googleSignIn()
        })

        card_fb.setOnClickListener({
            third_party = "FACEBOOK"
            fbSignIn()
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("facebookz", "Facebook onScuccess: ")
                firebaseAuthWithFacebook(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("facebookz", "Facebook onCancel: ")
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            override fun onError(error: FacebookException) {
                Log.d("facebookz", "Facebook onError: "+error.message)
//                error.stackTrace
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        })
    }

    private fun fbSignIn(){
        LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList<String>("email", "public_profile"))
    }

    private fun googleSignIn() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
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
        var token = FirebaseInstanceId.getInstance().getToken()
        if (token == null) {
            token = FirebaseInstanceId.getInstance().getToken()
        }
        GmsStatic.setSPString(context, Preferences.FIREBASE_TOKEN, token)
        presenter.updateTokenFirebase(token!!, model)
    }

    override fun onSuccessUpdateTokenFirebase(model: UserModel) {
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

    override fun onFailedLoginThirdParty(message: String) {
        GmsStatic.ToastShort(context, message)
        startActivity(Intent(context, RegisterActivity::class.java).putExtra("data", email).putExtra("isThirdParty", third_party).putExtra("id_token", token_id))
    }

    override fun onSuccessAuthEmail(message: String, name: String, email: String) {
        toggle_login.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_left)
        initButtonStyle(false)
        btn_login.setText("M A S U K")
        toggle_login.hide(form_email)
        toggle_login.display(lay_password)
        edt_password.requestFocus()
        GmsStatic.ToastShort(context, message)

        tv_email.setText(email)
        tv_name.setText("Hi "+name)

        val refreshableAvatarPlaceholder = AvatarPlaceholder(name)
        PicassoLoader().loadImage(avatar, refreshableAvatarPlaceholder, "www.google.com" )

        lay_bottom.visibility = View.GONE
        lay_logo.visibility = View.GONE
        lay_profile.visibility = View.VISIBLE
    }

    private fun toFirstStep(){
        toggle_login.setInOutAnimation(R.anim.pull_in_left, R.anim.push_out_right)
        initButtonStyle(true)
        toggle_login.hide(lay_password)
        toggle_login.display(form_email)
        edt_email.requestFocus()
        btn_login.setText("SELANJUTNYA")

        lay_bottom.visibility = View.VISIBLE
        lay_logo.visibility = View.VISIBLE
        lay_profile.visibility = View.GONE
    }

    private fun firebaseAuthWithFacebook(token: AccessToken){
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginActivity", "signInWithCredential:success")
                    val user = auth.currentUser
                    email = user!!.email!!
                    token_id = token.token
                    presenter.oauthFacebook(token.token)
                } else {
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)

                    GmsStatic.ToastShort(context, "Authentication failed.")

                }
            }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("loginUser", "signInWithCredential:success")
                    val user = auth.currentUser
                    token_id = acct.idToken!!
                    presenter.authGoogle(acct.idToken!!)

                    email = acct.email!!
                } else {
                    Log.w("loginUser", "signInWithCredential:failure", task.exception)
                    tv_loading_top.setText("Authentication Failed")
//                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
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
        // automaticall¡y handle clicks on the Home/Up button, so long
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w("login", "Google sign in failed", e)
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}
