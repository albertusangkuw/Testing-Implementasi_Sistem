package com.tubes.emusic.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tubes.emusic.R
import com.tubes.emusic.api.UserApi
import com.tubes.emusic.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrasiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_new_account, container, false)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Sign In Triggered")
            (context as LoginActivity).openFragment(LoginFragment())
        }

        view.findViewById<Button>(R.id.btn_create).setOnClickListener {
            val email = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_email).editText?.text.toString()
            val password = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_create_password).editText?.text.toString()

            if(password.length >= 8 && email.length >= 1){
                GlobalScope.launch(Dispatchers.IO) {
                    val status =  UserApi.insertUser(User("", "", email, "", "", "", 1), password)
                    if(status){
                        Log.e("Abstract", "Success Registrasi back to login")
                        (context as LoginActivity).openFragment(LoginFragment())
                    }else{
                        Log.e("Abstract", "Failed API Registrasi")
                    }
                }
            }else{
                Log.e("Abstract", "Not satisfied requiredment")
            }
        }
        return view
    }
}