package id.go.kemlu.legalisasidokumen.dialogs.DialogPicker

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.kemlu.legalisasidokumen.R
import id.go.kemlu.legalisasidokumen.data.models.PickerModel
import id.go.kemlu.legalisasidokumen.dialogs.DialogPicker.adapter.PickerAdapter
import id.go.kemlu.legalisasidokumen.utils.LegalisasiFunction
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_daftar_negara.*
import lib.gmsframeworkx.utils.DialogBuilder
import lib.gmsframeworkx.utils.GmsStatic
import java.util.concurrent.TimeUnit

class DialogPicker (context: Context?, title: String, isNeedSearch: Boolean, listPicker: MutableList<PickerModel>, onListener: OnPickerListener ) : DialogBuilder(context, R.layout.dialog_daftar_negara), PickerAdapter.PickerAdapterListener, DialogPickerView.View {
    lateinit var adapterPicker: PickerAdapter
    lateinit var onListener: OnPickerListener
    lateinit var listPicker : MutableList<PickerModel>
    var reallistPicker : MutableList<PickerModel> = ArrayList()
    lateinit var dialogPickerPresenter: DialogPickerPresenter

    private var subject_edittext: PublishSubject<String>? = null

    init{
        this.onListener = onListener
        this.listPicker = listPicker
        this.reallistPicker.addAll(listPicker)
        dialog.toolbar_title.setText(title)
        if(!isNeedSearch){
            dialog.img_search.visibility = View.GONE
        }

        dialogPickerPresenter = DialogPickerPresenter(this)

        adapterPicker = PickerAdapter(listPicker, this)
        dialog.rv.layoutManager = LinearLayoutManager(getContext())
        dialog.rv.adapter = adapterPicker

        val onClick = View.OnClickListener {
            when(it.id){
                R.id.img_search->{
                    displayToolbar(false)
                }
                R.id.img_back->{
                    if(dialog.toggle_toolbar.isDisplaying(dialog.edt_search)){
                        displayToolbar(true)
                    } else{
                        dismiss()
                    }
                }

            }
        }

        dialog.img_search.setOnClickListener(onClick)
        dialog.img_back.setOnClickListener(onClick)

        subject_edittext = PublishSubject.create()
        subject_edittext!!.debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(s: String) {
                    Log.d("DialogPickerText", s+reallistPicker.size)
                    if(s.equals("")){
                        listPicker.clear()
                        listPicker.addAll(reallistPicker)
                        adapterPicker.notifyDataSetChanged()
                    } else {
                        dialogPickerPresenter.filterListPicker(reallistPicker, s)
                    }
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
        dialog.edt_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                subject_edittext!!.onNext(s.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        val onKeyListener = object : DialogInterface.OnKeyListener{
            override fun onKey(p0: DialogInterface?, p1: Int, p2: KeyEvent?): Boolean {
                val keyaction = p2!!.getAction()
                if(keyaction == KeyEvent.ACTION_DOWN){
                    Log.d("DialogPicker", "down")
                    if(dialog.toggle_toolbar.isDisplaying(dialog.lay_toolbar)){
                        dismiss()
                    } else {
                        displayToolbar(true)
                    }
                }
                return true
            }
        }

        dialog.setOnKeyListener(onKeyListener)

        show()
    }

    override fun onFiltering(lizt: MutableList<PickerModel>) {
        Log.d("DialogPickerCount", ""+lizt.size)
        listPicker.clear()
        listPicker.addAll(lizt)
        adapterPicker.notifyDataSetChanged()
    }

    fun displayToolbar(isDisplay: Boolean){
        if(isDisplay){
            dialog.toggle_toolbar.hide(dialog.edt_search)
            dialog.toggle_toolbar.displaying(dialog.lay_toolbar)
            LegalisasiFunction.hideSoftKeyboard(context, dialog.edt_search)
            listPicker.clear()
            listPicker.addAll(reallistPicker)
            adapterPicker.notifyDataSetChanged()
        } else {
            dialog.toggle_toolbar.displaying(dialog.edt_search)
            dialog.toggle_toolbar.hide(dialog.lay_toolbar)
            LegalisasiFunction.showKeyboard(context, dialog.edt_search)
        }
    }

    override fun onItemClick(pickerModel: PickerModel, position: Int) {
        onListener.onItemClick(pickerModel)
        dismiss()
    }

    interface OnPickerListener {
        fun onItemClick(pickerModel: PickerModel)
    }
}