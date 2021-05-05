package com.tubes.emusic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import com.tubes.emusic.ui.component.ListUserAdapter

class ResultSearchFragment: Fragment()  {
    lateinit  var rvArtist: RecyclerView
    lateinit  var rvRegUser: RecyclerView
    lateinit  var rvAlbum: RecyclerView
    lateinit  var rvSong : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_search_result, container, false)

        rvArtist = view.findViewById<RecyclerView>(R.id.rv_item_relatedartist_musicalbum)
        rvAlbum = view.findViewById<RecyclerView>(R.id.rv_item_relatedplaylist_musicalbum)
        rvSong = view.findViewById<RecyclerView>(R.id.rv_item_relatedsong_musicalbum)
        rvRegUser = view.findViewById<RecyclerView>(R.id.rv_item_relatedreguser)
        showResultSong(view)
        showResultArtis(view)
        showResultAlbum(view)
        showResultRegularUser(view)
        return view
    }

    private fun showResultSong(view: View){
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
        rvSong.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rvSong.adapter = listHeroAdapter
    }

    private fun showResultArtis(view: View){
        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayArtistdata){
            list.add(Thumbnail(  i.iduser ,"Artist" ,
                    "" ,
                    HTTPClientManager.host + "users/"+ i.iduser + "/photo",
                    "" +i.username ,
                    ""
            ))
        }
        rvArtist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListUserAdapter(list)
        rvArtist.adapter = listHeroAdapter
    }

    private fun showResultRegularUser(view: View){
        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayRegularUserdata){
            if(i.urlphotoprofile == ""){
                i.urlphotoprofile = "https://www.jobstreet.co.id/en/cms/employer/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png"
            }else{
                i.urlphotoprofile = HTTPClientManager.host + "users/"+ i.iduser + "/photo"
            }
            list.add(Thumbnail(  i.iduser ,"User" ,
                    "" ,
                    i.urlphotoprofile ,
                    "" +i.username ,
                    ""
            ))
        }

        rvRegUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListUserAdapter(list)
        rvRegUser.adapter = listHeroAdapter
    }

    private fun showResultAlbum(view: View){

        val list = ArrayList<Thumbnail>()
        for(i in SearchFragment.arrayAlbumdata){
            list.add(Thumbnail(  i.idalbum.toString() ,"Album" ,
                    "" ,
                    HTTPClientManager.host + "album/"+ i.idalbum + "/photo",
                    "" +i.namealbum ,
                    "" + MainActivity.getUserByIdUser(MainActivity.searchAlbumIdAlbum(i.idalbum)?.iduser)?.username
            ))
        }
        rvAlbum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rvAlbum.adapter = listHeroAdapter
    }

}