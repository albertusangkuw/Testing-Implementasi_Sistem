package com.tubes.emusic.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import com.tubes.emusic.ui.library.LibraryFragment

class GenrePlaylistAdapter: Fragment()  {
    private lateinit var rv_genre_music_list : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_genre, container, false)
        var bundleData = (context as MainActivity).getBundle(this)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(SearchFragment())
        }
        rv_genre_music_list = view.findViewById(R.id.rv_item_genre_music)
        rv_genre_music_list.setHasFixedSize(true)
        view.findViewById<TextView>(R.id.tv_genre_title).setText("Genre " + bundleData.title)
        showRecyclerViewGenreMusicView()
        return view
    }
    private fun showRecyclerViewGenreMusicView() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "song1","Music", "", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Sunset ", "Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "song2","Music", "", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Yes or Yes", "Twice")
        list.add(hero2)
        list.add(hero1)

        rv_genre_music_list .layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_genre_music_list .adapter = listHeroAdapter
    }
}