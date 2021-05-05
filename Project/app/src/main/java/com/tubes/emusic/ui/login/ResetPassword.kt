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
import com.tubes.emusic.api.SessionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        view.findViewById<Button>(R.id.btn_send_reset).setOnClickListener {
            Log.e("Abstract", "Reset Triggered")
            val email = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.id_reset_email).editText?.text.toString()
            if(email.length > 0 ) {
                GlobalScope.launch(Dispatchers.IO) {
                    SessionApi.forgotPasswrod(email)
                }
                (context as LoginActivity).openFragment(LoginFragment())
            }
        }
        return view
    }


}