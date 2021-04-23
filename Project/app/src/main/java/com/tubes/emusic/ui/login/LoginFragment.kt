package com.tubes.emusic.ui.login

import android.content.Intent
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
import com.tubes.emusic.api.SessionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

            val email = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_email_login).editText?.text.toString()
            val password = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_password_login).editText?.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                var status = false
                status  = SessionApi.loginUser(email, password)
                Log.e("Abstract", "Testing login  : " + status)
                if(status){
                    Log.e("Abstract", "Redirect Mainactivit")
                    startActivity(Intent(context,MainActivity::class.java))
                    //(context as MainActivity).startMainActivity()
                }else{
                    Log.e("Abstract", "Failed Login")
                }
            }


        }
        return view
    }

}