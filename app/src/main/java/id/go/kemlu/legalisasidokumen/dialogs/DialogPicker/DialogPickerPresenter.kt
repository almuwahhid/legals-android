package id.go.kemlu.legalisasidokumen.dialogs.DialogPicker

import id.go.kemlu.legalisasidokumen.data.models.PickerModel

class DialogPickerPresenter(view: DialogPickerView.View): DialogPickerView.Presenter {

    var view : DialogPickerView.View
    init {
        this.view = view
    }

    override fun filterListPicker(list: MutableList<PickerModel>, keyword: String) {
        var list2 = ArrayList<PickerModel>()
        for (i in list){
            if(i.name.toLowerCase().contains(keyword.toLowerCase())){
                i.keyword = keyword
                list2.add(i)
            }
        }

        view.onFiltering(list2)
    }
}