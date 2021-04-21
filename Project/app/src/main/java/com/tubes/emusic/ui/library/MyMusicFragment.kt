package com.tubes.emusic.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter

class MyMusicFragment : Fragment() {
    private lateinit var rv_listMusic : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my_music, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listMusic = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_my_music)
        rv_listMusic.setHasFixedSize(true)
        showRecyclerListMusic()
        return view
    }

    private fun showRecyclerListMusic() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "sfhskwe","MusicNoCover", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "3242ddwe","MusicNoCover", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        list.add(hero2)
        list.add(hero2)
        list.add(hero2)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        rv_listMusic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listMusic.adapter = listHeroAdapter
    }
}