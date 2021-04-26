package com.tubes.emusic.ui.component

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail

class ListUserAdapter (private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder> () {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thumb: Thumbnail) {
            with(itemView){
                Glide.with(itemView.context).load(thumb.urlImage).into(findViewById<ImageView>(R.id.img_item_user_photo))
                findViewById<TextView>(R.id.tv_item_user_name).text = thumb.title

                when(thumb.addOn){
                    "ListArtist" -> {
                        findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_item_user_photo).layoutParams.width = 250
                        findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.img_item_user_photo).layoutParams.height = 250
                    }
                }

                itemView.setOnClickListener {
                    Log.e("Abstract", "List user item clicked")
                    when(thumb.type){
                        "Artist" -> {
                            val args =  (context as MainActivity).setBundle(thumb)
                            val ldf = ArtistProfileFragment()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                        "User" -> {
                            val args =  (context as MainActivity).setBundle(thumb)
                            val ldf = UserProfileFragment()
                            ldf.setArguments(args)
                            (context as MainActivity).openFragment(ldf)
                        }
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListUserAdapter.ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user_list, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listThumbnail.size
    override fun onBindViewHolder(holder: ListUserAdapter.ListViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }
}