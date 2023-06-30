package com.example.marsad.ui.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.marsad.R
import com.example.marsad.ui.MainActivity
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        PreferenceManager.setDefaultValues(requireContext(), R.xml.root_preferences, true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.language_key)) {
            val currentLocale = sharedPreferences?.getString(key, "english")
            setLocale(currentLocale)
        } else if (key == getString(R.string.pref_location_method_key)) {
            val method = sharedPreferences?.getString(key, null)
            setPrefMethod(method)
        }
    }

    private fun setPrefMethod(method: String?) {
        when (method) {
            "gps" -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            "map" -> {
                val sharedPreferences = activity?.getSharedPreferences(
                    getString(R.string.preferences_file_key), Context.MODE_PRIVATE
                ) ?: return
                with(sharedPreferences.edit()) {
                    putBoolean(getString(R.string.first_use_key), true)
                    putString(getString(R.string.latitude_key), "0.0")
                    putString(getString(R.string.longitude_key), "0.0")
                        .apply()
                }
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun setLocale(currentLocale: String?) {
        val langTag = when (currentLocale) {
            "arabic" -> "ar-eg"
            else -> "en-us"
        }
        val locale = LocaleListCompat.forLanguageTags(langTag)
        AppCompatDelegate.setApplicationLocales(locale)
    }


    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)

    }

}