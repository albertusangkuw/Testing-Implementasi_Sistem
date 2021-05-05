package com.tubes.emusic.ui.component

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.helper.MappingHelper
import com.tubes.emusic.ui.home.HomeFragment
import com.tubes.emusic.ui.library.LibraryFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private lateinit var rv_listPublicPlaylists : RecyclerView
    private lateinit var bundleData : Thumbnail
    private var playlists : String = ""
    private var followers : String = ""
    private var followings : String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        bundleData = (context as MainActivity).getBundle(this)
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }
        MainActivity.synchronizeObject()
        rv_listPublicPlaylists = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_user_music)
        rv_listPublicPlaylists.setHasFixedSize(true)
        view.findViewById<TextView>(R.id.tv_profile_name_user).setText(bundleData.title)
        var userFromApi = MainActivity.getUserByIdUser(bundleData.id)
        if(userFromApi?.urlphotoprofile == ""){
            bundleData.urlImage ="https://www.jobstreet.co.id/en/cms/employer/wp-content/plugins/all-in-one-seo-pack/images/default-user-image.png"
        }
        Glide.with(view.context).load(bundleData.urlImage).into(view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_foto_profile_user))

        val followingstatus = view.findViewById<ToggleButton>(R.id.btn_following_user)
        var statusFollowers = true

        //Get Detail to database
        GlobalScope.launch {
            var apiUser = UserApi.getDetailSingleUser(bundleData.id!!)
            delay(400)
            if(apiUser != null){
                if(apiUser.datafollowers  != null){
                    followers = "" + apiUser.datafollowers.size + ""
                }else{
                    followers = "" + 0 + ""
                }
                var sumfollowing = 0
                if(apiUser.datafollowingartis  != null){
                    sumfollowing += apiUser.datafollowingartis.size
                }
                if(apiUser.datafollowingregular  != null){
                    sumfollowing += apiUser.datafollowingregular.size
                }
                followings = "" + sumfollowing + ""
            }
        }

        //Check if user it self
        if(MainActivity.currentUser?.iduser == bundleData.id){
            //Remove following button
            followingstatus.setVisibility(View.GONE)
        }

        followingstatus.setOnCheckedChangeListener { buttonView,
                                                     isChecked ->
            if(followingstatus.isChecked){
                Log.e("Abstract", "Following user")
                if(statusFollowers) {
                    GlobalScope.launch {
                        UserApi.addFollowingUser(
                                MainActivity.currentUser?.iduser!!,
                                bundleData.id!!
                        )
                    }
                }else{
                    statusFollowers = true
                }
            }else{
                Log.e("Abstract", "Unfollowing user")
                GlobalScope.launch {
                    UserApi.deleteFollowingUser(MainActivity.currentUser?.iduser!!,bundleData.id!!)
                }
            }
        }

        GlobalScope.launch {
            val detail = UserApi.getDetailSingleUser(bundleData.id!!)
            delay(500)
            if(detail?.datafollowers != null) {
                followers = "" + detail.datafollowers.size
                for(i in detail.datafollowers ){
                    if(i == MainActivity.currentUser?.iduser!!){
                        if(!followingstatus.isChecked) {
                            statusFollowers = false
                            followingstatus.isChecked = true
                        }
                        break
                    }
                }
            }
        }

        val handler: Handler = Handler()
        val run = object : Runnable {
            override fun run() {
                Thread.sleep(2000)
                updatePage(view)
                showRecyclerListPublicPlaylists()
            }
        }
        handler.postDelayed(run,(1000).toLong())
        showRecyclerListPublicPlaylists()
        return view
    }

    private fun showRecyclerListPublicPlaylists() {
        var mapData : List<PlaylistData> = MappingHelper.mapListPlaylistSongToArrayList(
                MainActivity.db?.queryCustomById(bundleData.id!!, DatabaseContract.PlaylistDB.IDUSER, DatabaseContract.PlaylistDB.TABLE_NAME)
        )
        val list = ArrayList<Thumbnail>()
        for(i in mapData){
            if(i.urlimagecover == ""){
                i.urlimagecover = "http://18.140.59.14/static/playlist_default.jpg"
            }
            var follower: Int? = 0
            if(i.userfollowing.isNullOrEmpty()){
                follower = i.userfollowing?.size
            }
            val thumb = Thumbnail(i.idplaylist.toString(), "Playlist", "", i.urlimagecover,  i.nameplaylist,""+ follower + " followers")
            list.add(thumb)
        }
        playlists = "" + mapData.size + ""
        rv_listPublicPlaylists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val listHeroAdapter = ListMusicAlbumAdapter(list)
        rv_listPublicPlaylists.adapter = listHeroAdapter
    }
    private fun updatePage(view: View){
        view.findViewById<TextView>(R.id.tv_number_playlists_user).setText("" + playlists + "")
        view.findViewById<TextView>(R.id.tv_number_followers_user).setText("" + followers + "")
        view.findViewById<TextView>(R.id.tv_number_following_user).setText("" + followings + "")
    }
}