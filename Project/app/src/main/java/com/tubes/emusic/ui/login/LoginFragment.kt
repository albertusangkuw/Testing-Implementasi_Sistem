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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.SessionApi
import com.tubes.emusic.api.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment  : Fragment(), View.OnClickListener {
    companion object{
        lateinit  var signInButton : com.google.android.gms.common.SignInButton
        lateinit  var mGoogleSignInClient :GoogleSignInClient
        var RC_SIGN_IN = 1
    }
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
                    MainActivity.loggedEmail = email
                    //MainActivity.currentUser = UserApi.getSingleUser(email)
                    startActivity(Intent(context, MainActivity::class.java))
                    //(context as MainActivity).startMainActivity()
                }else{
                    //Toast.makeText(view.context, "Failed Login", Toast.LENGTH_LONG).show()
                    Log.e("Abstract", "Failed Login")
                }
            }


        }
        signInButton = view.findViewById<SignInButton>(R.id.signin);
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso);

        signInButton.setOnClickListener(this)

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            //Toast.makeText(this.context, "SIgn in success", Toast.LENGTH_LONG).show()

            GlobalScope.launch {
                if(SessionApi.signwithGoogle(account!!)){
                    Log.w(TAG, "Success sign with google " + account?.email + " -" + account?.id  + " ,"  + account?.displayName )
                    Log.e("Abstract", "Redirect Main Activity")
                    MainActivity.loggedEmail = account?.email!!
                    //MainActivity.currentUser = UserApi.getSingleUser(account?.email!!)
                    startActivity(Intent(context, MainActivity::class.java))
                }
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this.context, "Sign in with Google Failed ("    + e.statusCode + ")", Toast.LENGTH_LONG).show()
            //  updateUI(null)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.signin -> signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}