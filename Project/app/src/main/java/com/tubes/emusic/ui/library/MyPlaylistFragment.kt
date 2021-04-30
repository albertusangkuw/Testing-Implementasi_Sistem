package com.tubes.emusic.ui.library

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.MainActivity.Companion.laucherWaiting
import com.tubes.emusic.MainActivity.Companion.playlistFollowing
import com.tubes.emusic.MainActivity.Companion.playlistUser
import com.tubes.emusic.R
import com.tubes.emusic.api.PlaylistApi
import com.tubes.emusic.entity.Playlist
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MyPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPlaylistFragment : Fragment() {
    private lateinit var rv_listPlaylist : RecyclerView
    private var m_Text = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my_playlist, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }

        rv_listPlaylist = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_playlist_music)
        rv_listPlaylist.setHasFixedSize(true)
        showRecyclerListPlaylist()

        view.findViewById<RelativeLayout>(R.id.rl_add_playlist).setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Name of new playlist")
            val input = EditText(context)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialog,
                which -> m_Text = input.text.toString()
                if(m_Text.length > 0){
                    createNewPlaylist(m_Text)
                }
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()

        }

        return view
    }

    private fun createNewPlaylist(name : String){
        GlobalScope.launch {
            val newPlay = Playlist(
                    iduser= MainActivity.currentUser?.iduser,
                    namePlaylist = name,
                    urlImageCover = "",
                    ""
            )
            PlaylistApi.insertPlaylist(newPlay)
            delay(1000)
            laucherWaiting()
            delay(2000)
            (context as MainActivity).openFragment(MyPlaylistFragment())
        }
    }

    private fun showRecyclerListPlaylist() {
        val list = ArrayList<Thumbnail>()
        for(i in playlistUser){
            val thumb = Thumbnail(i.idplaylist.toString(), "Playlist", "LibraryListAlbum",  i.urlimagecover,
                    i.nameplaylist, MainActivity.getUserByIdUser(i.iduser)?.username)
            list.add(thumb)
        }
        for(i in playlistFollowing){
            val thumb = Thumbnail(i.idplaylist.toString(), "Playlist", "LibraryListAlbum",  i.urlimagecover,
                    i.nameplaylist, MainActivity.getUserByIdUser(i.iduser)?.username)
            list.add(thumb)
        }
        rv_listPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listPlaylist.adapter = listHeroAdapter
    }
}