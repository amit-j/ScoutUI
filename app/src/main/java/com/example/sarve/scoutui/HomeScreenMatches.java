package com.example.sarve.scoutui;



import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

public class HomeScreenMatches extends Fragment {

    Button btnShowGamerID, btnPreviousMatch, btnNextMatch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen_matches, container, false);
        return rootView;
    }
}
