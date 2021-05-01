package com.example.appclinica.ui.exercise

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.appclinica.R
import java.util.concurrent.TimeUnit

class MultimediaActivity : AppCompatActivity() {

    lateinit var txtCurrenTime: TextView
    lateinit var txtTotalTime: TextView
    lateinit var seekBar: SeekBar
    lateinit var imgPlayPause: ImageView
    lateinit var btnback: Button

    var handler: Handler = Handler()
    lateinit var mediaPlayer: MediaPlayer
    lateinit var mRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multimedia)

        val bundle = intent.extras
        val dato = bundle?.getString("url")

        getValores()

        btnback.setOnClickListener{
            finish()
        }


        var uri = Uri.parse(dato.toString())
        mediaPlayer = MediaPlayer.create(this,uri)

        mRunnable = object : Runnable {
            override fun run() {
                seekBar.setProgress(mediaPlayer.currentPosition)
                handler.postDelayed(this,500)
            }
        }

        val duration = mediaPlayer.duration

        val sDuration = miliSecondToTimer(duration)

        txtTotalTime.text = sDuration

        imgPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
                handler.removeCallbacks(mRunnable)
                imgPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            }else{
                mediaPlayer.start()
                seekBar.max = mediaPlayer.duration
                handler.postDelayed(mRunnable,0)
                imgPlayPause.setImageResource(R.drawable.ic_pause_circle)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }
                txtCurrenTime.text = miliSecondToTimer(mediaPlayer.currentPosition)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        mediaPlayer.setOnCompletionListener{
            imgPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            mediaPlayer.seekTo(0)
        }



    }

    private fun getValores() {
        txtCurrenTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        imgPlayPause = findViewById(R.id.imageView)
        seekBar = findViewById(R.id.playerseekBar)
        btnback = findViewById(R.id.btnPasoback)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
    }

    fun miliSecondToTimer(milesecond: Int):String{

        var timeString = ""
        var secondString = ""

        var hora = milesecond/(1000*60*60) as Int
        var minute = (milesecond%(1000*60*60))/(1000*60) as Int
        var second = (milesecond%(1000*60*60))%(1000*60)/1000 as Int

        if (hora>0){
            timeString = hora as String + ":"
        }
        if (second<10){
            secondString = "0"+ second
        }else{
            secondString = ""+ second
        }

        timeString = timeString + minute + ":" + secondString
        return timeString
    }

}