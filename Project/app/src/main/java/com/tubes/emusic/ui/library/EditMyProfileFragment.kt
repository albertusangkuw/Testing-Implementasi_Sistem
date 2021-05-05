package com.tubes.emusic.ui.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.Regularuser
import com.tubes.emusic.api.ResponseDetailUser
import com.tubes.emusic.api.ResponseUser
import com.tubes.emusic.api.UserApi
import com.tubes.emusic.entity.User
import com.tubes.emusic.ui.login.LoginActivity
import com.tubes.emusic.ui.login.LoginFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [EditMyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditMyProfileFragment : Fragment() {
   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false)
        view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.text_input_edit_name).setText(MainActivity.currentUser?.username)
        view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.text_input_edit_email_edit_profile).setText(MainActivity.currentUser?.email)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }

        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            Log.e("Abstract", "Button Save was Clicked")

            var email = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_email_edit_profile).getEditText()?.getText().toString()
            if (email != null){
                if(email.equals(MainActivity.currentUser?.email)){
                    email= ""
                }else{
                    MainActivity.loggedEmail = email
                }
                Log.e("Abstract", "Email : " + email)
            }else{
                email = ""
                Log.e("Abstract", "Email kosong")
            }

            var password = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_create_new_password).getEditText()?.getText().toString()
            if (password != null){
                Log.e("Abstract", "Password : " + password)
            }else{
                password = ""
                Log.e("Abstract", "Password kosong")
            }

            var username = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.text_input_name).getEditText()?.getText().toString()
            if (username != null){
                if(username.equals(MainActivity.currentUser?.username)){
                    username= ""
                }
                Log.e("Abstract", "Username : " + username)
            }else{
                username = ""
                Log.e("Abstract", "Username kosong")
            }
            GlobalScope.launch {
                val idUser = MainActivity.currentUser?.iduser
                Log.e("Abstract", "ID Usernow in Edit Profile : " + idUser)
                var updatedUser = User(email= email, username = username ,country = "", iduser = idUser,urlphotoprofile = "" , datejoin = "", categories = 2)
                UserApi.updateUser(updatedUser, password)
                delay(500)
                MainActivity.synchronizeObject()
            }
            (context as MainActivity).openFragment(LibraryFragment())
        }
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            Log.e("Abstract", "Button Cancel was Clicked")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        return view
    }
}