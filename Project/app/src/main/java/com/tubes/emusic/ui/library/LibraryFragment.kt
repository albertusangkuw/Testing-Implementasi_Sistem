package com.tubes.emusic.ui.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
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
          var playlistUser :ArrayList<PlaylistData> = ArrayList<PlaylistData>()
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

        laucherWaiting(view)


        return view
    }

    private fun laucherWaiting(view: View){
        GlobalScope.launch{
            delay(1000)
            Log.e("Abstract", "Get Usernow : " + MainActivity.currentUser?.username)
            val idUser = MainActivity.currentUser?.iduser
            Log.e("Abstract", "ID Usernow : " + idUser)

            val detailUser = idUser?.let { UserApi.getDetailSingleUser(it) }
            delay(1000)
            if(detailUser != null){
                if(detailUser.dataplaylist.size > 0) {
                    view.findViewById<TextView>(R.id.tv_detail_playlist).setText("" + detailUser.dataalbum.size + " Playlists")
                    for(i in detailUser.dataplaylist){
                        val playlist  = PlaylistApi.getPlaylistById(i.toInt())
                        delay(1000)
                        if(playlist != null){
                            playlistUser.add(playlist.data.get(0))
                        }
                    }
                }

            }

        }
    }


}