package id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.app.verifikatorapp.indekskepuasanmasyarakat.adapter.AdapterIkm
import id.go.kemlu.legalisasidokumen.data.models.IKMCommentModel
import id.go.kemlu.legalisasidokumen.data.models.IkmModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogRequestIkm.DialogRequestIkm
import id.go.kemlu.legalisasidokumen.module.Activity.LegalisasiActivity
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.EndlessRecyclerViewScrollListener
import id.go.kemlu.legalisasidokumen.utils.LayoutManagerUtil.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_ikm.*
import kotlinx.android.synthetic.main.layout_helper.*
import kotlinx.android.synthetic.main.toolbar_white.*
import lib.gmsframeworkx.utils.GmsStatic
import java.util.*
import kotlin.collections.ArrayList

class IKMActivity : LegalisasiActivity(), IKMView.View, DatePickerDialog.OnDateSetListener {
    lateinit var model : IkmModel
    lateinit var adapter: AdapterIkm
    var list: MutableList<IKMCommentModel> = ArrayList()
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    internal lateinit var presenter:IKMPresenter
    internal var status_id: Int = 0

    var date1 = ""
    var date2 = ""
    var keyIkm = 0

    lateinit var dialogIkm : DialogRequestIkm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ikm)

        if(intent.hasExtra("data")){
            model = intent.getSerializableExtra("data") as IkmModel
            date1 = intent.getStringExtra("date1")
            date2 = intent.getStringExtra("date2")
        } else {
            finish()
        }

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(true)
            toolbar_title.setText("Indeks Kepuasan Masyarakat")
        }
        getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
        helper_loading_more.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom)

        presenter = IKMPresenter(context, this)
        adapter = AdapterIkm(context, list)
        val layoutManager = SpeedyLinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {
                presenter.requestComment(false, date1, date2)
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            presenter.requestComment(true, date1, date2)
        }

        initData()
    }

    private fun initData(){
        tv_1.setText(""+model.tidak_puas)
        tv_2.setText(""+model.puas)
        tv_3.setText(""+model.sangat_puas)
        tv_total.setText(""+model.total)
        presenter.requestComment(true, date1, date2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.ikm, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_daterange->{
                dialogIkm = DialogRequestIkm(context, object : DialogRequestIkm.OnRequestIkm{
                    override fun onClickDate(key: Int) {
                        keyIkm = key
                        val now = Calendar.getInstance()
                        val dpd = DatePickerDialog.newInstance(
                            this@IKMActivity,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                        )
                        dpd.maxDate = now
                        dpd.firstDayOfWeek = Calendar.MONDAY
                        dpd.accentColor = ContextCompat.getColor(context, R.color.colorPrimary)
                        dpd.show(fragmentManager, "Tanggal Kejadian")
                    }

                    override fun onIkmRequested(ikmModel: IkmModel, d1: String, d2: String) {
                        model = ikmModel
                        date1 = d1
                        date2 = d2
                        initData()
                    }

                })
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestIkm(ls: MutableList<IKMCommentModel>, isReload: Boolean) {
        helper_nodata.visibility = View.GONE
        helper_error.visibility = View.GONE
        endlessRecyclerViewScrollListener.resetState()
        if(isReload){
            list.clear()
        }

        list.addAll(ls)
        adapter.notifyDataSetChanged()
    }

    override fun onFailedRequestSomething(isFirst: Boolean, message: String) {
        if(isFirst){
            helper_error.visibility = View.VISIBLE
            tv_error.setText(message)
        } else {
            GmsStatic.ToastShort(context, message)
        }
    }

    override fun onFailedRequestMore(isFirst: Boolean, message: String) {
        if(isFirst){
            list.clear()
            adapter.notifyDataSetChanged()
            helper_nodata.visibility = View.VISIBLE
            tv_nodata.text = message
        }
    }

    override fun onLoading() {
        helper_loading_top.show()
    }

    override fun onLoadingMore() {
        helper_loading_more.show()
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

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        dialogIkm.onDateClicked(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth, keyIkm)
    }

}
