package com.tubes.emusic.ui.playbar

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tubes.emusic.R
import com.tubes.emusic.entity.Music
import com.tubes.emusic.entity.Playbar
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [PlaybarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaybarFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_playbar, container, false)

        val dummyMusic = Music("1","1","The Days","https://mir-s3-cdn-cf.behance.net/project_modules/disp/e705a534040642.56c2206da74cf.jpg","http://18.140.59.14:8081/music/17/data","Avicii","Electronic")

        Glide.with(view.context).load( dummyMusic.urlAlbumPhoto).into(view.findViewById<ImageView>(R.id.img_cover))

        var playBtn : ImageButton = view.findViewById(R.id.img_pause_icon)

        //var beginTimeTv : TextView = view.findViewById(R.id.tv_begin_time)
        //var endTimeTv : TextView = view.findViewById(R.id.tv_end_time)

        view.findViewById<TextView>(R.id.tv_title_playbar_song).setText(dummyMusic.title)
        view.findViewById<TextView>(R.id.tv_artist_playbar_name).setText(dummyMusic.artistName)
        //view.findViewById<com.google.android.material.slider.Slider>(R.id.icon_seekbar_progress).setPadding(0,0,0,0)
       /*
       var shuflleBtn : ImageButton = view.findViewById(R.id.img_shuffle_icon)
       var previousBtn : ImageButton = view.findViewById(R.id.img_previous_icon)
       var nextBtn : ImageButton = view.findViewById(R.id.img_next_icon)
       var repeatBtn : ImageButton = view.findViewById(R.id.img_repeat_icon)
       */

       if(Playbar.mediaPlayer == null){
           startMusic(dummyMusic.urlsongs)
       }else{
           //Stop Already Played Music
           Playbar.mediaPlayer!!.release()
           startMusic(dummyMusic.urlsongs)
       }

          // beginTimeTv.setText(mediaPlayer.currentSeconds)
           //endTimeTv.setText(mediaPlayer.seconds)

       // Start the media player
       playBtn.setOnClickListener{
           if(Playbar.mediaPlayer != null) {
               if (Playbar.pause) {
                   Playbar.mediaPlayer!!.seekTo(Playbar.mediaPlayer!!.currentPosition)
                   Playbar.mediaPlayer!!.start()
                   Playbar.pause = false
                   playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                   Log.e("Abstract", "Resuming Playbar")
               } else if (Playbar.isPlaying) {
                   Playbar.mediaPlayer!!.pause()
                   Playbar.pause = true
                   playBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                   Log.e("Abstract", "Pausing Playbar")
               }
               Playbar.mediaPlayer!!.setOnCompletionListener {
                   playBtn.isEnabled = true
               }
           }
      }

      return  view
   }

    private fun startMusic(url :String?){
        Playbar.mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
        Playbar.isPlaying = true
        Log.e("Abstract", "Playbar is running")
    }
    // Creating an extension property to get the media player time duration in seconds
    val MediaPlayer.seconds:Int
        get() {
            return this.duration / 1000
        }
    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds:Int
        get() {
            return this.currentPosition/1000
        }



}