package id.go.kemlu.legalisasidokumen.dialogs.DialogPicker

import id.go.kemlu.legalisasidokumen.data.models.PickerModel

interface DialogPickerView {
    interface Presenter{
        fun filterListPicker(list: MutableList<PickerModel>, keyword: String)
    }

    interface View {
        fun onFiltering(list: MutableList<PickerModel>)
    }
}