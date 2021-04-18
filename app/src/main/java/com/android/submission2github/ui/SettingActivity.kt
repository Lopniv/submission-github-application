package com.android.submission2github.ui

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.submission2github.R
import com.android.submission2github.databinding.ActivitySettingBinding
import com.android.submission2github.receiver.AlarmReceiver
import com.android.submission2github.receiver.AlarmReceiver.Companion.ID_REPEATING
import com.android.submission2github.receiver.AlarmReceiver.Companion.REPEATING

class SettingActivity : AppCompatActivity() {

    private val time: String = "11:43"

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var b: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(b.root)
        alarmReceiver = AlarmReceiver()
        setCheckedChange()
    }

    private fun setCheckedChange() {
        val intent = Intent(this, AlarmReceiver::class.java)
        val alarmUp = (PendingIntent.getBroadcast(this, ID_REPEATING, intent, PendingIntent.FLAG_NO_CREATE) != null)
        b.switchReminder.isChecked = alarmUp
        b.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReceiver.setRepeatingAlarm(this, REPEATING, time, getString(R.string.message_reminder), b.root)
            } else {
                alarmReceiver.cancelAlarm(this, b.root)
            }
        }
    }
}