package com.tubes.emusic.ui.component

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.AlbumApi
import com.tubes.emusic.api.MusicApi
import com.tubes.emusic.api.PlaylistApi
import com.tubes.emusic.api.UserApi
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ListMusicAlbumAdapter(private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<ListMusicAlbumAdapter.ListViewHolder> () {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thumb: Thumbnail) {
            with(itemView){
                if(thumb.urlImage == "" || thumb.urlImage == null){
                    Log.e("Abstract", "Url image is emypty")
                    thumb.urlImage =  "http://18.140.59.14/static/playlist_default.jpg"
                }
                Glide.with(itemView.context).load(thumb.urlImage).into(findViewById<ImageView>(R.id.img_item_musicalbum_photo))
                findViewById<TextView>(R.id.tv_item_music_name).text = thumb.title
                findViewById<TextView>(R.id.tv_item_music_description).text = thumb.description

                when(thumb.addOn){
                    "NoCover" -> {
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).setVisibility(View.GONE)
                    }
                    "LibraryListMusic" -> {
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).layoutParams.width = 250
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).layoutParams.height = 250
                    }
                    "LibraryListAlbum" -> {
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).layoutParams.width = 250
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).layoutParams.height = 250
                    }
                }


                itemView.setOnClickListener {
                    Log.e("Abstract", "List Music item clicked")
                    when(thumb.type){
                        "LibraryListAlbum" -> {
                            //Insialisisasi Bundle
                            val args = (context as MainActivity).setBundle(thumb)
                            // Frament destination
                            // Otw digantgi jadi format di my library
                            val ldf = DetailPlaylistAlbum()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                        "Music" -> {
                            //Insialisisasi Bundle
                            val args = (context as MainActivity).setBundle(thumb)
                            val ldf = PlaybarFragment()
                            ldf.setArguments(args)
                            // Menaruh data ke dalam fragment yang dikirim
                            (context as MainActivity).openFragment(ldf)
                        }
                        "Album" -> {
                            //Insialisisasi Bundle
                            val args = (context as MainActivity).setBundle(thumb)
                            val ldf = DetailPlaylistAlbum()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                        "Playlist" -> {
                            //Insialisisasi Bundle
                            val args = (context as MainActivity).setBundle(thumb)
                            val ldf = DetailPlaylistAlbum()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                    }
                }
                val button = itemView.findViewById<ImageButton>(R.id.btn_more_music_album_item)
                button.setOnClickListener { v: View ->
                    val popup = PopupMenu(itemView.context, v)
                    popup.menuInflater.inflate(R.menu.overflow_item_menu, popup.menu)

                    if(likedThumb(thumb)){
                        popup.menu.getItem(0).setVisible(false)
                    }else{
                        popup.menu.getItem(1).setVisible(false)
                    }

                    if(thumb.type != "Music"){
                        popup.menu.getItem(2).setVisible(false)
                    }
                    popup.menu.getItem(3).setVisible(false)
                    popup.menu.getItem(4).setVisible(false)
                    if(thumb.type == "Playlist"){
                        if(MainActivity.detailUser?.dataplaylistowned != null){
                            for( i in MainActivity.detailUser!!.dataplaylistowned){
                                if(i == thumb.id){
                                    //Disable like
                                    popup.menu.getItem(4).setVisible(true)
                                    popup.menu.getItem(0).setVisible(false)
                                    break
                                }
                            }
                        }
                    }
                    popup.setOnMenuItemClickListener { item: MenuItem ->
                        if (item.itemId == R.id.like_items) {
                            Log.e("Detail", "Item Like")
                            addLikedThumb(thumb)
                            popup.menu.getItem(0).setVisible(false)
                            popup.menu.getItem(1).setVisible(true)
                            //Toast.makeText(ApplicationProvider.getApplicationContext<Context>(), "Email clicked", Toast.LENGTH_SHORT).show()
                        } else if (item.itemId == R.id.unlike_items) {
                            Log.e("Detail", "Item UnLike")
                            removeLikedThumb(thumb)
                            popup.menu.getItem(0).setVisible(true)
                            popup.menu.getItem(1).setVisible(false)
                            //Toast.makeText(ApplicationProvider.getApplicationContext<Context>(), "Call clicked", Toast.LENGTH_SHORT).show()
                        } else if (item.itemId == R.id.add_playlist_item){
                            Log.e("Abstract", "Add Playlist Item")
                            addtomyplaylist(itemView,thumb)
                        }
                        true
                    }

                    popup.show()
                }
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_musicalbum_list, viewGroup, false)
        return ListViewHolder(view)
    }
    private fun likedThumb(thumb: Thumbnail): Boolean{
        if(thumb.type == "Album" && MainActivity.detailUser?.dataalbum != null){
            for(i in MainActivity.detailUser!!.dataalbum){
                if(i == thumb.id){
                    return true
                }
            }
        }else if(thumb.type =="Playlist" && MainActivity.detailUser?.dataplaylistliked != null){
            for(i in MainActivity.detailUser!!.dataplaylistliked){
                if(i == thumb.id){
                    return true
                }
            }
        }else if(thumb.type =="Music" && MainActivity.detailUser?.datalikedsong != null){
            for(i in MainActivity.detailUser!!.datalikedsong){
                if(i == thumb.id){
                    return true
                }
            }
        }
        return false
    }

    private fun addLikedThumb(thumb: Thumbnail): Boolean{
        if(thumb.type == "Album"){
            GlobalScope.launch {
                AlbumApi.addFollowingAlbum(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }else if(thumb.type =="Playlist"){
            GlobalScope.launch {
                PlaylistApi.addFollowingPlaylist(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }else if(thumb.type =="Music"){
            GlobalScope.launch {
                MusicApi.addLikedMusic(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }
        return false
    }

    private fun removeLikedThumb(thumb: Thumbnail): Boolean{
        if(thumb.type == "Album"){
            GlobalScope.launch {
                AlbumApi.deleteFollowingAlbum(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }else if(thumb.type =="Playlist"){
            GlobalScope.launch {
                PlaylistApi.deleteFollowingPlaylist(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }else if(thumb.type =="Music"){
            GlobalScope.launch {
                MusicApi.deleteLikedMusic(thumb.id!!.toInt(), MainActivity.detailUser!!.id)
            }
            Thread.sleep(550)
            MainActivity.synchronizeObject()
            return true
        }
        return false
    }

    private fun addtomyplaylist(view: View,itemsong: Thumbnail) {
        if(MainActivity.playlistUser == null){
            return
        }
        val itemsThumb = ArrayList<Thumbnail>()
        val items = Array<String>(MainActivity.playlistUser!!.size){""}
        val selectedList =  ArrayList<Int>()
        val builder = AlertDialog.Builder(view.context)

        var c = 0

        for(i in MainActivity.playlistUser!!){
            itemsThumb.add(Thumbnail(i.idplaylist.toString(),"Playlist","","",i.nameplaylist, ""))
            items.set(c,i.nameplaylist)
            c++
        }

        builder.setTitle("List your playlist")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("Add") {
            dialogInterface, i ->
           for (j in selectedList.indices) {
                GlobalScope.launch{
                    PlaylistApi.addSongPlaylist(itemsThumb.get(selectedList[j]).id!!.toInt()  , itemsong.id!!.toInt())
                }
            }
        }
        builder.show()
    }

    override fun getItemCount(): Int = listThumbnail.size
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }
}

