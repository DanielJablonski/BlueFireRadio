package com.example.bluefireradio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistSongs extends Fragment {

    DatabaseReference ref;
    ArrayList<String> items = new ArrayList<String>();
    long counter;
    Spinner dropdown;
    Button addSong;
    String selectedPlaylist;

    public PlaylistSongs() {
        // Required empty public constructor
    }

    public static PlaylistSongs newInstance() {
        PlaylistSongs fragment = new PlaylistSongs();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_playlist_songs, container, false);
        selectedPlaylist = ((MainActivity)getActivity()).getPlaylistName();
        final LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("playlists");
        dropdown = (Spinner) v.findViewById(R.id.spinner);
        addSong = (Button) v.findViewById(R.id.addSong);
        final long i = 1;

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("name").getValue().toString().equals("Songs"))
                        break;

                    TextView textView = new TextView(getContext());
                    textView.setText(snapshot.child(selectedPlaylist).child("name").getValue().toString());
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(20);
                    textView.setPadding(0,0,0,20);
                    linearLayout.addView(textView);
                    counter = snapshot.getChildrenCount()-1;


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference().child("music");
        musicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    items.add(snapshot.child("name").getValue().toString());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);

                addSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        String text = dropdown.getSelectedItem().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            if(snapshot.child("name").getValue().toString().equals(text))
                            {
                                String name =snapshot.child("name").getValue().toString();
                                String artist = snapshot.child("artist").getValue().toString();
                                String url = snapshot.child("url").getValue().toString();
                                map.put("/" + selectedPlaylist + "/"+ counter + "/", mapSongs(name, artist, url));
                                ref.updateChildren(map, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "The song has been added", Toast.LENGTH_SHORT).show();
                                        counter++;
                                    }
                                });
                            }
                        }


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Inflate the layout for this fragment
        return v;
    }

    public Map<String, Object> mapSongs(String name, String artist, String url)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("artist", artist);
        result.put("url", url);
        return result;
    }

}
