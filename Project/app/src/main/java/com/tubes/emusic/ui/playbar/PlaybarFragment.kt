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
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.entity.Music
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Playbar.Companion.mediaPlayer
import com.tubes.emusic.entity.Thumbnail
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [PlaybarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaybarFragment : Fragment() {
    private lateinit var seek_bar : SeekBar
    private lateinit var beginTime : TextView
    private lateinit var endTime : TextView

    companion object{
        var mapData = ArrayList<Thumbnail>()
        var sequenceNow = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_playbar, container, false)
        var bundleData = (context as MainActivity).getBundle(this)

        seek_bar = view.findViewById<SeekBar>(R.id.icon_seekbar_progress)
        beginTime = view.findViewById<TextView>(R.id.tv_begin_time)
        endTime = view.findViewById<TextView>(R.id.tv_end_time)

        sequenceNow = bundleData.addOn!!.toInt()
        Log.e("Abstract", "Data : " + bundleData )
        //sequenceNow = 3
        shuffleMusic(view, mapData.get(sequenceNow))
        previousMusic(view, mapData.get(sequenceNow))
        playMusic(view, mapData.get(sequenceNow))
          // beginTimeTv.setText(mediaPlayer.currentSeconds)
           //endTimeTv.setText(mediaPlayer.seconds)
        nextMusic(view, mapData.get(sequenceNow))
        repeatMusic(view, mapData.get(sequenceNow))

        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer!!.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        return  view
    }

    // Method to initialize seek bar and audio stats
    private fun initializeSeekBar() {
        seek_bar.max = mediaPlayer!!.seconds

        var handler: Handler = Handler()
        var runnable = object : Runnable {
            override fun run(){
                seek_bar.progress = mediaPlayer!!.currentSeconds
                beginTime.text = "${Playbar.mediaPlayer!!.currentSeconds} sec"
                val diff = Playbar.mediaPlayer!!.seconds - Playbar.mediaPlayer!!.currentSeconds
                endTime.text = "$diff sec"
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
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

    private fun shuffleMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_shuffle_icon).setOnClickListener {
            if (Playbar.shuffle == false) {
                Log.e("Abstract", "Shuffle Music ON")
                Playbar.shuffle = true
                mapData.shuffle()
            } else {
                Playbar.shuffle = false
                mapData.sortBy { it.addOn }
            }
        }
    }

    private fun previousMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_previous_icon).setOnClickListener {
            Log.e("Abstract", "Previous Music is playing")
        }
    }

    private fun playMusic(view: View, bundleData: Thumbnail){
        val dummyMusic = Music(
                bundleData.id,
                "",
                bundleData.title,
                bundleData.urlImage,
                HTTPClientManager.host +"/music/"+ bundleData.id  + "/data",
                "Avicii",
                bundleData.addOn
        )

        Glide.with(view.context).load( dummyMusic.urlAlbumPhoto).into(view.findViewById<ImageView>(R.id.img_cover))

        view.findViewById<TextView>(R.id.tv_title_playbar_song).setText(dummyMusic.title)
        view.findViewById<TextView>(R.id.tv_artist_playbar_name).setText(dummyMusic.artistName)

        if(Playbar.mediaPlayer == null){
            startMusic(dummyMusic.urlsongs)
        }else{
            //Stop Already Played Music
            Playbar.mediaPlayer!!.release()
            startMusic(dummyMusic.urlsongs)
            Playbar.pause = false
        }

        var playBtn : ImageButton = view.findViewById(R.id.img_pause_icon)
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
                    //playBtn.isEnabled = true
                    sequenceNow++
                    if(sequenceNow > mapData.size-1){
                        sequenceNow = 0
                    }
                    //playMusic(view, mapData[sequenceNow])
                    //Playbar.mediaPlayer!!.start()
                    startMusic(HTTPClientManager.host +"/music/"+ bundleData.id  + "/data")
                }
            }
        }
        initializeSeekBar()
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

    private fun nextMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_next_icon).setOnClickListener {
            Log.e("Abstract", "Next Music is playing")
        }
    }

    private fun repeatMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_repeat_icon).setOnClickListener {
            Log.e("Abstract", "Repeat Music ON")
        }
    }
}