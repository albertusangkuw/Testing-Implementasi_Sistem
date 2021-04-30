package com.tubes.emusic.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.MainActivity.Companion.albumUser
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [MyAlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAlbumFragment : Fragment() {
    private lateinit var rv_listAlbum : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my_album, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listAlbum = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_album_music)
        rv_listAlbum.setHasFixedSize(true)
        showRecyclerListAlbum()
        return view
    }

    private fun showRecyclerListAlbum() {
        val list = ArrayList<Thumbnail>()
        for(i in albumUser){
            val thumb = Thumbnail( i.idalbum.toString(),"Album","LibraryListAlbum",  HTTPClientManager.host + "album/"  + i.idalbum + "/photo" ,
                    i.namealbum, MainActivity.getUserByIdUser(MainActivity.searchAlbumIdAlbum(i.idalbum)?.iduser)?.username)
            list.add(thumb)
        }
        rv_listAlbum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listAlbum.adapter = listHeroAdapter
    }
}