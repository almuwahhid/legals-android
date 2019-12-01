package id.go.kemlu.legalisasidokumen.data.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

data class PhotoModel (var url: String ="",
                       var base64: String = "",
                       var uri: Uri = Uri.parse("")){
    fun getBitmap(context: Context): Bitmap{
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
    }
}