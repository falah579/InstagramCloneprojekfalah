package idn.falah.instagramclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import idn.falah.instagramclone.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    // variabel binding untuk viewbinding ActivityUpload
    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewbinding inisialiasi dimulai
        val inflater = layoutInflater
        binding = ActivityUploadBinding.inflate(inflater)
        setContentView(binding.root)
        // viewbinding inisialiasi selesai

    }
}