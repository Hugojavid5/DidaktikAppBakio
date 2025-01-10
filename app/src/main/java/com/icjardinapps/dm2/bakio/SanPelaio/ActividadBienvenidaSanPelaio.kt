package com.icjardinapps.dm2.bakio.SanPelaio
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.icjardinapps.dm2.bakio.R


class ActividadBienvenidaSanPelaio : AppCompatActivity(){
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_san_pelaio)

        val btnPlay = findViewById<Button>(R.id.btn_play)
        val btnPause = findViewById<Button>(R.id.btn_pause)
        val btnRestart = findViewById<Button>(R.id.btn_restart)
        val btnPlayGame = findViewById<Button>(R.id.btn_play_game)

        // Initialize MediaPlayer with audio file
        mediaPlayer = MediaPlayer.create(this, R.raw.sanpelaioaudioa)
        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }

        btnPlayGame.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            // Navigate to the game activity
            val intent = Intent(this, ActividadSanPelaio::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
