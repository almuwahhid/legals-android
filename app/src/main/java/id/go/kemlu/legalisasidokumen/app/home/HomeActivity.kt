package id.go.kemlu.legalisasidokumen.app.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.Menu
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.buatpermohonan.BuatPermohonanActivity
import id.go.kemlu.legalisasidokumen.app.login.LoginActivity
import id.go.kemlu.legalisasidokumen.app.notifikasi.NotifikasiActivity
import id.go.kemlu.legalisasidokumen.app.tentangaplikasi.TentangAplikasiActivity
import id.go.kemlu.legalisasidokumen.data.Preferences
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import id.go.kemlu.legalisasidokumen.utils.avatarview.AvatarPlaceholder
import id.go.kemlu.legalisasidokumen.utils.avatarview.loader.PicassoLoader
import id.go.kemlu.legalisasidokumen.utils.avatarview.views.AvatarView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import lib.gmsframeworkx.utils.AlertDialogBuilder
import lib.gmsframeworkx.utils.GmsStatic

class HomeActivity : LegalisasiActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var homeViewModel: HomeViewModel
    val imageLoader = PicassoLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        homeViewModel = HomeViewModel()
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

        initComponent()
        nav_view.getMenu().getItem(0).setChecked(true);
    }

    private fun initComponent(){
        nav_view.getMenu().getItem(0).setChecked(true);

        viewPager.setAdapter(TabMain(getSupportFragmentManager(), homeViewModel))
        viewPager.setOffscreenPageLimit(0)
        tablayout.setupWithViewPager(viewPager)
        tablayout.getTabAt(0)!!.setText("Semua")
        tablayout.getTabAt(1)!!.setText("Permohonan")
        tablayout.getTabAt(2)!!.setText("Pembayaran")
        tablayout.getTabAt(3)!!.setText("Selesai")
        tablayout.getTabAt(4)!!.setText("Ditolak")

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }

        })

        fab_add.setOnClickListener({
            startActivity(Intent(context, BuatPermohonanActivity::class.java))
        })
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
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_notification -> {
                startActivity(Intent(context, NotifikasiActivity::class.java))
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
}
