package com.tubes.emusic.entity

import android.media.MediaPlayer

class Playbar {
    companion object{
        public var mediaPlayer: MediaPlayer? = null
        public var pause : Boolean = false
        public var isPlaying : Boolean = false
        public var shuffle : Boolean = false
        public var repeat : Boolean = false
    }
}