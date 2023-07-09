package com.example.marsad.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marsad.R
import com.example.marsad.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        buildViews()
        binding.closeAlarmFab.setOnClickListener {
            mediaPlayer.release()
            mediaPlayer.stop()
            finish()
        }
    }

    private fun buildViews() {
        intent.extras?.let {
            binding.alarmTitleTv.text = it.getString(getString(R.string.alert_title_key))
            binding.alarmDescriptionTv.text =
                it.getString(getString(R.string.alert_description_key))
        }
    }
}