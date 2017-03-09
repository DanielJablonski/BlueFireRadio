package com.example.bluefireradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Random;

public class Music extends AppCompatActivity {

    Button myButton;
    SeekBar seekBar;
    AudioManager myAudioManager;
    ToggleButton myToggleButton;


    private MediaPlayer mMediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mMediaplayer = new MediaPlayer();
        mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        fetchAudioUrlFromFirebase();

        seekBar=(SeekBar)findViewById(R.id.seekbar1);
        myToggleButton=(ToggleButton) findViewById(R.id.toggleButton);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        myToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mMediaplayer.start();
                } else {
                    mMediaplayer.pause();


                }
            }
        });

    }

    private void fetchAudioUrlFromFirebase() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("music");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StorageReference storageRef = storage.getReferenceFromUrl(dataSnapshot.child(String.valueOf(randomInt(0,1))).getValue().toString());
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            // Download url of file
                            final String url = uri.toString();
                            mMediaplayer.setDataSource(url);
                            // wait for media player to get prepare
                            mMediaplayer.prepare();
                            mMediaplayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("TAG", e.getMessage());
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
// Create a storage reference from our app


    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
