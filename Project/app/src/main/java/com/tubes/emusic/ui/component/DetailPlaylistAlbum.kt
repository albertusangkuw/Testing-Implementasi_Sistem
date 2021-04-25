package com.tubes.emusic.ui.component

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.helper.MappingHelper.mapListsongToArrayList
import com.tubes.emusic.ui.home.ListBigMusicAlbumAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DetailPlaylistAlbum.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailPlaylistAlbum : Fragment() {
    private lateinit var rv_music: RecyclerView
    private val list = ArrayList<Thumbnail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_detail_playlist_album, container, false)
        var bundleData = (context as MainActivity).getBundle(this)
        var tvdesc =  view.findViewById<TextView>(R.id.tv_detail_playlist_description)


        Glide.with(view.context).load(bundleData.urlImage).into(view.findViewById<ImageView>(R.id.img_detail_musicalbum_photo))
        rv_music = view.findViewById<RecyclerView>(R.id.rv_item_music)
        rv_music.setHasFixedSize(true)

        bundleData.id = "" + 2
        /*
        GlobalScope.launch{
            if (bundleData.type == "Album"){

            }else if(bundleData.type == "Playlist"){
                val rawResult = com.tubes.emusic.api.PlaylistApi.getPlaylistById(3)
                if(rawResult != null){
                    android.util.Log.e("Abstract", "Status Get Detail Playlist : " + rawResult.toString())
                    tvdesc.setText("" + rawResult.data[0].userfollowing.size + " FOLLOWERS"  )
                    rawResult.data[0].userfollowing.size
                }
            }


        }
        */

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        laucherWaiting()
        showRecyclerListMusic()
    }
    private fun laucherWaiting(){
        MainActivity.db?.open()
        val mapData = mapListsongToArrayList(MainActivity.db?.queryAll())
        for (i in mapData){
            val thumb = Thumbnail(i.idsong.toString(), "Music", "",  HTTPClientManager.host + "album/" + i.idalbum + "/photo", i.title,  i.genre)
            list.add(thumb)
        }
        /*
        val hero1 = Thumbnail( "song1","Music","", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Sunset ", "Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "song2","MusicNoCover", "", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Yes or Yes", "Twice")
        list.add(hero2)
        list.add(hero1)
       */
    }

    private fun showRecyclerListMusic() {
        rv_music.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_music.adapter = listHeroAdapter
    }


}