package id.go.kemlu.legalisasidokumen.app.buatdokumen.model

import android.net.Uri

data class DokumenUiModel (var document_type_id: String = "",
                           var document_name : String= "",
                           var certifier_number : String= "",
                           var certifier_name : String= "",
                           var certifier_institution : String= "",
                           var official_sinstitution_id : String= "",
                           var description : String= "",
                           var file : MutableList<String> = ArrayList(),
                           var file_uri : MutableList<Uri> = ArrayList()){
    public fun setForms(certifier_name : String, certifier_number : String, certifier_institution : String){
        this.certifier_name = certifier_name
        this.certifier_number = certifier_number
        this.certifier_institution = certifier_institution
    }

    public fun setFiles(file : MutableList<String>, file_uri : MutableList<Uri>){
        this.file_uri = file_uri
        this.file = file
    }
}