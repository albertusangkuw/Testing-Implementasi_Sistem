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
import com.tubes.emusic.api.SessionApi
import com.tubes.emusic.api.UserApi
import com.tubes.emusic.db.DBManager
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.entity.User
import com.tubes.emusic.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object{
        public var currentUser : User? = null
        var db : DBManager? = null
    }
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBManager(this)
        db?.open()
        GlobalScope.launch{
            //Check is user can access api
            var statusCookie  = SessionApi.checkCookie()
            statusCookie  = true
            if(!statusCookie){
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            Log.e("Abstract", "Testing login  : " +  SessionApi.loginUser("albertus@gmail.com","albertus"))
            currentUser = UserApi.getSingleUser("Twice@gmail.com")
            Log.e("Abstract", "Testing User Now  : " +  currentUser?.iduser )
        }
        startMainActivity()
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
        Log.e("Abstract", "Main Activity Started")
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
        args.putString("addon", thumb.addOn)
        args.putString("title", thumb.title)
        args.putString("description", thumb.description)
        args.putString("urlImage", thumb.urlImage)
        return  args
    }
    public fun getBundle(f: Fragment): Thumbnail{
        return Thumbnail(f.arguments?.getString("id")
        ,f.arguments?.getString("type")
                ,f.arguments?.getString("addon")
        ,f.arguments?.getString("urlImage")
        ,f.arguments?.getString("title")
        ,f.arguments?.getString("desctiption"))
    }


}