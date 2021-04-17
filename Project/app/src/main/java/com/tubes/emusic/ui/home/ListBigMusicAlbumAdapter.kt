package com.tubes.emusic.ui.home

import android.os.Bundle
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
import com.tubes.emusic.ui.component.DetailPlaylistAlbum


class ListBigMusicAlbumAdapter(private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<ListBigMusicAlbumAdapter.ListViewHolder> (){
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(thumb: Thumbnail) {
            with(itemView){
                Glide.with(itemView.context).load(thumb.urlImage).into(findViewById<ImageView>(R.id.img_item_big_musicalbum_photo))
                findViewById<TextView>(R.id.tv_item_big_musicalbum_name).text = thumb.title

                itemView.setOnClickListener {
                    Log.e("Abstract", "List Big Album item clicked")
                    //val fm : FragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                    //val ldf = DetailPlaylistAlbum()
                    //val commit = fm.beginTransaction().add(R.id.navigation_home, ldf).commit()

                    //Insialisisasi Bundle
                    val args =  (context as MainActivity).setBundle(thumb)


                    val ldf = DetailPlaylistAlbum()
                    // Menaruh data ke dalam fragment yang dikirim
                    ldf.setArguments(args)
                    (context as MainActivity).openFragment(ldf)
                }

            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_big_musicalbum_list, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listThumbnail.size
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }



}
