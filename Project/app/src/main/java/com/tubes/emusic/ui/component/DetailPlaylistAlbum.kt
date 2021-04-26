package com.tubes.emusic.ui.component

import android.os.Bundle
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.helper.MappingHelper.mapListAlbumToArrayList
import com.tubes.emusic.helper.MappingHelper.mapListPlaylistSongToArrayList
import com.tubes.emusic.helper.MappingHelper.mapListsongToArrayList
import com.tubes.emusic.ui.home.HomeFragment
import com.tubes.emusic.ui.playbar.PlaybarFragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    private lateinit var bundleData : Thumbnail
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_detail_playlist_album, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(HomeFragment())
        }

        bundleData = (context as MainActivity).getBundle(this)
        var tvdesc =  view.findViewById<TextView>(R.id.tv_detail_playlist_description)


        Glide.with(view.context).load(bundleData.urlImage).into(view.findViewById<ImageView>(R.id.img_detail_musicalbum_photo))
        rv_music = view.findViewById<RecyclerView>(R.id.rv_item_music)
        rv_music.setHasFixedSize(true)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        laucherWaiting()
        showRecyclerListMusic()
    }
    private fun laucherWaiting(){
        var mapData : List<MusicData> = mapListsongToArrayList(MainActivity.db?.queryAll(DatabaseContract.SongDB.TABLE_NAME))
        var id = bundleData.id
        var addOn = ""
        var desc = ""
        if(bundleData.type == "Album" && id != null){
            val rawAlbum = mapListAlbumToArrayList(
                    MainActivity.db?.queryCustomById(
                            id,
                            DatabaseContract.AlbumDB.ID,
                            DatabaseContract.AlbumDB.TABLE_NAME
                    )
            )
            mapData = rawAlbum.get(0).listsong!!
            addOn = "NoCover"
            val timeRelease = rawAlbum.get(0).daterelease
            desc = "" +  timeRelease.substring(0, 4)

        }else if(bundleData.type == "Playlist" && id != null ){
            val rawPlaylist =   mapListPlaylistSongToArrayList(
                    MainActivity.db?.queryById(id, DatabaseContract.PlaylistDB.TABLE_NAME)
            )
            mapData = rawPlaylist.get(0).listsong!!
        }

        var iter = 0
        for (i in mapData){
            val thumb = Thumbnail(i.idsong.toString(), "Music", "" + iter, HTTPClientManager.host + "album/" + i.idalbum + "/photo", i.title, desc)
            list.add(thumb)
            iter++
        }
        PlaybarFragment.mapData = list
    }

    private fun showRecyclerListMusic() {
        rv_music.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_music.adapter = listHeroAdapter
    }


}