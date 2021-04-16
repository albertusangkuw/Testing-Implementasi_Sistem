package com.tubes.emusic.ui.component

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail


class ListMusicAlbumAdapter(private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<ListMusicAlbumAdapter.ListViewHolder> () {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thumb: Thumbnail) {
            with(itemView){
                Glide.with(itemView.context).load(thumb.urlImage).into(findViewById<ImageView>(R.id.img_item_musicalbum_photo))
                findViewById<TextView>(R.id.tv_item_music_name).text = thumb.title
                findViewById<TextView>(R.id.tv_item_music_description).text = thumb.description

                when(thumb.type){
                    "MusicNoCover" -> {
                        findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.img_item_musicalbum_photo).setVisibility(View.GONE)
                    }

                }

                itemView.setOnClickListener {
                    Log.e("Abstract", "List Music item clicked")

                    //Insialisisasi Bundle
                    /*
                    val args = Bundle()
                    args.putString("id", thumb.id)
                    args.putString("type", thumb.type)
                    args.putString("title", thumb.title)
                    args.putString("urlImage", thumb.urlImage)
                    val ldf = DetailPlaylistAlbum()
                    // Menaruh data ke dalam fragment yang dikirim
                    ldf.setArguments(args)
                    (context as MainActivity).openFragment(ldf)
                    */

                }
                val button = itemView.findViewById<ImageButton>(R.id.btn_more_music_album_item)
                button.setOnClickListener { v: View ->
                    val popup = PopupMenu(itemView.context, v)
                    popup.menuInflater.inflate( R.menu.overflow_item_menu, popup.menu)

                    popup.setOnDismissListener {
                        // Respond to popup being dismissed.
                    }
                    // Show the popup menu.
                    popup.show()
                }


            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_musicalbum_list, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listThumbnail.size
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }
}

