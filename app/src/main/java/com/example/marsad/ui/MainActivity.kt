package com.example.marsad.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.marsad.R
import com.example.marsad.core.connectivity.ConnectivityMonitor
import com.example.marsad.core.connectivity.NetworkMonitor
import com.example.marsad.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val connectivityMonitor: NetworkMonitor by lazy {
        ConnectivityMonitor(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeNetworkState()
        setUpDrawer()
        setUpNavigation()
    }


    private fun observeNetworkState() {
        val snackbar =
            Snackbar.make(
                binding.root,
                R.string.no_internet_msg,
                Snackbar.LENGTH_INDEFINITE
            )

        val isOffline = connectivityMonitor.isOnline.map(Boolean::not).stateIn(
            lifecycleScope, SharingStarted.WhileSubscribed(5000L), false
        )
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                isOffline.collectLatest {
                    if (it) snackbar.show()
                    else snackbar.dismiss()

                }
            }
        }
    }

    private fun setUpDrawer() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val drawerListener = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(drawerListener)
        drawerListener.syncState()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.nav_view).apply {
            setupWithNavController(navController)

            setNavigationItemSelectedListener { menuItem ->
                Log.d(TAG, "setUpNavigation: Selected Item: $menuItem")
                navController.popBackStack(R.id.nav_graph, inclusive = true)
                navController.navigate(menuItem.itemId)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d(TAG, "onCreate: ${destination.label} ${destination.id}")
            supportActionBar?.title = destination.label
            changeDrawerLockMode(destination.id)
        }
    }

    private fun changeDrawerLockMode(destinationId: Int) {
        if (destinationId == R.id.homeFragment) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun onResume() {
        super.onResume()
        /*registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        Log.i(TAG, "onResume: Receiver Registered")*/
    }

    override fun onStop() {
        super.onStop()
//        unregisterReceiver(connectivityReceiver)
//        Log.i(TAG, "onStop: Receiver Unregistered")
//        networkObserver.shutdown()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
