package com.tubes.emusic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail


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
}