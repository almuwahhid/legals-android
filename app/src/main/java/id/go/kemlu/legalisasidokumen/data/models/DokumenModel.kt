package id.go.kemlu.legalisasidokumen.data.models

import java.io.Serializable

data class DokumenModel (val document_id : Int,
                         val request_no : String,
                         val document_type_id : Int,
                         val document_name : String,
                         val status_id : Int,
                         val status_detail : String,
                         val group_no : String,
                         val request_id : Int,
                         val document_legal_no : Int,
                         val document_legal_by : String,
                         val document_legal_by_instansi : String,
                         val document_legal_desc : String,
                         val date_time : String,
                         val images : MutableList<ImagesModel>): Serializable