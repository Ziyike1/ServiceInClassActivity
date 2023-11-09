package edu.temple.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var timerService: TimerService
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if(isBound){
                timerService.start(60)
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if(isBound){
                timerService.pause()
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isBound){
                val binder = timerService.binder
                binder.stop()
            }
        }
    }
}