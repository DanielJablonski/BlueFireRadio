package com.example.bluefireradio;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;


public class HomeFragment extends Fragment {

    FragmentTransaction fragmentTransaction;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_home, container, false);

        TextView hits = (TextView) v.findViewById(R.id.bestHits);
        TextView albums = (TextView) v.findViewById(R.id.topAlbums);
        TextView artists = (TextView) v.findViewById(R.id.topArtists);

        AssetManager am = getResources().getAssets();
        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "timeburnerbold.ttf"));

        hits.setTypeface(typeface);
        albums.setTypeface(typeface);
        artists.setTypeface(typeface);

        CardView topAlbum = (CardView) v.findViewById(R.id.topAlbum);
        CardView bestMusic = (CardView) v.findViewById(R.id.bestMusic);
        CardView topArtist = (CardView) v.findViewById(R.id.artist);

        topAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, PlaylistFragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        bestMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, MusicFragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        topArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, ArtistFragment.newInstance());
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}
