package com.bca.musicplayer.ui.music

import android.icu.text.Transliterator.Position
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bca.musicplayer.R
import com.bca.musicplayer.data.collector.ResultBound
import com.bca.musicplayer.databinding.ActivityMusicBinding
import com.bca.musicplayer.domain.models.Music
import com.bca.musicplayer.ui.music.adapter.MusicAdapter
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var musicViewModel: MusicViewModel
    private var search: String = ""
    private lateinit var list: List<Music>;
    private lateinit var adapter: MusicAdapter;
    private var mediaPlayer: MediaPlayer? = null;
    var position = 0;
    private var searchJob: Job? = null;
    private var onPause : Boolean  = false;
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.rvMusic.layoutManager = LinearLayoutManager(this@MusicActivity)
        val musicViewModel : MusicViewModel by viewModel()
        this.musicViewModel = musicViewModel
        showListMusic("tulus")
        binding.imgPlay.setOnClickListener {
            if(list.isNotEmpty()){
                playMusic();
            }
        }
        binding.imgNext.setOnClickListener {
            if(list.isNotEmpty()) {
                nextMusic();
            }
        }
        binding.imgPrevious.setOnClickListener {
            if(list.isNotEmpty()) {
                previousMusic();
            }
        }
        binding.txtSearch.addTextChangedListener(
            afterTextChanged = {
                searchJob?.cancel()
                searchJob = lifecycleScope.launchWhenResumed {
                    delay(1000)
                    showListMusic(it.toString())
                }
            }
        )
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            if(fromUser){
                mediaPlayer?.seekTo(value.toInt())
            }

        }
    }
    private fun showListMusic(search:String){
        binding.rvMusic.visibility = View.GONE
        musicViewModel.getListMusic(search).observe(this){ result ->
            when(result){
                is ResultBound.Loading -> {
                    showLoading(true)
                }
                is ResultBound.Error -> {
                    Toast.makeText(this@MusicActivity, result.message ,Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
                is ResultBound.Success -> {
                    showLoading(false)
                    list = result.data!!
                    showRecyclerList()
                }
            }
        }
    }
    private fun showRecyclerList() {
        binding.rvMusic.visibility = View.VISIBLE
        adapter = MusicAdapter(this@MusicActivity, list)
        binding.rvMusic.adapter = adapter
        adapter.setOnItemClickCallback(object : MusicAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Music,_position: Int) {
                list.map {
                    it.isPlay = false
                }
                onPause = false;
                position = _position;
                playMusic();
            }
        })
    }
    private fun nextMusic(){
        onPause = false;
        list.get(position).isPlay = if (list.get(position).isPlay)  false else true;
        //next position
        val totalSize = list.size - 1;
        position = if (position == totalSize) 0 else position + 1;
        list.get(position).isPlay = if (list.get(position).isPlay)  false else true;
        binding.imgPlay.setImageDrawable( getDrawable(android.R.drawable.ic_media_pause ))
        adapter.notifyDataSetChanged()
        playPreview(list.get(position).previewUrl)
    }
    private fun previousMusic(){
        onPause = false;
        list.get(position).isPlay = if (list.get(position).isPlay)  false else true;

        position = if (position == 0) 0 else position - 1;
        list.get(position).isPlay = if (list.get(position).isPlay)  false else true;
        binding.imgPlay.setImageDrawable(if (list.get(position).isPlay)  getDrawable(android.R.drawable.ic_media_pause) else getDrawable(android.R.drawable.ic_media_play) )
        adapter.notifyDataSetChanged()
        playPreview(list.get(position).previewUrl)
    }
    private fun playMusic(){
        list.get(position).isPlay = if (list.get(position).isPlay)  false else true;
        binding.imgPlay.setImageDrawable(if (list.get(position).isPlay)  getDrawable(android.R.drawable.ic_media_pause) else getDrawable(android.R.drawable.ic_media_play) )
        if(!list.get(position).isPlay){
            onPause = true;
            pausePreview()
        }else{
            if(!onPause){
                adapter.notifyDataSetChanged()
                playPreview(list.get(position).previewUrl)
            }else{
                mediaPlayer!!.start()
            }
        }

    }
    private fun playPreview(url:String){
        clearPreview()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            it.start()
            val duration = mediaPlayer!!.duration.toFloat();
            binding.slider.valueTo = duration
        }

        updateRunnable = object : Runnable {
            override fun run() {
                if(mediaPlayer != null){
                    binding.slider.value = mediaPlayer!!.currentPosition.toFloat()
                    handler.postDelayed(this,500)
                }
            }

        }
        handler.post(updateRunnable)
        mediaPlayer!!.setOnCompletionListener {
            nextMusic()
            handler.removeCallbacks(updateRunnable)
        }

    }
    private fun clearPreview(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null;
    }
    private fun pausePreview(){
        mediaPlayer?.pause()
    }


    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}