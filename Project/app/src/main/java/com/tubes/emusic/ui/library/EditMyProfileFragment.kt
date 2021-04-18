package com.tubes.emusic.ui.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tubes.emusic.R


/**
 * A simple [Fragment] subclass.
 * Use the [EditMyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditMyProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false)
        return view
    }


}