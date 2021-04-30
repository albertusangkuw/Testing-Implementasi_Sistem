package com.tubes.emusic.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tubes.emusic.R


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val fragment: Fragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_activity_login,
                fragment,
                fragment.javaClass.simpleName
            )
            .addToBackStack(null).commit()
        supportActionBar?.hide()

    }



    fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager
        transaction.beginTransaction()
        .addToBackStack(null)
        .add(R.id.fragment_container_activity_login, fragment).commit()
    }
}