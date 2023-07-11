package com.example.marsad.ui

import android.Manifest
import android.app.StatusBarManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PatternMatcher
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.marsad.R
import com.example.marsad.databinding.ActivityMainBinding
import com.example.marsad.ui.utils.NetworkConnectivityObserver
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
        lifecycleScope.launch {
            networkConnectivityObserver.netState.collect {
                if (it != NetworkConnectivityObserver.Status.Available) {
                    Snackbar.make(binding.root, getString(R.string.no_internet_msg), Snackbar.LENGTH_LONG).show()
                }
            }
        }
        networkConnectivityObserver.getNetState()
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
}
