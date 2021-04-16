package com.tubes.emusic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail

class SearchFragment : Fragment() {
    private lateinit var rv_genre_card : RecyclerView
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        rv_genre_card = view.findViewById(R.id.rv_card_genre_search)
        rv_genre_card.setHasFixedSize(true)
        showRecyclerCardGenreView()
        return view
    }

    private fun showRecyclerCardGenreView() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail("", "", "" , "EDM", "")
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        rv_genre_card.layoutManager = GridLayoutManager(context,2)
        val cardViewAdapter = CardViewGenreAdapter(list)
        rv_genre_card.adapter = cardViewAdapter
    }
}