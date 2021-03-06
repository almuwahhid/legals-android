package id.go.kemlu.legalisasidokumen.data.models

import java.io.Serializable

data class PembayaranToVerifModel (val ol_id : Int,
                                   val request_no : String,
                                   val group_no : String,
                                   val request_date : String,
                                   val dtm_req : String,
                                   val user_id : Int,
                                   val is_paid : String,
                                   val country_id : Int,
                                   val institution_name : String,
                                   val verification_no : String,
                                   val dtm_verification : String,
                                   val trans_no : String,
                                   val dtm_trans : String,
                                   val price_code : String,
                                   val doc_qty : String,
                                   val price : String,
                                   val total_price : String,
                                   val rekening : String,
                                   val bank : String,
                                   val bank_branch : String,
                                   val nik : String,
                                   val request_name : String,
                                   val request_address : String,
                                   val request_phone : String,
                                   val is_true : Boolean,
                                   val image_path : String,
                                   val ikm : String,
                                   val ikm_det : String,
                                   val ikm_comment : String,
                                   val status_id : Int,
                                   val status_detail : String,
                                   val source : Int,
                                   val create_date : String,
                                   val is_open : String,
                                   val open_by : String,
                                   val open_date : String,
                                   val country_code : String,
                                   val country_name : String,
                                   val payment_request_date : String,
                                   val uniq_code : String,
                                   val not_oke : String,
                                   val is_oke : String,
                                   val note : String,
                                   val payment_image : String): Serializable