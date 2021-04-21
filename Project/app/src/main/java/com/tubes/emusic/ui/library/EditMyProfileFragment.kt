package com.tubes.emusic.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.ui.login.LoginActivity
import com.tubes.emusic.ui.login.LoginFragment


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
        var view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        return view
    }


}