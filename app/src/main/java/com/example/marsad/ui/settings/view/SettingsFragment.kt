package com.example.marsad.ui.settings.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.marsad.R
import com.example.marsad.core.utils.ARABIC
import com.example.marsad.core.utils.AR_LANG_TAG
import com.example.marsad.core.utils.CELSIUS
import com.example.marsad.core.utils.ENGLISH
import com.example.marsad.core.utils.EN_lANG_TAG
import com.example.marsad.core.utils.LANGUAGE_KEY
import com.example.marsad.core.utils.MAP
import com.example.marsad.core.utils.METER_SEC
import com.example.marsad.core.utils.NOTIFICATIONS_KEY
import com.example.marsad.core.utils.PREFERRED_LOCATION_METHOD_KEY
import com.example.marsad.core.utils.SPEED_UNIT_KEY
import com.example.marsad.core.utils.TEMPERATURE_UNIT_KEY
import com.example.marsad.core.utils.getCurrentLocale
import com.example.marsad.core.utils.viewModelFactory
import com.example.marsad.data.repositories.UserPrefsRepository
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val settingsViewModel: SettingsViewModel by viewModels(ownerProducer = { requireActivity() }) {
        viewModelFactory { SettingsViewModel(UserPrefsRepository.getInstance(requireActivity())) }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        PreferenceManager.setDefaultValues(requireContext(), R.xml.root_preferences, true)
        settingsViewModel.prefs.value.run {
            setInitialLocationMethod(preferredLocationMethod)
            setInitialLanguage(language)
        }
    }

    private fun setInitialLocationMethod(method: String) {
        val locationMethodPreference =
            findPreference<ListPreference>(PREFERRED_LOCATION_METHOD_KEY) as ListPreference
        locationMethodPreference.value = method
    }

    private fun setInitialLanguage(language: String) {
        val languagePreference = findPreference<ListPreference>(LANGUAGE_KEY) as ListPreference
        languagePreference.value = language

    }

    override
    fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val value = sharedPreferences?.getString(key, null)
        when (key) {

            PREFERRED_LOCATION_METHOD_KEY -> {
                if (value == MAP) settingsViewModel.resetFirstUse()
                settingsViewModel.updatePreferredLocationMethod(value!!)
                navigateToHome()
            }

            NOTIFICATIONS_KEY -> {}

            TEMPERATURE_UNIT_KEY -> {
                settingsViewModel.updatePreferredTempUnit(
                    value ?: CELSIUS
                )
            }

            SPEED_UNIT_KEY -> {
                settingsViewModel.updatePreferredSpeedUnit(
                    value ?: METER_SEC
                )
            }

            LANGUAGE_KEY -> {
                updateLocale(
                    value ?: getCurrentLocale().language
                )
                settingsViewModel.updateLanguage(value ?: ENGLISH)
            }
        }
    }

    private fun navigateToHome() {
        findNavController()
            .navigate(SettingsFragmentDirections.actionsSettingsToHome())
    }

    private fun updateLocale(language: String) {
        val langTag = when (language) {
            ARABIC -> AR_LANG_TAG
            ENGLISH -> EN_lANG_TAG
            else -> getCurrentLocale().toLanguageTag()
        }
        val locale = Locale(langTag)
        Locale.setDefault(locale)
        resources.configuration.setLocale(locale)

        val locales = LocaleListCompat.forLanguageTags(langTag)
        AppCompatDelegate.setApplicationLocales(locales)
    }


    override fun onStart() {
        super.onStart()
        preferenceScreen
            .sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen
            .sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(this)

    }

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }
}