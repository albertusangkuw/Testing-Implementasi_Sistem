package com.tubes.emusic.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.ui.component.UserProfileFragment
import com.tubes.emusic.ui.playbar.PlaybarFragment

class LibraryFragment : Fragment() {
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
        return view
    }


}