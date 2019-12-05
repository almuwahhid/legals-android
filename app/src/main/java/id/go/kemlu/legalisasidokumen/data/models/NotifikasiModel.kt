package id.go.kemlu.legalisasidokumen.data.models

import java.io.Serializable

data class NotifikasiModel (val intNotifid : Int,
                            val dtmNotif : String,
                            val strNotifTitle : String,
                            val strNotifDesc : String,
                            val intNotifTo : Int,
                            val intNotifFrom : Int,
                            val isNotifSync : String,
                            val strNotifType : String,
                            val intNotifTypeId : Int,
                            val strNotifGroupNo : String,
                            val strType : String,
                            val strUsername : String): Serializable