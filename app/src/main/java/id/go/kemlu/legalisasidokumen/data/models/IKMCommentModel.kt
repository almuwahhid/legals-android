package id.go.kemlu.legalisasidokumen.data.models

data class IKMCommentModel (val intVerifBy : Int,
                            val request_id : Int,
                            val request_no : String,
                            val group_no : String,
                            val dtm_req : String,
                            val user_id : Int,
                            val is_paid : Boolean,
                            val request_name : String,
                            val request_address : String,
                            val request_phone : String,
                            val ikm : Int,
                            val ikm_det : String,
                            val ikm_comment : String,
                            val uniq_code : Int,
                            val not_oke : Int,
                            val is_oke : Int,
                            val note : String)