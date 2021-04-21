package com.tubes.emusic.ui.component

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
import com.tubes.emusic.ui.library.LibraryFragment

class ArtistProfileFragment : Fragment() {
    private lateinit var rv_listAlbums : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artist_profile, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listAlbums = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_album_artist)
        rv_listAlbums.setHasFixedSize(true)
        showRecyclerListPublicPlaylists()
        return view
    }

    private fun showRecyclerListPublicPlaylists() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "sfhskwe","LibraryListMusic", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "3242ddwe","LibraryListMusic", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        list.add(hero2)
        list.add(hero2)
        list.add(hero2)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        rv_listAlbums.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listAlbums.adapter = listHeroAdapter
    }
}