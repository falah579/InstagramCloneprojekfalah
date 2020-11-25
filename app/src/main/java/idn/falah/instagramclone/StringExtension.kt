package idn.falah.instagramclone

import android.text.TextUtils

/**
 * Created by Imam Fahrur Rofi on 14/08/2020.
 */

// fungsi ini berlaku ke semua tipe data String
fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}