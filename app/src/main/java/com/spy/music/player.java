package com.spy.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class player {

    protected static player p = new player();
    MediaPlayer mediaplayer;

    public player getInstance(){
        return p;
    }

    public void play(Context context,String path,int pos){
        stop();
        if(mediaplayer==null){
            mediaplayer = new MediaPlayer();
        }
        try {
            mediaplayer.setDataSource(context, Uri.fromFile(new File(path)));
            mediaplayer.prepare();
            mediaplayer.start();
            MainActivity.updatecurrent(pos);
            MainActivity.updateTag("playing");
        } catch (IOException e) {
            Log.d("error",e.getMessage());
        }


    }

    public void pause_play(){
        if(mediaplayer.isPlaying()){
            mediaplayer.pause();
            MainActivity.updateTag("notplaying");
        }else {
            mediaplayer.start();
            MainActivity.updateTag("playing");
        }
    }


    public void stop(){
        if(mediaplayer!=null){
            try {
                mediaplayer.stop();
                mediaplayer.release();
                mediaplayer=null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
