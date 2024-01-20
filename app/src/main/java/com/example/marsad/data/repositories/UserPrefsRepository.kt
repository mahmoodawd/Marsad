package com.example.marsad.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.marsad.core.utils.FAHRENHEIT
import com.example.marsad.core.utils.FIRST_USE_KEY
import com.example.marsad.core.utils.IS_HAVING_LOCATION_KEY
import com.example.marsad.core.utils.KELVIN
import com.example.marsad.core.utils.LANGUAGE_KEY
import com.example.marsad.core.utils.LAT_KEY
import com.example.marsad.core.utils.LON_KEY
import com.example.marsad.core.utils.MILES_HOUR
import com.example.marsad.core.utils.PREFERENCE_FILE_KEY
import com.example.marsad.core.utils.PREFERRED_LOCATION_METHOD_KEY
import com.example.marsad.core.utils.SPEED_UNIT_KEY
import com.example.marsad.core.utils.TEMPERATURE_UNIT_KEY
import com.example.marsad.core.utils.UNDEFINED
import com.example.marsad.core.utils.getCurrentLocale
import com.example.marsad.domain.models.MeasureUnit
import com.example.marsad.domain.models.UserPrefs
import com.example.marsad.domain.repositories.UserPrefsRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class UserPrefsRepository private constructor(context: Context) : UserPrefsRepositoryInterface {

    companion object {
        private const val TAG = "User Prefs Repo"
        private var instance: UserPrefsRepository? = null
        fun getInstance(context: Context): UserPrefsRepository {
            if (instance == null) {
                instance = UserPrefsRepository(context)
            }
            return instance as UserPrefsRepository
        }
    }

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCE_FILE_KEY, Context.MODE_PRIVATE
    )

    private val settingPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    override val prefs = MutableStateFlow(UserPrefs())


    init {
        prefs.update {
            sharedPreferences.run {
                it.copy(
                    lat = getDouble(LAT_KEY),

                    lon = getDouble(LON_KEY),

                    language = getString(LANGUAGE_KEY, null) ?: getCurrentLocale().language,

                    isFirstUse = getBoolean(FIRST_USE_KEY, true),

                    isHavingLocation = getBoolean(IS_HAVING_LOCATION_KEY, false),

                    preferredLocationMethod = getString(
                        PREFERRED_LOCATION_METHOD_KEY,
                        null
                    ) ?: UNDEFINED,

                    tempUnit = when (settingPrefs.getString(
                        TEMPERATURE_UNIT_KEY, null
                    )) {
                        KELVIN -> MeasureUnit.Kelvin

                        FAHRENHEIT -> MeasureUnit.Fahrenheit

                        else -> MeasureUnit.Celsius
                    },
                    speedUnit = when (settingPrefs.getString(SPEED_UNIT_KEY, null)) {

                        MILES_HOUR -> MeasureUnit.MilesPerHour
                        else -> MeasureUnit.MeterPerSecond
                    }
                )
            }
        }
    }


    override fun updateLatLon(lat: Double, lon: Double) {
        prefs.update {
            it.copy(
                lat = lat,
                lon = lon,
                isFirstUse = false,
                isHavingLocation = true
            )
        }
        Log.i(TAG, "updateUserPrefs: Done")
    }

    override fun updateLanguage(langTag: String) {
        prefs.update { it.copy(language = langTag) }
    }

    override fun updatePreferredLocationMethod(method: String) {
        prefs.update { it.copy(preferredLocationMethod = method) }
    }

    override fun updateTempUnit(unit: String) {
        Log.d(TAG, "updateTempUnit with $unit")
        prefs.update {
            it.copy(
                tempUnit = when (unit) {
                    FAHRENHEIT -> MeasureUnit.Fahrenheit
                    KELVIN -> MeasureUnit.Kelvin
                    else -> MeasureUnit.Celsius
                }
            )
        }
    }

    override fun updateSpeedUnit(unit: String) {

        prefs.update {
            it.copy(
                speedUnit = when (unit) {
                    MILES_HOUR -> MeasureUnit.MilesPerHour
                    else -> MeasureUnit.MeterPerSecond
                }
            )
        }
    }

    override fun resetFirstUse() =
        prefs.update { it.copy(isFirstUse = true) }

    override fun updatePrefs() {
        prefs.value.run {
            with(sharedPreferences.edit()) {
                putFloat(LAT_KEY, lat.toFloat())
                putFloat(LON_KEY, lon.toFloat())
                putBoolean(FIRST_USE_KEY, isFirstUse)
                putBoolean(IS_HAVING_LOCATION_KEY, isHavingLocation)
                putString(TEMPERATURE_UNIT_KEY, tempUnit.title)
                putString(PREFERRED_LOCATION_METHOD_KEY, preferredLocationMethod)
            }.apply()
        }
    }
}


fun SharedPreferences.getDouble(key: String, defaultValue: Float = 0.0f) =
    getFloat(key, defaultValue).toDouble()