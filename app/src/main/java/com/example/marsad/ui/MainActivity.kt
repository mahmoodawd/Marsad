package com.example.marsad.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.marsad.R
import com.example.marsad.databinding.ActivityMainBinding
import com.example.marsad.ui.utils.UnitsUtils
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val actionBar = supportActionBar
        actionBar?.apply {
            title = UnitsUtils.getTempRepresentation(applicationContext, 36.0)
            setHomeAsUpIndicator(R.drawable.ic_list_24)
            setDisplayHomeAsUpEnabled(true)
        }

        Navigation.findNavController(this, R.id.nav_host_fragment).apply {
            NavigationUI.setupWithNavController(activityMainBinding.navigatorLayout, this)
        }
    }

   /* private fun setLocale() {
        Locale.setDefault(UnitsUtils.getCurrentLocale(this))
        val config = resources.configuration
        config.setLocale(Locale.getDefault())
        resources.updateConfiguration(config, resources.displayMetrics)
    }
*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityMainBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

