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
import com.tubes.emusic.api.AlbumApi
import com.tubes.emusic.api.AlbumData
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.api.UserApi
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
        view.findViewById<ImageView>(R.id.img_back_icon).setOnClickListener {
            Log.e("Abstract", "Back to Stack")
            (context as MainActivity).openFragment(LibraryFragment())
        }

        rv_listPublicPlaylists = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_item_user_music)
        rv_listPublicPlaylists.setHasFixedSize(true)
        view.findViewById<TextView>(R.id.tv_profile_name_user).setText(bundleData.title)
        Glide.with(this).load(bundleData.urlImage).into(object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?) {
                view.findViewById<LinearLayout>(R.id.img_foto_profile_user).setBackground(resource)
            }
        })
        val followingstatus = view.findViewById<ToggleButton>(R.id.btn_following_user)
        var statusFollowers = true
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
            delay(1000)
            AlbumApi.searchAlbumByArtits(bundleData.id!!)
            if(detail?.datafollowers != null) {
                followers = "" + detail.datafollowers.size
                if(UserApi.searchIdUserArray(MainActivity.currentUser?.iduser!!,detail.datafollowers)){
                    if(!followingstatus.isChecked) {
                        statusFollowers = false
                        followingstatus.isChecked = true
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
        /*
        val list = ArrayList<Thumbnail>()
        val hero1 = Thumbnail( "sfhskwe","LibraryListMusic", "", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Avicii", "Description Avicii")
        list.add(hero1)
        list.add(hero1)
        val hero2 = Thumbnail( "3242ddwe","LibraryListMusic", "", "https://www.allkpop.com/upload/2019/09/content/211137/1569080263-ee-ymhtueaahug.jpg" , "Twice", "Description Avicii")
        list.add(hero2)
        list.add(hero1)
        list.add(hero2)
        list.add(hero2)
        list.add(hero2)
        list.add(hero1)
        list.add(hero1)
        list.add(hero1)*/
        var mapData : List<AlbumData> = MappingHelper.mapListAlbumToArrayList(
                MainActivity.db?.queryCustomById(bundleData.id!!, DatabaseContract.AlbumDB.IDUSER, DatabaseContract.AlbumDB.TABLE_NAME)
        )
        val list = ArrayList<Thumbnail>()
        for(i in mapData){
            val thumb = Thumbnail(i.idalbum.toString(), "Album", "", HTTPClientManager.host + "album/" + i.idalbum + "/photo",  i.namealbum, i.daterelease.substring(0, 4))
            list.add(thumb)
        }
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