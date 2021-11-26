package com.example.project.model

import android.net.Uri

data class Music(
    val audioTitle: String,
    val audioDuration: String,
    val audioArtist: String,
    /*val audioUri: Uri,*/
    val image:Uri
)