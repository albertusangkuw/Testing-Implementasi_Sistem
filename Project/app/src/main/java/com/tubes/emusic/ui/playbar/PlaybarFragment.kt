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
import com.tubes.emusic.MainActivity.Companion.getMusicByIdSong
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.entity.Music
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Playbar.Companion.mapData
import com.tubes.emusic.entity.Playbar.Companion.mediaPlayer
import com.tubes.emusic.entity.Playbar.Companion.sequenceNow
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ArtistProfileFragment
import com.tubes.emusic.ui.home.HomeFragment
import com.tubes.emusic.ui.library.LibraryFragment
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

        view.findViewById<ImageView>(R.id.img_down_icon).setOnClickListener {
            Log.e("Abstract", "Back to List Music Artist")
            (context as MainActivity).openFragment(HomeFragment())
        }


        var counterMap = 0
        for(i in mapData){
            if(i.id == bundleData.id){
                sequenceNow = counterMap
            }
            counterMap++
        }

        Log.e("Abstract", "Data : " + bundleData )

        //sequenceNow = 3
        shuffleMusic(view, mapData.get(sequenceNow))
        previousMusic(view, mapData.get(sequenceNow))
        playMusic(view, mapData.get(sequenceNow))
        nextMusic(view, mapData.get(sequenceNow))
        repeatMusic(view, mapData.get(sequenceNow))
        likeMusic(view, mapData.get(sequenceNow))

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
                val minuteStart = mediaPlayer!!.currentSeconds / 60
                val secondStart = mediaPlayer!!.currentSeconds % 60
                beginTime.text = "$minuteStart:$secondStart"

                val diff = Playbar.mediaPlayer!!.seconds - Playbar.mediaPlayer!!.currentSeconds
                val minuteEnd = diff / 60
                val secondEnd = diff % 60
                endTime.text = "$minuteEnd:$secondEnd"
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
            var shuffleBtn : ImageButton = view.findViewById(R.id.img_shuffle_icon)
            if (Playbar.shuffle == false) {
                Log.e("Abstract", "Shuffle Music ON")
                Playbar.shuffle = true
                mapData.shuffle()
                shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_on_24)
            } else {
                Log.e("Abstract", "Shuffle Music OFF")
                Playbar.shuffle = false
                mapData.sortBy { it.description }
                shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24)
            }
        }
    }

    private fun previousMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_previous_icon).setOnClickListener {
            Log.e("Abstract", "Previous Music is playing")
            if (sequenceNow == 0){
                sequenceNow = sequenceNow + (mapData.size-1)
                playMusic(view, mapData[sequenceNow])
            } else {
                sequenceNow = sequenceNow-1
                playMusic(view, mapData[sequenceNow])
            }
            mediaPlayer!!.start()
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
        view.findViewById<TextView>(R.id.tv_artist_playbar_name).setText(getMusicByIdSong(dummyMusic.idsong!!.toInt() )?.artistName)

        if(mediaPlayer == null){
            startMusic(dummyMusic.urlsongs)
        }else{
            //Stop Already Played Music
            mediaPlayer!!.release()
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
        mediaPlayer = MediaPlayer().apply {
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
            sequenceNow++
            if (sequenceNow > mapData.size-1){
                sequenceNow = 0
            }
            playMusic(view, mapData[sequenceNow])
            mediaPlayer!!.start()
        }
    }

    private fun repeatMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_repeat_icon).setOnClickListener {
            var repeatBtn : ImageButton = view.findViewById(R.id.img_repeat_icon)
            if (Playbar.repeat == false) {
                Log.e("Abstract", "Repeat Music ON")
                repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_on_24)
                view.findViewById<ImageButton>(R.id.img_next_icon).setOnClickListener {
                    Log.e("Abstract", "Next Music is playing")
                    playMusic(view, mapData[sequenceNow])
                }
                view.findViewById<ImageButton>(R.id.img_previous_icon).setOnClickListener {
                    Log.e("Abstract", "Previous Music is playing")
                    playMusic(view, mapData[sequenceNow])
                }
                Playbar.repeat = true
            } else {
                Log.e("Abstract", "Repeat Music OFF")
                previousMusic(view, mapData[sequenceNow])
                nextMusic(view, mapData[sequenceNow])
                repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24)
                Playbar.repeat = false
            }
            mediaPlayer!!.start()
        }
    }

    private fun likeMusic(view: View, bundleData: Thumbnail){
        view.findViewById<ImageButton>(R.id.img_like_icon).setOnClickListener {
            var likeBtn : ImageButton = view.findViewById(R.id.img_like_icon)
            if (Playbar.like == false) {
                Log.e("Abstract", "Like Music")
                likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                Playbar.like = true
            }else{
                Log.e("Abstract", "Unlike Music")
                likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Playbar.like = false
            }
        }
    }
}