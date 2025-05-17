package com.bca.musicplayer.ui.music.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bca.musicplayer.R
import com.bca.musicplayer.databinding.ItemMusicBinding
import com.bca.musicplayer.domain.models.Music
import com.bumptech.glide.Glide

class MusicAdapter(private val context: Context,private val listMusic: List<Music>)  : RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = listMusic[position]
        holder.bind(data,context)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listMusic[holder.adapterPosition],position)
        }
    }

    override fun getItemCount(): Int = listMusic.size

    class MyViewHolder(private val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Music, context: Context) {
            binding.txtTitle.text = data.musicName
            binding.txtArtist.text = data.artistName
            Glide.with(context).load(data.imageUrl).into(binding.imgCover)
            Glide.with(context).asGif().load(R.drawable.equalizer).into(binding.equalizer)
            if(data.isPlay){
                binding.equalizer.visibility = View.VISIBLE
            }else{
                binding.equalizer.visibility = View.GONE
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Music,position: Int)
    }
}