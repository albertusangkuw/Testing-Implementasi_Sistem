package com.tubes.emusic.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.SessionApi
import com.tubes.emusic.api.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment  : Fragment() {
    var mGoogleSignInClient: GoogleSignInClient? = null
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
            val password = view.findViewById<com.google.android.material.textfield.TextInputLayout>(
                R.id.text_input_password_login
            ).editText?.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                var status = false
                status  = SessionApi.loginUser(email, password)
                Log.e("Abstract", "Testing login  : " + status)
                if(status){
                    Log.e("Abstract", "Redirect Mainactivity")
                    MainActivity.currentUser = UserApi.getSingleUser(email)
                    startActivity(Intent(context, MainActivity::class.java))
                    //(context as MainActivity).startMainActivity()
                }else{
                    Log.e("Abstract", "Failed Login")
                }
            }


        }
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(view.context, gso);

        view.findViewById<com.google.android.gms.common.SignInButton>(R.id.sign_in_button).setOnClickListener{
            when (view.getId()) {
                R.id.sign_in_button -> signIn()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(view?.context)

        if(account!=null) {
            //val intent = Intent(this, UserProfile::class.java)
            //startActivity(intent)
        }
        //updateUI(account)
    }
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.e("Abstract", "Account " + account?.email)
            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }

}