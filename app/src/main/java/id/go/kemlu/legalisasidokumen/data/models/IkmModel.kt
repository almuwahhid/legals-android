package id.go.kemlu.legalisasidokumen.data.models

import java.io.Serializable

data class IkmModel (var sangat_puas: Int,
                     var puas: Int,
                     var tidak_puas: Int,
                     var total: Int): Serializable