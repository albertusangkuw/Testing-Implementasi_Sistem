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
import com.tubes.emusic.ui.home.ListBigMusicAlbumAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [MyAlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAlbumFragment : Fragment() {
    private lateinit var rv_listAlbum : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my_album, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listAlbum = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_album_music)
        rv_listAlbum.setHasFixedSize(true)
        showRecyclerListAlbum()
        return view
    }

    private fun showRecyclerListAlbum() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "sfhskwe","LibraryListAlbum","LibraryListAlbum", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "3242ddwe","LibraryListAlbum", "LibraryListAlbum", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        list.add(hero2)
        list.add(hero2)
        list.add(hero2)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        rv_listAlbum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listAlbum.adapter = listHeroAdapter
    }
}