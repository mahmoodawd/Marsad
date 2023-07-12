package com.example.marsad.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.marsad.R
import com.example.marsad.databinding.ActivityMainBinding
import com.example.marsad.utils.ConnectivityReceiver
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val connectivityReceiver by lazy {
        ConnectivityReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        ).also {
            binding.drawerLayout.addDrawerListener(it)
            it.syncState()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        Log.i("TAG", "onResume: Receiver Registered")
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
        Log.i("TAG", "onStop: Receiver Unregistered")
    }
}
