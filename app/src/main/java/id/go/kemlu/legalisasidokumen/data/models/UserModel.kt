package id.go.kemlu.legalisasidokumen.data.models

data class UserModel (var user_id : String = "",
                      var user_nip : String = "",
                      var user_name : String = "",
                      var user_phone : String = "",
                      var user_email : String = "",
                      var user_role : String = "",
                      var user_photo_name : String = "",
                      var user_photo_url : String = "",
                      var user_type : String = "",
                      var user_type_code : Int = 0,
                      var access_token : String = "",
                      var refresh_token : String = "",
                      var token_iat : Int = 0,
                      var token_exp : Int = 0
                      ){
//    fun setUserModel(email: String, username: String, password: String, nama: String, tel: String, address : String) {
//        this.user_email = email
//        this.password = password
//        this.user_name = nama
//        this.username = username
//        this.user_phone = tel
//        this.user_address = address
//    }
}