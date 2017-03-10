package com.example.bluefireradio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaylistFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

}
