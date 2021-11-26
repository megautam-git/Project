package com.example.project.adapter


import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Music

class MusicAdapter(private val context:Context,private val list: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val duration = itemView.findViewById<TextView>(R.id.duration)
        val artist = itemView.findViewById<TextView>(R.id.artist)
       // val uri = itemView.findViewById<TextView>(R.id.uri)
        val album=itemView.findViewById<ImageView>(R.id.imgAudio)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.audio_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].audioTitle
        holder.duration.text = list[position].audioDuration
        holder.artist.text = list[position].audioArtist
      //  holder.uri.text = list[position].audioUri.toString()
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,list[position].image)
            holder.album.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}