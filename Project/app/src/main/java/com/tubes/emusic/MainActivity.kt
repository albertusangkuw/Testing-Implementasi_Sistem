package com.tubes.emusic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.login.LoginActivity


class MainActivity : AppCompatActivity() {

    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusCookie = true
        if(!statusCookie){
            Log.e("Abstract", "Dipulangin lagi :(")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else {
            startMainActivity()
        }
    }

    public fun startMainActivity(){
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_library)).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        Log.e("Abstract", "Yeayyyyy masuk :>")
        // To remove title in apps
        supportActionBar?.hide()
    }
    public fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager
        transaction.beginTransaction().addToBackStack(null)
        .replace(R.id.nav_host_fragment, fragment).commit()
    }
    // Source https://stackoverflow.com/questions/31590919/replace-fragment-with-another-on-back-button
    override fun onBackPressed() {
        // note: you can also use 'getSupportFragmentManager()'
        val transaction = supportFragmentManager
        if (transaction.getBackStackEntryCount() === 0) {
            // No backstack to pop, so calling super
            super.onBackPressed()
        } else {
            transaction.popBackStack()
        }
    }

    public fun setBundle(thumb: Thumbnail) : Bundle{
        val args = Bundle()
        args.putString("id", thumb.id)
        args.putString("type", thumb.type)
        args.putString("title", thumb.title)
        args.putString("description", thumb.description)
        args.putString("urlImage", thumb.urlImage)
        return  args
    }
    public fun getBundle(f: Fragment): Thumbnail{
        return Thumbnail(f.arguments?.getString("id")
        ,f.arguments?.getString("type")
        ,f.arguments?.getString("urlImage")
        ,f.arguments?.getString("title")
        ,f.arguments?.getString("desctiption"))
    }


}