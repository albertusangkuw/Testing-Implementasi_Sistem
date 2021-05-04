package com.tubes.emusic.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.tubes.emusic.helper.MappingHelper
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import com.tubes.emusic.ui.component.UserProfileFragment
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment()  {

    private lateinit var rv_bigmusicalbum_recently : RecyclerView
    private lateinit var rv_bigmusicalbum_recommended : RecyclerView
    private lateinit var rv_bigmusicalbum_new_release : RecyclerView

    private val listRecently = ArrayList<Thumbnail>()
    private val listRecommendedPlaylist = ArrayList<Thumbnail>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile_home).setOnClickListener {
            (context as MainActivity).openFragment(UserProfileFragment())
        }

        rv_bigmusicalbum_recently = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_big_musicalbum_recently)
        rv_bigmusicalbum_recently.setHasFixedSize(true)
        rv_bigmusicalbum_recommended = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_big_musicalbum_recommended)
        rv_bigmusicalbum_recommended.setHasFixedSize(true)
        rv_bigmusicalbum_new_release = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_big_musicalbum_new_release)
        rv_bigmusicalbum_new_release.setHasFixedSize(true)

        //Change greetings
        view.findViewById<TextView>(R.id.text_home_greeting).setText(dayGreeting())
        view.findViewById<Button>(R.id.btn_testing_playbar).setOnClickListener {
            Log.e("Abstract", "Playbar excecuted")
            val ldf = PlaybarFragment()
            // Menaruh data ke dalam fragment yang dikirim
            (context as MainActivity).openFragment(ldf)
        }
        testingButton(view)


        laucherWaiting()

        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                if(MainActivity.currentUser?.urlphotoprofile != "" &&  MainActivity.currentUser?.iduser != null) {
                    Glide.with(view.context).load(HTTPClientManager.host + "users/" + MainActivity.currentUser?.iduser + "/photo").into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile_home))
                }else{
                    Glide.with(view.context).load("https://www.jobstreet.co.id/en/cms/employer/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png").into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_profile_home))
                }
                Thread.sleep((200* 20).toLong())
                showRecyclerListBigMusicAlbum()
            }
        }

        handler.postDelayed(run,(100).toLong())
        return view
    }



    private fun laucherWaiting(){
        GlobalScope.launch{
            delay(1000)
            Log.e("Abstract", "Get Usernow : " + MainActivity.currentUser?.username)
            if(MainActivity.currentUser == null){
                return@launch
            }
            var dataHistory =  UserApi.getHistory(MainActivity.currentUser?.iduser!!)
            Thread.sleep(500)
            if (dataHistory != null) {
                for(i in dataHistory){
                    if(i.type == 1){
                        //Id Playlist
                        val data = ( PlaylistApi.getPlaylistById(i.idlist ))?.data?.get(0)
                        if(data != null) {
                            if(data.urlimagecover == ""){
                                data.urlimagecover ="http://18.140.59.14/static/playlist_default.jpg"
                            }
                            val thumb = Thumbnail(id= data.idplaylist.toString(), type="Playlist",
                                    addOn = "",  urlImage = data.urlimagecover,
                                    title = data.nameplaylist,
                                    description = " followers")
                            val res  = listRecently.find { s -> s.id == thumb.id && s.type == "Playlist" }
                            if(res != null ){
                                continue
                            }
                            listRecently.add(thumb)
                        }
                    }else if(i.type == 2){
                        //Album
                        val data = ( AlbumApi.getAlbumById(i.idlist ))?.data?.get(0)
                        if(data != null) {
                            val thumb = Thumbnail(id= data.idalbum.toString(), type="Album",
                                    addOn = "",  urlImage = HTTPClientManager.host + "album/" + i.idlist + "/photo",
                                    title = data.namealbum,
                                    description = " followers")
                            val res  = listRecently.find { s -> s.id == thumb.id && s.type == "Album" }
                            if(res != null ){
                                continue
                            }
                            listRecently.add(thumb)
                        }
                    }
                }
            }
        }
    }

    private fun showRecyclerListBigMusicAlbum() {
        rv_bigmusicalbum_recently.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        rv_bigmusicalbum_recommended.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        rv_bigmusicalbum_new_release.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)


        val hasilDBAlbum = MappingHelper.mapListAlbumToArrayList(MainActivity.db?.queryAll(DatabaseContract.AlbumDB.TABLE_NAME))
        hasilDBAlbum.shuffle()
        var trackAlbum = 0
        for( i in hasilDBAlbum){
            if(trackAlbum > 14){
                break
            }
            if(listRecently != null ){

                val res  = listRecently.find { s -> s.id == i.idalbum.toString() }
                if(res != null ){
                    continue
                }
                listRecommendedPlaylist.add(
                        Thumbnail(i.idalbum.toString(),"Album","",HTTPClientManager.host + "album/" +  i.idalbum + "/photo",  i.namealbum," followers"
                        ))
                trackAlbum++
            }
        }
        //listRecommendedPlaylist.shuffle()

        Log.e("Abstract", "hasil Album " + listRecommendedPlaylist)
        Log.e("Abstract", "hasil history " + listRecently)

        val listRecentlyAdapter = ListBigMusicAlbumAdapter(listRecently)
        val listRecommendedAdapter = ListBigMusicAlbumAdapter(listRecommendedPlaylist)
        rv_bigmusicalbum_recently.adapter = listRecentlyAdapter
        rv_bigmusicalbum_recommended.adapter = listRecommendedAdapter
        rv_bigmusicalbum_new_release.adapter = ListMusicAlbumAdapter(listRecently)
    }

    fun dayGreeting(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = current.format(formatter)
        val hour: Int? =  formatted.toInt()
        var status = "Good Day"
        if (hour != null) {
            if(hour > 18){
                status ="Good evening"
                Log.e("Abstract", "Good evening " + formatted)
            }else if(hour > 12){
                status ="Good afternoon"
                Log.e("Abstract", "Good afternoon " + formatted)
            }else{
                status ="Good morning"
                Log.e("Abstract", "Good morning " + formatted)
            }
        }
        return status
    }

    fun testingButton(view: View){
        view.findViewById<Button>(R.id.btn_testing_login).setOnClickListener {
            Log.e("Abstract", "Playbar excecuted")

            GlobalScope.launch(Dispatchers.IO) {
                //Example API Session API
                Log.e("Abstract", "Testing login  : " +  SessionApi.loginUser("albertus@gmail.com","albertus"))
                Log.e("Abstract", "Status Cookie : " + SessionApi.checkCookie())
                //SessionApi.logoutUser()
                //Log.e("Abstract", "Status Cookie : " + SessionApi.checkCookie())

                //Example User  API
                Log.e("Abstract", "Status Get Detail User : " + UserApi.getDetailSingleUser("f68baf7d0802d43f43018dabc72c2cf4"))
                Log.e("Abstract", "Status Search User : " + UserApi.searchUser("ber"))
                //Log.e("Abstract", "Status Following User : " + UserApi.addFollowingUser("c2d14ceeb62257774d6ea9d74025d085","db219aa2ce3a3dc57fadbb26a538cb37"))
                Log.e("Abstract", "Status Delete Following User : " + UserApi.deleteFollowingUser("c2d14ceeb62257774d6ea9d74025d085","db219aa2ce3a3dc57fadbb26a538cb37"))
                val user =  UserApi.getSingleUser("BolXYSAang@gmail.com")
                if(user != null){
                    Log.e("Abstract", "Status username dulu : " + user.username)
                    user.username="New Bolang from Android"
                    Log.e("Abstract", "Status Update User : " + UserApi.updateUser(user,""))
                }

                //Example Playlist
                Log.e("Abstract", "Status Get Detail Playlist : " + PlaylistApi.getPlaylistById(3))
                Log.e("Abstract", "Status Add Song Playlist : " + PlaylistApi.deleteSongPlaylist(3,11))
                Log.e("Abstract", "Status FollowingPlaylist : " + PlaylistApi.deleteFollowingPlaylist(3,"10d00d99a73f94be3710f8f0de952e17"))

                // Example Music
                Log.e("Abstract", "Status Get Detail Music : " + MusicApi.getMusicById(11))

                // Example Album
                Log.e("Abstract", "Status Get Detail Album : " + MusicApi.getMusicById(1))
            }

        }
    }
}
