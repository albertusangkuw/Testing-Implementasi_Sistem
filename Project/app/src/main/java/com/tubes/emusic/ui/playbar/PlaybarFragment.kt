package com.tubes.emusic.ui.playbar

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.tubes.emusic.R
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [PlaybarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaybarFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_playbar, container, false)

       var playBtn : ImageButton = view.findViewById(R.id.img_pause_icon)
       var pauseBtn : ImageButton = view.findViewById(R.id.img_repeat_icon)

       // Start the media player
       playBtn.setOnClickListener{
           val url = "https://archive.org/details/NathanielConteDemoSongs/01_Al_Sol.mp3" // your URL here
           if(pause){
               mediaPlayer.seekTo(mediaPlayer.currentPosition)
               mediaPlayer.start()
               pause = false
           }else{
/*
               mediaPlayer = MediaPlayer().apply {
                   setAudioAttributes(
                           AudioAttributes.Builder()
                                   .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                   .setUsage(AudioAttributes.USAGE_MEDIA)
                                   .build()
                   )
                   setDataSource(url)
                   prepareAsync() // might take long! (for buffering, etc)
                   start()
               }
*/              playMp3(url)
               Log.e("Abstract", "Playbar is running")

           }
           playBtn.isEnabled = false
           pauseBtn.isEnabled = true

           mediaPlayer.setOnCompletionListener {
               playBtn.isEnabled = true
               pauseBtn.isEnabled = false
           }
       }
       // Pause the media player
       pauseBtn.setOnClickListener {
           if(mediaPlayer.isPlaying){
               mediaPlayer.pause()
               pause = true
               playBtn.isEnabled = true
               pauseBtn.isEnabled = false


           }
       }
       return  view
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

    fun playMp3(_link: String?) {
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.setDataSource(_link)
            //mediaPlayer.prepare(); // might take long! (for buffering, etc)   //@@
            mediaPlayer.prepareAsync()
        } catch (e: IllegalArgumentException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: SecurityException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            // TODO Auto-generated catch block///
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

}