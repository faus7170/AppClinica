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

        val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Fvideos%2Fy2mate.com%20-%20La%20Fuerza%20De%20La%20Naturaleza%20%201%20Festival%20de%20Cortometrajes%20con%20Tem%C3%A1tica%20Ambiental%202017_360p.mp4?alt=media&token=ec98ad3b-ffa4-42d3-ba98-b561b68119b9")

        videoView.setMediaController(
            MediaController(this)
        )

        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }
}