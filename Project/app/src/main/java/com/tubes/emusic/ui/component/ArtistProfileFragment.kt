package com.tubes.emusic.ui.component

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Playbar
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.helper.MappingHelper
import com.tubes.emusic.ui.library.LibraryFragment
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ArtistProfileFragment : Fragment() {
    private lateinit var rv_listAlbums : RecyclerView
    private lateinit var bundleData : Thumbnail
    private val list = ArrayList<Thumbnail>()
    private var followers : String = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artist_profile, container, false)
        bundleData = (context as MainActivity).getBundle(this)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listAlbums = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_album_artist)

        rv_listAlbums.setHasFixedSize(true)

        view.findViewById<TextView>(R.id.tv_artist_name).setText(bundleData.title)
        //Glide.with(view.context).load(bundleData.urlImage).into(DrawableViewBackgroundTarget())

        Glide.with(this).load(bundleData.urlImage).into(object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?) {
                view.findViewById<LinearLayout>(R.id.background_profile_artist).setBackground(resource)
            }
        })
        val followingstatus = view.findViewById<ToggleButton>(R.id.btn_following)
        var statusFollowers = true
        followingstatus.setOnCheckedChangeListener { buttonView,
             isChecked ->
             if(followingstatus.isChecked){
                 Log.e("Abstract", "Following user")
                 if(statusFollowers) {
                     GlobalScope.launch {
                         UserApi.addFollowingUser(
                             MainActivity.currentUser?.iduser!!,
                             bundleData.id!!
                         )
                     }
                 }else{
                     statusFollowers = true
                 }
             }else{
                 Log.e("Abstract", "Unfollowing user")
                 GlobalScope.launch {
                     UserApi.deleteFollowingUser(MainActivity.currentUser?.iduser!!,bundleData.id!!)
                 }
             }
        }
        GlobalScope.launch {
            val detail = UserApi.getDetailSingleUser(bundleData.id!!)
            delay(1000)
            AlbumApi.searchAlbumByArtits(bundleData.id!!)
            if(detail?.datafollowers != null) {
                followers = "" + detail.datafollowers.size
                if(UserApi.searchIdUserArray(MainActivity.currentUser?.iduser!!,detail.datafollowers)){
                    if(!followingstatus.isChecked) {
                        statusFollowers = false
                        followingstatus.isChecked = true
                    }
                }
            }
        }
        showRecyclerListPublicPlaylists()
        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                Thread.sleep(2000)
                updatePage(view)
            }
        }
        handler.postDelayed(run,(1000).toLong())

        view.findViewById<android.widget.Button>(R.id.btn_artist_shuffleplay).setOnClickListener {
            if(!list.isNullOrEmpty()){
                var randomAlbum =list.random()
                val rawAlbum = MappingHelper.mapListAlbumToArrayList(
                        MainActivity.db?.queryCustomById(
                                randomAlbum.id.toString(),
                                DatabaseContract.AlbumDB.ID,
                                DatabaseContract.AlbumDB.TABLE_NAME
                        )
                )
                var mapData = rawAlbum.get(0).listsong!!
                var addOn = "NoCover"
                var desc = "" +  MainActivity.getUserByIdUser(rawAlbum.get(0).iduser)?.username

                var listSong = ArrayList<Thumbnail>()
                for (i in mapData){
                    val thumb = Thumbnail(i.idsong.toString(), "Music", addOn, HTTPClientManager.host + "album/" + i.idalbum + "/photo", i.title,"" + desc)
                    listSong.add(thumb)
                }
                Playbar.mapData = listSong
                Playbar.parentData = randomAlbum

                val args = (context as MainActivity).setBundle(listSong.random())
                val ldf = PlaybarFragment()
                ldf.setArguments(args)
                (context as MainActivity).openFragment(ldf)
            }
        }

        return view
    }

    private fun showRecyclerListPublicPlaylists() {

        var mapData : List<AlbumData> = MappingHelper.mapListAlbumToArrayList(
            MainActivity.db?.queryCustomById(bundleData.id!!, DatabaseContract.AlbumDB.IDUSER, DatabaseContract.AlbumDB.TABLE_NAME)
        )
        for(i in mapData){
           val thumb = Thumbnail(i.idalbum.toString(), "Album", "", HTTPClientManager.host + "album/" + i.idalbum + "/photo",  i.namealbum, i.daterelease.substring(0, 4))
            list.add(thumb)
        }
        rv_listAlbums.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listAlbums.adapter = listHeroAdapter
    }

    private fun updatePage(view: View){
        view.findViewById<TextView>(R.id.tv_number_followers_artist).setText("" + followers + " followers")
    }
}