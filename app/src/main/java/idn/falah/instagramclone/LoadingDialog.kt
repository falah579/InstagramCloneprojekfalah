package idn.falah.instagramclone

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import idn.falah.instagramclone.databinding.CustomBarBinding

/**
 * Created by Imam Fahrur Rofi on 18/08/2020.
 */
class LoadingDialog(private val activity: Activity) { // tambahkan constructor atau variabel activity

    // Gunakan viewbinding untuk mengakses custom_bar.xml
    private val inflater = LayoutInflater.from(activity)
    private val binding = CustomBarBinding.inflate(inflater)

    // buat variabel dialog tipe data AlertDialog
    private lateinit var dialog: AlertDialog

    // Buat fungsi startLoadingDialog untuk memulai loading dialog
    fun startLoadingDialog() {
        if (binding.root.parent == null) {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setView(binding.root)
            alertDialog.setCancelable(false)
            dialog = alertDialog.create()
        }
        dialog.show()
    }

    // buat fungsi dismissDialog untuk menutup loading Dialog
    fun dismissDialog() {
        dialog.dismiss()
    }
}