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
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.MusicApi
import com.tubes.emusic.api.PlaylistApi
import com.tubes.emusic.api.SessionApi
import com.tubes.emusic.api.UserApi
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.entity.User
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment()  {
    private lateinit var rv_bigmusicalbum : RecyclerView
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        rv_bigmusicalbum = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_big_musicalbum)
        rv_bigmusicalbum.setHasFixedSize(true)
        showRecyclerListBigMusicAlbum()

        //Change greetings
        view.findViewById<TextView>(R.id.text_home_greeting).setText(dayGreeting())
        view.findViewById<Button>(R.id.btn_testing_playbar).setOnClickListener {
            Log.e("Abstract", "Playbar excecuted")
            val ldf = PlaybarFragment()
            // Menaruh data ke dalam fragment yang dikirim
            (context as MainActivity).openFragment(ldf)
        }
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
        return view
    }

    private fun showRecyclerListBigMusicAlbum() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "sfhskwe","Album", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "3242ddwe","Album", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        rv_bigmusicalbum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        val listHeroAdapter = ListBigMusicAlbumAdapter(list)
        rv_bigmusicalbum.adapter = listHeroAdapter
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



}
