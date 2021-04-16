package com.tubes.emusic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_library)).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // To remove title in apps
        supportActionBar?.hide()
    }
    public fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager
        transaction.beginTransaction()
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
}