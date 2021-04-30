package com.tubes.emusic.ui.search

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import com.tubes.emusic.ui.component.ListUserAdapter

class ResultSearchFragment: Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_search_result, container, false)

        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                Thread.sleep(1510)
                showResultSong(view)
                showResultArtis(view)
                showResultAlbum(view)
            }
        }
        handler.postDelayed(run,(1000).toLong())

        return view
    }

    private fun showResultSong(view: View){
        var rv = view.findViewById<RecyclerView>(R.id.rv_item_relatedsong_musicalbum)
        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayMusicdata){
            list.add(Thumbnail(  i.idsong.toString() ,"Music" ,
                    "" ,
                    HTTPClientManager.host + "album/"+ i.idalbum.toString()  + "/photo",
                    "" +i.title ,
                    MainActivity.getMusicByIdSong(i.idsong!!.toInt())?.artistName
            ))
        }
        Playbar.mapData = list
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv.adapter = listHeroAdapter
    }

    private fun showResultArtis(view: View){
        var rv = view.findViewById<RecyclerView>(R.id.rv_item_relatedartist_musicalbum)
        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayArtistdata){
            list.add(Thumbnail(  i.iduser ,"Artist" ,
                    "" ,
                    HTTPClientManager.host + "users/"+ i.iduser + "/photo",
                    "" +i.username ,
                    ""
            ))
        }
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListUserAdapter(list)
        rv.adapter = listHeroAdapter
    }

    private fun showResultAlbum(view: View){
        var rv = view.findViewById<RecyclerView>(R.id.rv_item_relatedplaylist_musicalbum)
        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayAlbumdata){
            list.add(Thumbnail(  i.idalbum.toString() ,"Album" ,
                    "" ,
                    HTTPClientManager.host + "album/"+ i.idalbum + "/photo",
                    "" +i.namealbum ,
                    "" + MainActivity.getUserByIdUser(MainActivity.searchAlbumIdAlbum(i.idalbum)?.iduser)?.username
            ))
        }
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv.adapter = listHeroAdapter
    }

}