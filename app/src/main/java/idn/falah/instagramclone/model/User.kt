package idn.falah.instagramclone.model

/**
 * Created by Imam Fahrur Rofi on 21/08/2020.
 */

// isi var dari data class User harus persis sama dengan yang ada di realtime database Firebase
data class User(
    var Bio: String = "",
    var email: String = "",
    var fullname: String = "",
    var image: String = "",
    var uid: String = "",
    var username: String = ""
)