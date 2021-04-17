package com.tubes.emusic.ui.search

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.entity.Thumbnail
import com.tubes.emusic.ui.component.DetailPlaylistAlbum
import java.util.*
import kotlin.collections.ArrayList

class CardViewGenreAdapter(private val listThumbnail: ArrayList<Thumbnail>): RecyclerView.Adapter<CardViewGenreAdapter.CardViewHolder> () {
   inner class CardViewHolder(itemView: View): RecyclerView .ViewHolder(itemView){
       fun bind(thumb: Thumbnail){
           with(itemView){
               val rnd = Random()
               val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
               findViewById<CardView>(R.id.cv_inner_genre).setCardBackgroundColor(color)
               findViewById<TextView>(R.id.tv_item_genre_text).text = thumb.title
               itemView.setOnClickListener {
                   Log.e("Abstract", "List Card View Genre item clicked")

                   //Insialisisasi Bundle
                   val args =  (context as MainActivity).setBundle(thumb)

                   // Frament destination
                   // Otw digantgi jadi format di my library
                   val ldf = DetailPlaylistAlbum()
                   // Menaruh data ke dalam fragment yang dikirim
                   ldf.setArguments(args)
                   (context as MainActivity).openFragment(ldf)

                   //Toast.makeText(itemView.context, "Kamu memilih $thumb.title}", Toast.LENGTH_SHORT).show()
               }
           }
       }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_genre_card , viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listThumbnail[position])
    }

    override fun getItemCount(): Int = listThumbnail.size

}