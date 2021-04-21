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
                Log.e("Abstract", "Testing login  : " +  SessionApi.loginUser("albertus@gmail.com","albertus"))
                Log.e("Abstract", "Status Cookie : " + SessionApi.checkCookie())
                //Mendapat User
                val loggedUser =  UserApi.getSingleUser("albertus@gmail.com")
                Log.e("Abstract", "Status username : " + loggedUser?.username)
                SessionApi.logoutUser()
                Log.e("Abstract", "Status Cookie : " + SessionApi.checkCookie())
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