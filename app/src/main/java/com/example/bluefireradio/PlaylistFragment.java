package com.example.bluefireradio;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.fragment;

public class PlaylistFragment extends Fragment {

    EditText input;
    DatabaseReference ref;
    String srt;
    CardView playlist;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = new Fragment();
    Bundle bundle = new Bundle();

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PlaylistFragment fragment = new PlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_playlist, container, false);

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("playlists");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(! dataSnapshot.getValue().toString().equals("0"))
                {
                    TextView playlistName = (TextView) v.findViewById(R.id.playlistName);
                    TextView numOfsongs = (TextView) v.findViewById(R.id.numOfSongs);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String playlistNameString = snapshot.child("name").getValue().toString();
                        playlistName.setText(playlistNameString);
                        numOfsongs.setText(String.valueOf(snapshot.getChildrenCount()-1) + " songs");

                        ((MainActivity)getActivity()).savePlaylist(playlistNameString);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        playlist = (CardView) v.findViewById(R.id.playlistCardview);
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, PlaylistSongs.newInstance());
                fragmentTransaction.commit();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.addPlaylist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add Playlist"); //Set Alert dialog title here

                // Set an EditText view to get user input
                input = new EditText(getContext());
                input.setHint("Enter Playlist Name");
                alert.setView(input);


                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        srt = input.getEditableText().toString();

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("/" + srt + "/", toMap(srt));
                        ref.updateChildren(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            }
                        });
                        Toast.makeText(getContext(),srt, Toast.LENGTH_LONG).show();
                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();


            }
        });
        return v;
    }

    public Map<String, Object> toMap(String playList)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", playList);
        return result;
    }




}
