package com.example.project

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.adapter.MusicAdapter
import com.example.project.model.Music
import kotlinx.android.synthetic.main.activity_music_list.*


class MusicListActivity : AppCompatActivity() {
    val AUDIO_PERMISSION_CODE = 119
    private val musicList = ArrayList<Music>()
    lateinit var musicAdapter: MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        checkPermissionForFile()
        //getAudioFiles()
    }

    private fun checkPermissionForFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )

                requestPermissions(permissions, AUDIO_PERMISSION_CODE)
            } else {
                getAudioFiles()
            }
        } else {
            getAudioFiles();
        }
    }


    @SuppressLint("Range")
    fun getAudioFiles() {
        val contentResolver = contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val audioDuration: Int =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val albumId:Long=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val url: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val imageUri=Uri.parse("content://media/external/audio/albumart")
                val albumUri=ContentUris.withAppendedId(imageUri,albumId)
                musicList.add(Music(title, audioDuration.toString(), artist, /*Uri.parse(url),*/albumUri))
            } while (cursor.moveToNext())
        }
        musicAdapter = MusicAdapter(this,musicList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        musicRecycler.layoutManager = linearLayoutManager
        musicRecycler.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()


    }
}