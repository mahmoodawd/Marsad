package com.example.marsad.ui.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
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
        val languagePreference =
            findPreference<ListPreference>(getString(R.string.language_key)) as ListPreference
        languagePreference.setDefaultValue(Locale.getDefault().language)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.language_key)) {
            val currentLocale = sharedPreferences?.getString(
                key,
                Locale.getDefault().language
            )
            setLocale(currentLocale)
        } else if (key == getString(R.string.pref_location_method_key)) {
            val method = sharedPreferences?.getString(key, null)
            setPrefMethod(method)
        }
    }

    private fun setPrefMethod(method: String?) {
        when (method) {
            "gps" -> {
                restartWithNewMethod()
            }
            "map" -> {
                clearPrefs()
                restartWithNewMethod()
            }
        }
    }

    private fun restartWithNewMethod() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun clearPrefs() {
        val sharedPreferences = activity?.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        ) ?: return
        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.first_use_key), true)  //To View map
            putString(getString(R.string.latitude_key), "0.0")
            putString(getString(R.string.longitude_key), "0.0")
                .apply()
        }
    }

    private fun setLocale(currentLocale: String?) {
        val langTag = when (currentLocale) {
            "arabic" -> "ar-eg"
            "english" -> "en-us"
            else -> Locale.getDefault().toLanguageTag()
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