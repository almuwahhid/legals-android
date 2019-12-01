package id.go.kemlu.legalisasidokumen.app.login

data class LoginUiModel(var username : String,
                        var password : String,
                        val tipe: String = "LOGIN",
                        val appid : String = "123412341235")