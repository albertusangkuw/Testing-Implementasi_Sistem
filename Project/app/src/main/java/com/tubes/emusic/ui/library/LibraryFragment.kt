package com.tubes.emusic.ui.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.UserProfileFragment
import com.tubes.emusic.ui.login.LoginActivity
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    companion object{
        var detailUser : ResponseDetailUser? = null
        var playlistUser :ArrayList<PlaylistData> = ArrayList<PlaylistData>()
        var albumUser : ArrayList<AlbumData> = ArrayList<AlbumData>()
        var musicUser : ArrayList<MusicData> = ArrayList<MusicData>()
        var artistUser : ArrayList<UserApi> = ArrayList<UserApi>()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_library, container, false)
        view.findViewById<RelativeLayout>(R.id.rl_my_profile).setOnClickListener {
            (context as MainActivity).openFragment(UserProfileFragment())
        }
        view.findViewById<TextView>(R.id.tv_edit_profile).setOnClickListener {
            (context as MainActivity).openFragment(EditMyProfileFragment())
        }
        view.findViewById<RelativeLayout>(R.id.rl_playlist_library).setOnClickListener {
            (context as MainActivity).openFragment(MyPlaylistFragment())
        }
        view.findViewById<RelativeLayout>(R.id.rl_album_library).setOnClickListener {
            (context as MainActivity).openFragment(MyAlbumFragment())
        }
        view.findViewById<RelativeLayout>(R.id.rl_music_library).setOnClickListener {
            (context as MainActivity).openFragment(MyMusicFragment())
        }
        view.findViewById<RelativeLayout>(R.id.rl_artist_library).setOnClickListener {
            (context as MainActivity).openFragment(MyArtistFragment())
        }
        view.findViewById<RelativeLayout>(R.id.rl_logout_library).setOnClickListener {
            GlobalScope.launch {
                if( SessionApi.logoutUser()){
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        laucherWaiting()

        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                //delay(2000)
                if(detailUser != null){
                    if(detailUser!!.dataplaylist != null) {
                        view.findViewById<TextView>(R.id.tv_detail_playlist).setText("" + detailUser!!.dataplaylist.size + " Playlists")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_playlist).setText("0 Playlists")
                    }

                    if(detailUser!!.dataalbum != null) {
                        view.findViewById<TextView>(R.id.tv_detail_album).setText("" + detailUser!!.dataalbum.size + " Albums")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_album).setText("0 Albums")
                    }
                }
            }
        }

        handler.postDelayed(run,(3000).toLong())
        return view
    }

    private fun laucherWaiting(){
        GlobalScope.launch{
            delay(1000)
            Log.e("Abstract", "Get Usernow : " + MainActivity.currentUser?.username)
            val idUser = MainActivity.currentUser?.iduser
            Log.e("Abstract", "ID Usernow : " + idUser)
            detailUser = idUser?.let { UserApi.getDetailSingleUser(it) }

            if(detailUser!!.dataplaylist != null){
                for(i in detailUser?.dataplaylist!!){
                    val playlist  = PlaylistApi.getPlaylistById(i.toInt())
                    delay(1000)
                    if(playlist != null){
                        playlistUser.add(playlist.data.get(0))
                    }
                }
            }

            if(detailUser!!.dataalbum != null){
                for(i in detailUser?.dataalbum!!) {
                    val album = AlbumApi.getAlbumById(i.toInt())
                    delay(1000)
                    if(album != null){
                        albumUser.add(album.data.get(0))
                    }
                }
            }
        }
    }
}