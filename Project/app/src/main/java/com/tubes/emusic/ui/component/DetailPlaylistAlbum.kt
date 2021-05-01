package com.tubes.emusic.ui.component

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Playbar.Companion.mapData
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.helper.MappingHelper.mapListAlbumToArrayList
import com.tubes.emusic.helper.MappingHelper.mapListPlaylistSongToArrayList
import com.tubes.emusic.helper.MappingHelper.mapListUserToArrayString
import com.tubes.emusic.helper.MappingHelper.mapListsongToArrayList
import com.tubes.emusic.ui.home.HomeFragment
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private var statusFollowers = true
    private var isOwnlist = false
    private var description= ""
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

        val followingstatus = view.findViewById<ToggleButton>(R.id.tb_following_playlist)

        followingstatus.setOnCheckedChangeListener { buttonView,
                                                     isChecked ->
            if(followingstatus.isChecked){
                Log.e("Abstract", "Following user")
                if(statusFollowers) {
                    GlobalScope.launch {
                        AlbumApi.addFollowingAlbum(
                            bundleData.id!!.toInt(),
                            MainActivity.currentUser?.iduser!!
                        )
                    }
                }else{
                    statusFollowers = true
                }
            }else{
                Log.e("Abstract", "Unfollowing user")
                GlobalScope.launch {
                    AlbumApi.deleteFollowingAlbum(bundleData.id!!.toInt(),MainActivity.currentUser?.iduser!!)
                }
            }
        }


        view.findViewById<TextView>(R.id.tv_detail_playlist_title).setText(bundleData.title)

        Glide.with(view.context).load(bundleData.urlImage).into(view.findViewById<ImageView>(R.id.img_detail_musicalbum_photo))
        rv_music = view.findViewById<RecyclerView>(R.id.rv_item_music)
        rv_music.setHasFixedSize(true)

        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                Thread.sleep(1000)
                if (statusFollowers == false) {
                    view.findViewById<ToggleButton>(R.id.tb_following_playlist).toggle()
                }
                view.findViewById<TextView>(R.id.tv_detail_playlist_description).setText(description)
                if(isOwnlist){
                    view.findViewById<ToggleButton>(R.id.tb_following_playlist).setVisibility(View.GONE)
                }
            }
        }
        handler.postDelayed(run,(3000).toLong())

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        laucherWaiting(view)
        getStatusFollowing()
        showRecyclerListMusic()
    }
    fun getStatusFollowing(){
        GlobalScope.launch {
            if(bundleData.type == "Album"){
                val rawAlbum = AlbumApi.getAlbumById(bundleData.id!!.toInt())?.data?.get(0)
                description = UserApi.getSingleUserByID(rawAlbum?.iduser!!)?.username!!
                if(rawAlbum?.userfollowing != null) {
                    for (i in rawAlbum.userfollowing!!) {
                            if(i.iduser == MainActivity.currentUser?.iduser){
                                statusFollowers = false
                            }
                    }
                }


            }else if(bundleData.type =="Playlist"){
                val rawPlaylist = PlaylistApi.getPlaylistById(bundleData.id!!.toInt())?.data?.get(0)
                description = UserApi.getSingleUserByID(rawPlaylist?.iduser!!)?.username!!
                if(rawPlaylist.iduser ==MainActivity.currentUser?.iduser ){
                    isOwnlist = true
                }
                if(rawPlaylist?.userfollowing != null) {
                    for (i in rawPlaylist.userfollowing!!) {
                        if(i.iduser == MainActivity.currentUser?.iduser){
                            statusFollowers = false
                        }
                    }
                }
            }
        }

    }
    fun laucherWaiting(view: View){
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
            desc = "" +  MainActivity.getUserByIdUser(rawAlbum.get(0).iduser)?.username
        }else if(bundleData.type == "Playlist" && id != null ){
            val rawPlaylist =   mapListPlaylistSongToArrayList(
                    MainActivity.db?.queryById(id, DatabaseContract.PlaylistDB.TABLE_NAME)
            )
            mapData = rawPlaylist.get(0).listsong!!
        }

        for (i in mapData){
            if(bundleData.type == "Playlist"){
                desc = MainActivity.getMusicByIdSong(i.idsong!!.toInt())?.artistName!!
            }
            val thumb = Thumbnail(i.idsong.toString(), "Music", addOn, HTTPClientManager.host + "album/" + i.idalbum + "/photo", i.title,"" + desc)
            list.add(thumb)
        }
        Playbar.mapData = list
        Playbar.parentData = bundleData
    }

    private fun showRecyclerListMusic() {
        rv_music.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_music.adapter = listHeroAdapter
    }


}