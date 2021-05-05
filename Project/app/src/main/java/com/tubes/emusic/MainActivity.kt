package com.tubes.emusic

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tubes.emusic.api.*
import com.tubes.emusic.db.DBManager
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Music
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.entity.User
import com.tubes.emusic.helper.MappingHelper
import com.tubes.emusic.helper.MappingHelper.mapListUserToArrayString
import com.tubes.emusic.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object{
        var loggedEmail : String = ""
        var currentUser : User? = null
        var db : DBManager? = null
        var detailUser : ResponseDetailUser? = null
        var playlistUser :ArrayList<PlaylistData>? = null
        var playlistFollowing :ArrayList<PlaylistData>? = null
        var albumUser : ArrayList<AlbumData>? = null
        var musicUser : ArrayList<MusicData>? = null
        var artistUser : ArrayList<User>? = null

        fun fillUserDetailDate(){
            GlobalScope.launch{
                val idUser = MainActivity.currentUser?.iduser
                Log.e("Abstract", "ID Usernow Library : " + idUser)
                detailUser = idUser?.let {
                    UserApi.getDetailSingleUser(it)
                }

                var playlistUserTemp :ArrayList<PlaylistData> = ArrayList<PlaylistData>()
                if(detailUser?.dataplaylistowned != null){
                    for(i in detailUser?.dataplaylistowned!!){
                        val playlist  = PlaylistApi.getPlaylistById(i.toInt())
                        delay(600)
                        if(playlist != null){
                            playlistUserTemp.add(playlist.data.get(0))
                        }
                    }
                }
                playlistUser = playlistUserTemp

                var playlistFollowingTemp :ArrayList<PlaylistData> = ArrayList<PlaylistData>()
                if(detailUser?.dataplaylistliked != null){
                    for(i in detailUser?.dataplaylistliked!!){
                        val playlist  = PlaylistApi.getPlaylistById(i.toInt())
                        delay(600)
                        if(playlist != null){
                            playlistFollowingTemp.add(playlist.data.get(0))
                        }
                    }
                }
                playlistFollowing = playlistFollowingTemp

                var albumUserTemp : ArrayList<AlbumData> =ArrayList<AlbumData>()
                if(detailUser?.dataalbum != null){
                    for(i in detailUser?.dataalbum!!) {
                        val album = AlbumApi.getAlbumById(i.toInt())
                        delay(600)
                        if(album != null){
                            albumUserTemp.add(album.data.get(0))
                        }
                    }
                }
                albumUser= albumUserTemp

                var musicUserTemp : ArrayList<MusicData> =ArrayList<MusicData>()
                if(detailUser?.datalikedsong != null){
                    for(i in detailUser?.datalikedsong!!) {
                        val music = MusicApi.getMusicById(i.toInt())
                        delay(600)
                        if(music != null){
                            musicUserTemp.add(music.data.get(0))
                        }
                    }
                }
                musicUser= musicUserTemp

                var artistUserTemp : ArrayList<User> =ArrayList<User>()
                if(detailUser?.datafollowingartis != null){
                    for(i in detailUser?.datafollowingartis!!) {
                        val artist = UserApi.getSingleUserByID(i)
                        delay(600)
                        if(artist != null){
                            artistUserTemp.add(artist)
                        }
                    }
                }
                artistUser = artistUserTemp
            }
        }
        fun getUserByIdUser(iduser: String?): User?{
            if(iduser == null){
                return User("","","","","","",0)
            }
            var apiUser : User? = null
            GlobalScope.launch{
                apiUser = UserApi.getSingleUserByID(iduser)
            }
            val regularuserDB  = MappingHelper.mapListUserToArrayString(db?.queryById(iduser, DatabaseContract.UserDB.TABLE_NAME))
            if(regularuserDB.iduser == ""){
                // Database cannot find the user
                // Return value from API
                Thread.sleep(510)
                return apiUser
            }else{
                return regularuserDB
            }
        }
        fun getUserDetailByIdUser(iduser: String?): ResponseDetailUser?{
            if(iduser == null){
                return ResponseDetailUser(0,"","", ArrayList<String>(),ArrayList<String>(),ArrayList<String>(),ArrayList<String>(),ArrayList<String>(),ArrayList<String>(),ArrayList<String>())
            }
            var apiUser : ResponseDetailUser? = null
            GlobalScope.launch {
                apiUser = UserApi.getDetailSingleUser(iduser)
            }
            if(detailUser == null){
                Thread.sleep(600)
                detailUser = apiUser
                return  detailUser
            }else{
                return detailUser
            }
        }
        fun getMusicByIdSong(idsong: Int) :Music?{
            var apiUser : MusicData? = null
            var responseMusicData: MusicData? = null
            GlobalScope.launch{
                responseMusicData = MusicApi.getMusicById(idsong)?.data?.get(0)
            }
            val songDB  = MappingHelper.mapListsongToArrayList(db?.queryById( idsong.toString(), DatabaseContract.SongDB.TABLE_NAME))
            if(songDB.isEmpty()){
                // Database cannot find the user
                // Return value from API
                Thread.sleep(510)
                val nameArtis = getUserByIdUser(searchAlbumIdAlbum(responseMusicData?.idalbum)?.iduser)?.username
                //Thread.sleep(1030)
                return  Music(responseMusicData?.idsong.toString(), responseMusicData?.idalbum.toString(), responseMusicData?.title, HTTPClientManager.host + "album/" + responseMusicData?.idalbum + "/photo"  , HTTPClientManager.host +"/music/"+ responseMusicData?.urlsongs + "/data" , nameArtis, responseMusicData?.genre)
            }else{
                apiUser = songDB.get(0)
                val nameArtis = getUserByIdUser(searchAlbumIdAlbum(apiUser?.idalbum)?.iduser)?.username
                return  Music(apiUser?.idsong.toString(), apiUser?.idalbum.toString(), apiUser.title, HTTPClientManager.host + "album/" +apiUser?.idalbum + "/photo"  , apiUser.urlsongs, nameArtis,apiUser?.genre)
            }
        }
        fun synchronizeObject(){
            GlobalScope.launch{
                currentUser = UserApi.getSingleUser(loggedEmail)
                fillUserDetailDate()
            }
        }
        fun searchAlbumIdAlbum(idalbum: Int?) : AlbumData?{
            if(idalbum == null ) {
                return  AlbumData(0, "", "", "", "", "", null, null)
            }
            var reponseAlbumData : AlbumData? = null
            GlobalScope.launch {
                reponseAlbumData = AlbumApi.getAlbumById(idalbum)?.data?.get(0)
            }
            val albumresult  = MappingHelper.mapListAlbumToArrayList( db?.queryById(idalbum.toString(), DatabaseContract.AlbumDB.TABLE_NAME))
            if(albumresult.isEmpty()){
                Thread.sleep(510)
                return reponseAlbumData
            }else{
                return albumresult[0]
            }
        }
    }
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBManager(this)
        db?.open()
        val savedUser = checkUserSavedLogin()
        GlobalScope.launch{
            if(savedUser.iduser != ""){
                SessionApi.forgotPasswrod(savedUser.email!!)
                loggedEmail = savedUser.email!!
            }
            Thread.sleep(310)
            var statusCookie  = SessionApi.checkCookie()
            Thread.sleep(310)
            //statusCookie  = true
            if(!statusCookie){
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            currentUser = UserApi.getSingleUser(loggedEmail)

            synchronizeObject()
            Log.e("Abstract", "Testing User Now  : " +  currentUser?.iduser )
        }
        Thread.sleep(1000)
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

    private fun checkUserSavedLogin(): User{
        var loggedUser = mapListUserToArrayString(db?.queryCustomById("1", DatabaseContract.UserDB.LOGGED,DatabaseContract.UserDB.TABLE_NAME ))
        return loggedUser
    }



}