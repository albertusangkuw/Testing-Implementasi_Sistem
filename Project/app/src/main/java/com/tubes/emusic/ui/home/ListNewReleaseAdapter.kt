package com.tubes.emusic.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.HTTPClientManager
import com.tubes.emusic.api.PlaylistApi
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.DetailPlaylistAlbum
import com.tubes.emusic.ui.component.ListMusicAlbumAdapter
import com.tubes.emusic.ui.playbar.PlaybarFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListNewReleaseAdapter(private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<ListNewReleaseAdapter.ListViewHolder> () {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thumb: Thumbnail) {
            with(itemView){
                if(thumb.urlImage == "" || thumb.urlImage == null){
                    Log.e("Abstract", "Url image is empty")
                    thumb.urlImage =  "http://18.140.59.14/static/playlist_default.jpg"
                }
                Glide.with(itemView.context).load(thumb.urlImage).into(findViewById<ImageView>(R.id.img_item_musicalbum_photo_release))
                findViewById<TextView>(R.id.tv_item_music_name_release).text = thumb.title
                findViewById<TextView>(R.id.tv_item_music_description_release).text = thumb.description

                val artist = MainActivity.getUserByIdUser(thumb.addOn)
                Glide.with(itemView.context).load(HTTPClientManager.host + "users/" +  thumb.addOn + "/photo" ).into(findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_item_user_photo_release))
                findViewById<TextView>(R.id.tv_item_user_name_release).text = artist?.username

                itemView.setOnClickListener {
                    Log.e("Abstract", "List Music item clicked")
                    when(thumb.type){
                        "Album" -> {
                            //Insialisisasi Bundle
                            val args = (context as MainActivity).setBundle(thumb)
                            val ldf = DetailPlaylistAlbum()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_new_release_list, viewGroup, false)
        return ListViewHolder(view)
    }
    override fun getItemCount(): Int = listThumbnail.size
    override fun onBindViewHolder(holder: ListNewReleaseAdapter.ListViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }
}