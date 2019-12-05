package id.go.kemlu.legalisasidokumen.data.models

data class PengesahModel (val official_id : String,
                          val official_nip : String,
                          val official_name : String,
                          val official_position : String,
                          val oi_id : Int,
                          val oi_name : String,
                          val active_date : String,
                          val is_active : Boolean,
                          val level_id : Int,
                          val institution_id : String,
                          val user_id : Int,
                          val create_date : String,
                          val update_date : String)