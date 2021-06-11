package com.example.appclinica.ui.exercise

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import com.example.appclinica.R

class VideoActivity : AppCompatActivity() {

    lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.videoView)

        val bundle = intent.extras
        val dato = bundle?.getString("url")

        val uri = Uri.parse(dato.toString())

        videoView.setMediaController(
            MediaController(this)
        )

        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }
}