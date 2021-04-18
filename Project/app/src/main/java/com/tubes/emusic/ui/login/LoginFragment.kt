package com.tubes.emusic.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R

class LoginFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        view.findViewById<TextView>(R.id.tv_reset_password).setOnClickListener {
            Log.e("Abstract", "Reset Password Triggered")
            (context as LoginActivity).openFragment(ResetPassword())
        }
        view.findViewById<TextView>(R.id.tv_signup).setOnClickListener {
            Log.e("Abstract", "Sign Up Triggered")
            (context as LoginActivity).openFragment(RegistrasiFragment())
        }
        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            Log.e("Abstract", "Sign In Triggered")
            (context as MainActivity).startMainActivity()
        }
        return view
    }

}