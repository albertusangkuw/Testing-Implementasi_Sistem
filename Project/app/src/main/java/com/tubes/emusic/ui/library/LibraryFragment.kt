package com.tubes.emusic.ui.library

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
import com.tubes.emusic.entity.User
import com.tubes.emusic.ui.component.UserProfileFragment
import com.tubes.emusic.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    companion object{
        var detailUser : ResponseDetailUser? = null
        var playlistUser :ArrayList<PlaylistData> = ArrayList<PlaylistData>()
        var albumUser : ArrayList<AlbumData> = ArrayList<AlbumData>()
        var musicUser : ArrayList<MusicData> = ArrayList<MusicData>()
        var artistUser : ArrayList<User> = ArrayList<User>()

        public fun laucherWaiting(){
            GlobalScope.launch{
                delay(1000)
                val idUser = MainActivity.currentUser?.iduser
                Log.e("Abstract", "ID Usernow Library : " + idUser)
                detailUser = idUser?.let { UserApi.getDetailSingleUser(it) }
                var playlistUserTemp :ArrayList<PlaylistData> =ArrayList<PlaylistData>()

                if(detailUser?.dataplaylistowned != null){
                    for(i in detailUser?.dataplaylistowned!!){
                        val playlist  = PlaylistApi.getPlaylistById(i.toInt())
                        delay(600)
                        if(playlist != null){
                            playlistUserTemp.add(playlist.data.get(0))
                        }
                    }
                }
                playlistUser = playlistUserTemp

                var albumUserTemp : ArrayList<AlbumData> =ArrayList<AlbumData>()
                if(detailUser?.dataalbum != null){
                    for(i in detailUser?.dataalbum!!) {
                        val album = AlbumApi.getAlbumById(i.toInt())
                        delay(600)
                        if(album != null){
                            albumUserTemp.add(album.data.get(0))
                        }
                    }
                }
                albumUser= albumUserTemp

                var musicUserTemp : ArrayList<MusicData> =ArrayList<MusicData>()
                if(detailUser!!.datalikedsong != null){
                    for(i in detailUser?.datalikedsong!!) {
                        val music = MusicApi.getMusicById(i.toInt())
                        delay(600)
                        if(music != null){
                            musicUserTemp.add(music.data.get(0))
                        }
                    }
                }
                musicUser= musicUserTemp

                var artistUserTemp : ArrayList<User> =ArrayList<User>()
                if(detailUser!!.datafollowingartis != null){
                    for(i in detailUser?.datafollowingartis!!) {
                        val artist = UserApi.getSingleUserByID(i)
                        delay(600)
                        if(artist != null){
                            artistUserTemp.add(artist)
                        }
                    }
                }
                artistUser = artistUserTemp

            }
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_library, container, false)
        if(MainActivity.currentUser != null) {
            view.findViewById<TextView>(R.id.tv_profile_name).setText(MainActivity.currentUser?.username)
            view.findViewById<TextView>(R.id.tv_email).setText(MainActivity.currentUser?.email)
            if(MainActivity.currentUser?.urlphotoprofile != ""){
                Glide.with(view.context).load(HTTPClientManager.host + "users/" + MainActivity.currentUser?.iduser + "/photo").into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile))
            }else{
                Glide.with(view.context).load("https://www.jobstreet.co.id/en/cms/employer/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png").into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile))
            }
        }else{
            Glide.with(view.context).load("https://www.jobstreet.co.id/en/cms/employer/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png").into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile))
        }

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
                if(detailUser != null){
                    if(detailUser!!.dataplaylistowned != null) {
                        var sums =  detailUser!!.dataplaylistowned.size
                        if(detailUser!!.dataplaylistowned != null){
                            sums += detailUser!!.dataplaylistliked.size
                        }
                        view.findViewById<TextView>(R.id.tv_detail_playlist).setText("" + sums + " Playlists")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_playlist).setText("0 Playlists")
                    }
                    if(detailUser!!.dataalbum != null) {
                        view.findViewById<TextView>(R.id.tv_detail_album).setText("" + detailUser!!.dataalbum.size + " Albums")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_album).setText("0 Albums")
                    }
                    if(detailUser!!.datalikedsong != null){
                        view.findViewById<TextView>(R.id.tv_detail_song).setText("" + detailUser!!.datalikedsong.size + " Liked Musics")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_song).setText("0 Liked Music")
                    }
                    if(detailUser!!.datafollowingartis != null){
                        view.findViewById<TextView>(R.id.tv_detail_artist).setText("" + detailUser!!.datafollowingartis.size + " Following")
                    }else{
                        view.findViewById<TextView>(R.id.tv_detail_artist).setText("0 Following")
                    }

                }
            }
        }

        handler.postDelayed(run,(3000).toLong())
        return view
    }


}