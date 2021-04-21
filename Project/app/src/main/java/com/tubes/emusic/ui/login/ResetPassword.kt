package com.tubes.emusic.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPassword : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Sign In Triggered")
            (context as LoginActivity).openFragment(LoginFragment())
        }
        return view
    }


}