package com.tubes.emusic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.R
import com.tubes.emusic.api.MusicApi
import com.tubes.emusic.entity.Thumbnail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListGenreFragment : Fragment() {
    private lateinit var rv_genre_card : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_item_genre_list, container, false)

        rv_genre_card = view.findViewById(R.id.rv_card_genre_search)
        rv_genre_card.setHasFixedSize(true)

        showRecyclerCardGenreView()

        return view
    }

    private fun showRecyclerCardGenreView() {
        val list = ArrayList<Thumbnail>()
        val strGenre = arrayListOf<String>("Electronic", "Dance",        "Pop",        "K-Pop",        "Electronica",        "Ballad",        "R&B",        "J-Pop",        "Rap / Hip-hop",        "Aziatische Muziek / Electro",               "R&B / Soul")

        for(i in strGenre){
            val thumb = Thumbnail("", "Genre", "", "" , i, "")
            GlobalScope.launch {
                MusicApi.getMusicByGenre(i)
            }
            list.add(thumb)
        }
        rv_genre_card.layoutManager = GridLayoutManager(context,2)
        val cardViewAdapter = CardViewGenreAdapter(list)
        rv_genre_card.adapter = cardViewAdapter

    }

}