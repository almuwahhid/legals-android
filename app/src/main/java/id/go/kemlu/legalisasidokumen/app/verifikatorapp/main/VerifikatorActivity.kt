package id.go.kemlu.legalisasidokumen.app.verifikatorapp.main

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.login.LoginActivity
import id.go.kemlu.legalisasidokumen.app.tentangaplikasi.TentangAplikasiActivity
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.daftarpengesah.DaftarPengesahActivity
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.detaildokumen.DetailDokumenToVerifActivity
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.main.adapter.VerifikasiAdapter
import id.go.kemlu.legalisasidokumen.data.Preferences
import id.go.kemlu.legalisasidokumen.data.models.RequestModel
import id.go.kemlu.legalisasidokumen.data.models.RequestToVerifModel
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import id.go.kemlu.legalisasidokumen.utils.avatarview.AvatarPlaceholder
import id.go.kemlu.legalisasidokumen.utils.avatarview.loader.PicassoLoader
import id.go.kemlu.legalisasidokumen.utils.avatarview.views.AvatarView
import kotlinx.android.synthetic.main.activity_verifikator.*
import kotlinx.android.synthetic.main.app_bar_verifikator.*
import kotlinx.android.synthetic.main.layout_helper.*
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.GmsStatic


class VerifikatorActivity : LegalisasiActivity(), NavigationView.OnNavigationItemSelectedListener, VerifikatorView.View {

    val imageLoader = PicassoLoader()
    lateinit var presenter : VerifikatorPresenter

    lateinit var daftarLayananAdapter: VerifikasiAdapter
    var layananModels: MutableList<RequestToVerifModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikator)
        setSupportActionBar(toolbar)

        presenter = VerifikatorPresenter(context, this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_hamburger_menu)

        nav_view.setNavigationItemSelectedListener(this)

        val userModel = LegalisasiFunction.getUserPreference(context)
        val nv = nav_view.getHeaderView(0)
        val refreshableAvatarPlaceholder = AvatarPlaceholder(userModel.user_name)
        (nv.findViewById(R.id.tv_nav_header) as TextView).setText(userModel.user_name)
        imageLoader.loadImage((nv.findViewById(R.id.avatar) as AvatarView), refreshableAvatarPlaceholder, if(userModel.user_photo_url.equals("")) "www.google.com" else userModel.user_photo_url)


        daftarLayananAdapter = VerifikasiAdapter(context!!, layananModels, object : VerifikasiAdapter.OnVerifikasiAdapter{
            override fun onClick(model: RequestToVerifModel) {
                presenter.requestDetail(model)
            }
        })
        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = daftarLayananAdapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestDataVerifikasi(false)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestDataVerifikasi(true)
        }

        presenter.requestDataVerifikasi(true)
        nav_view.getMenu().getItem(0).setChecked(true);

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.verifikator, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> true
            R.id.action_histori -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_beranda -> {

            }
            R.id.nav_tentang -> {
                startActivity(Intent(context, TentangAplikasiActivity::class.java))
                nav_view.getMenu().getItem(0).setChecked(true);
            }
            R.id.nav_pengesah -> {
                startActivity(Intent(context, DaftarPengesahActivity::class.java))
                nav_view.getMenu().getItem(0).setChecked(true);
            }
            R.id.nav_logout -> {
                AlertDialogBuilder(context,
                    "Apakah Anda yakin ingin logout?",
                    "Ya",
                    "Tidak", object : AlertDialogBuilder.OnAlertDialog{
                        override fun onPositiveButton(dialog: DialogInterface?) {
                            GmsStatic.setSPString(context, Preferences.USER_INTRO, "")
                            LegalisasiFunction.logoutUser(context)
                            startActivity(Intent(context, LoginActivity::class.java))
                            finish()
                        }

                        override fun onNegativeButton(dialog: DialogInterface?) {
                            nav_view.getMenu().getItem(0).setChecked(true);
                        }

                    })
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestDataVerifikasi(list: MutableList<RequestToVerifModel>, isReload: Boolean) {
        endlessRecyclerViewScrollListener.resetState()
        if(isReload){
            layananModels.clear()
        }

        layananModels.addAll(list)
        daftarLayananAdapter.notifyDataSetChanged()
    }

    override fun onFailedRequestSomething(isFirst: Boolean, message: String) {
        if(isFirst){
            helper_error.visibility = View.VISIBLE
            tv_error.setText(message)
        } else {
            GmsStatic.ToastShort(context, message)
        }
    }

    override fun onFailedRequestMore(isFirst: Boolean, message : String) {
        if(isFirst){
            helper_nodata.visibility = View.VISIBLE
            tv_nodata.text = message
        }
    }

    override fun onHideLoading(isFirst: Boolean) {
        helper_nodata.visibility = View.GONE
        if(isFirst){
            helper_loading_top.hide()
        } else {
            helper_loading_more.hide()
        }
    }

    override fun onHideLoading() {

    }

    override fun onErrorConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
    }

    override fun onRequestDetail(model: RequestModel) {
        startActivity(Intent(context, DetailDokumenToVerifActivity::class.java).putExtra("data", model))
    }
}
