package com.tubes.emusic.ui.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tubes.emusic.R


/**
 * A simple [Fragment] subclass.
 * Use the [MyArtistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyArtistFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_my_artist, container, false)

        return view
    }


}