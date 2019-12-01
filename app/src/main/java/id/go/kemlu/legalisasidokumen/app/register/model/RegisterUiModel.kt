package id.go.kemlu.legalisasidokumen.app.register.model

data class RegisterUiModel (var user_name : String = "",
                            var user_nip : String = "",
                            var user_phone : String = "",
                            var user_email : String = "",
                            var user_password : String = "",
                            var user_re_password : String = "",
                            var user_photo : String = "",
                            var device_type : String = "ANDROID",
                            var user_type : String = "INTERNAL"
                            )