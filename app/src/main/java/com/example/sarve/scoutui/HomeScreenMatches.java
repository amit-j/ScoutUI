package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarve.scoutui.Model.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class HomeScreenMatches extends Fragment {

    Button btnShowGamerID, btnPreviousMatch, btnNextMatch;
    private List<DocumentSnapshot> matches;
    private String username;
    private String game;
    ProgressDialog mDialog;
    private FirebaseFirestore mFirestore;
    private int mPlayerRating;
    public Iterator<DocumentSnapshot> matchesIterator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen_matches, container, false);
        EditText overRating = (EditText) rootView.findViewById(R.id.matchStrikerValue);
        overRating.setText("abdc");
        btnShowGamerID = (Button) rootView.findViewById(R.id.btnShowGamerID);
        test();
        matchesIterator = matches.iterator();

        btnShowGamerID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {/*
                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

// define your view here that found in popup_layout
// for example let consider you have a button

                Button btn = (Button) popupView.findViewById(R.id.button);

// finally show up your popwindow
                popupWindow.showAsDropDown(popupView, 0, 0);
*/
            }
        });
        btnNextMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (matchesIterator.hasNext()) {
                    matchesIterator.next();


                }

            }
        });
        return rootView;

    }


    private void test() {
        username = getUserName();
        game = HomeScreenMatches.this.getActivity().getIntent().getStringExtra("gamename");

        CollectionReference collection = mFirestore.collection("user_rankings/pubg/rankings");
        Query q = collection.whereGreaterThan("player_rating", mPlayerRating - 10).whereLessThan("player_rating", mPlayerRating + 10);
        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {

                matches = snapshots.getDocuments();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(HomeScreenMatches.this.getActivity(), e.getMessage(),
                                                      Toast.LENGTH_SHORT).show();
                                          }
                                      }
                );
    }

    private String getUserName() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);
        return restoredText;
    }


    private void ggetPlayerData() {

        CollectionReference collection = mFirestore.collection("user_rankings/" + game + "/rankings");
        collection.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {

                        mPlayerRating = Integer.parseInt(document.get("player_rating").toString());


                    } else {
                        Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player data.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                mDialog.setProgress(0);
            }

        });
    }

}
