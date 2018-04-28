package com.test.zj.m2048.utils;

import android.content.Context;
import android.media.MediaPlayer;


/**
 * 音效播放，使用MediaPlayer
 */

public class RingPlayer {

    MediaPlayer mediaPlayer;
    int sourceID;
    Context mContext;

    public RingPlayer(int id, Context context){
        sourceID = id;
        mContext =context;
    }

    private void init(){
        mediaPlayer = MediaPlayer.create(mContext,sourceID);
    }

    public void play(){
        if(mediaPlayer == null){
            init();
        }

        if(mediaPlayer.isPlaying()){
            return;
        }

        mediaPlayer.start();
    }

    public void destroy(){
        if(mediaPlayer == null){
            return;
        }

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        mediaPlayer.release();

        mContext = null;
    }

}

