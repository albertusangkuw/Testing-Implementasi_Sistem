package com.tubes.emusic.ui.component

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.library.LibraryFragment


class ArtistProfileFragment : Fragment() {
    private lateinit var rv_listAlbums : RecyclerView
    private lateinit var bundleData : Thumbnail
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artist_profile, container, false)
        bundleData = (context as MainActivity).getBundle(this)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        rv_listAlbums = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_album_artist)

        rv_listAlbums.setHasFixedSize(true)

        view.findViewById<TextView>(R.id.tv_artist_name).setText(bundleData.title)
        //Glide.with(view.context).load(bundleData.urlImage).into(DrawableViewBackgroundTarget())

        Glide.with(this).load(bundleData.urlImage).into(object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?) {
                view.findViewById<LinearLayout>(R.id.background_profile_artist).setBackground(resource)
            }
        })

        showRecyclerListPublicPlaylists()

        return view
    }

    private fun showRecyclerListPublicPlaylists() {
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail("sfhskwe", "LibraryListMusic", "LibraryListMusic", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg", "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail("3242ddwe", "LibraryListMusic", "LibraryListMusic", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg", "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        list.add(hero2)
        list.add(hero2)
        list.add(hero2)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)
        rv_listAlbums.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listAlbums.adapter = listHeroAdapter
    }
}