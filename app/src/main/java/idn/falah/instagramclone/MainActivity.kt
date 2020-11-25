package idn.falah.instagramclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import idn.falah.instagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // mendeklarasikan binding viewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewbinding inflater
        val inflater = layoutInflater
        // set viewbinding untuk activity_main.xml
        binding = ActivityMainBinding.inflate(inflater)
        // setContentView diisi dengan binding.root
        setContentView(binding.root)

        // Definisikan id NavHostFragment yang ada di activity_main.xml,
        // idnya itu fragment_container
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment? ?: return
        val navController = host.navController

        // navView itu adalah id dari bottomNavigation
        binding.navView.setupWithNavController(navController)
    }
}