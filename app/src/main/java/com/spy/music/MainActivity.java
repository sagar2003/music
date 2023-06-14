package com.spy.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ImageButton previous,play,next;
    static TextView txt_title;

    player p = new player().getInstance();
    static ArrayList<String> names,artists,paths;
    RecyclerView recyclerView;
    static  int currentpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previous=findViewById(R.id.previous);
        play=findViewById(R.id.pause_play);
        next=findViewById(R.id.next);
        txt_title=findViewById(R.id.txt_title);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        names=new ArrayList<>();
        artists=new ArrayList<>();
        paths=new ArrayList<>();


        checkPermission();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.pause_play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentpos!=paths.size()-1){
                    currentpos+=1;
                }else{
                    currentpos=0;
                }
                p.play(recyclerView.getContext(),paths.get(currentpos),currentpos);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentpos!=0){
                    currentpos-=1;
                }else{
                    currentpos=paths.size()-1;
                }
                p.play(recyclerView.getContext(),paths.get(currentpos),currentpos);
            }
        });

    }

    public static void updatecurrent(int pos){
        currentpos=pos;
        txt_title.setText(names.get(pos));
    }

    public static void updateTag(String tag){
        play.setTag(tag);
        if(play.getTag().equals("playing")){
            play.setImageResource(R.drawable.baseline_pause_circle_24);
        }else{
            play.setImageResource(R.drawable.baseline_play_circle_24);
        }
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }else{
            getAllAudioFromDevice(play.getContext());
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    public void getAllAudioFromDevice(final Context context) {


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.ArtistColumns.ARTIST};
        Cursor c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            c = context.getContentResolver().query(uri, projection, null, null);
        }

        if (c != null) {
            while (c.moveToNext()) {

                String path = c.getString(0);
                String artist = c.getString(1);

                String name = path.substring(path.lastIndexOf("/") + 1);

                names.add(name);
                artists.add(artist);
                paths.add(path);

            }
            c.close();
            adapter_songs adapter = new adapter_songs(play.getContext(),names,artists,paths);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getAllAudioFromDevice(play.getContext());
                    }else{
                        requestPermission();
                    }
                }
                break;
            default:
                break;
        }
    }
}