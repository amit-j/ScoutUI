/*the  second fragment in the home screen
* 1.shows the matches
* 2.user can switch b/w matches and request for gamer ID*/
package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.provider.DocumentsProvider;
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
import android.util.Log;
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
import com.example.sarve.scoutui.Model.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HomeScreenMatches extends Fragment {

    private static final String KEY_OVERALLRATING = "player_rating";
    private static final String KEY_TEAMPLAYER = "rank_var_1_avg";
    private static final String KEY_CAMPER = "rank_var_2_avg";
    private static final String KEY_STRIKER = "rank_var_3_avg";
    private static final String KEY_USERNAME = "player_rating";
    Button btnShowGamerID, btnPreviousMatch, btnNextMatch;
    private ArrayList<DocumentSnapshot> matches;
    private String username;
    private String game;
    ProgressDialog mDialog;
    public FirebaseFirestore mFirestore;
    private int mPlayerRating;
    public ListIterator<DocumentSnapshot> matchesIterator;
    public DocumentSnapshot previousMatch, currentMatch;
    EditText totalMatches, matchOverAllRating, matchUserName, matchLanguage, matchAge, matchTime, matchTeamPlayer, matchCamper, matchStriker;

    private static final int MAX_MATCHES = 5;
    private int matchesDone = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirestore = FirebaseFirestore.getInstance();
        View rootView = inflater.inflate(R.layout.fragment_home_screen_matches, container, false);
        btnShowGamerID = (Button) rootView.findViewById(R.id.btnShowGamerID);
        btnNextMatch = (Button) rootView.findViewById(R.id.btnNextMatch);
        btnPreviousMatch = (Button) rootView.findViewById(R.id.btnPreviousMatch);
        totalMatches = (EditText) rootView.findViewById(R.id.totalMatchesValue);
        matchOverAllRating = (EditText) rootView.findViewById(R.id.matchOverAllRatingValue);
        matchUserName = (EditText) rootView.findViewById(R.id.matchUserNameValue);
        matchAge = (EditText) rootView.findViewById(R.id.matchAgeValue);
        matchTime = (EditText) rootView.findViewById(R.id.matchTimeValue);
        matchTeamPlayer = (EditText) rootView.findViewById(R.id.matchTeamPlayerValue);
        matchCamper = (EditText) rootView.findViewById(R.id.matchCamperValue);
        matchStriker = (EditText) rootView.findViewById(R.id.matchStrikerValue);
        matchLanguage = (EditText) rootView.findViewById(R.id.matchLanguageValue);
        getPlayerData();//calls the fucntion


/*buttons for previous, next and show gamer ID*/
        btnShowGamerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.setMessage("Loading..");
                mDialog.show();
                matchesDone++;
                if(matchesDone > MAX_MATCHES){
                    mDialog.setMessage("Max Limit reached for the day.");
                    btnShowGamerID.setEnabled(false);
                    return;
                }
                mDialog.show();
                saveMatches();


            }
        });


        btnNextMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ProgressDialog(HomeScreenMatches.this.getActivity());
                mDialog.setMessage("Getting a match!");
                mDialog.setProgress(0);
                mDialog.show();
                getNextMatch();

            }
        });


        btnPreviousMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ProgressDialog(HomeScreenMatches.this.getActivity());
                mDialog.setMessage("Getting a match!");
                mDialog.setProgress(0);
                mDialog.show();
                getPreviousMatch();

            }
        });


        return rootView;
    }


    private void saveMatches() {//saves the matches into database
        try {
            final Map<String, Object> newProfile = new HashMap<>();
            newProfile.put("has_been_rated", false);
            newProfile.put("rank_var_1", false);
            newProfile.put("rank_var_2", false);
            newProfile.put("rank_var_3", false);

            CollectionReference collection = mFirestore.collection("users/" + currentMatch.getId() + "/gamer_profiles");
            collection.document(game).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();/*retrieves the documents AND fields from the database into the document snapshot*/

                            final String gamerID = document.get("gamer_ID").toString();
                            if (document.exists()) {

                                newProfile.put(Globals.GAMERID_KEY, document.get("gamer_ID"));//creates a document in users/'username'/gamer_profiles/'game'/matches
                                CollectionReference newmatch = mFirestore.collection("users/" + username + "/gamer_profiles/" + game + "/matches");
                                newmatch.document(currentMatch.getId()).set(newProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mDialog.setMessage( "Gamer ID: "+gamerID);//displays the gamerID
                                        mDialog.setCancelable(true);
                                      //  matches.remove(currentMatch);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomeScreenMatches.this.getActivity(), "Something went wrong, Please try again later." + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                    }

                }

            });


        } catch (Exception ex) {
            Toast.makeText(HomeScreenMatches.this.getActivity(), "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
        }


    }

    private void getdata() {

        mDialog = new ProgressDialog(HomeScreenMatches.this.getActivity());//dialog box to indicate that its loading(fetching)
        mDialog.setMessage("Please wait while we set things up");
        mDialog.setProgress(0);


        mDialog.show();



        CollectionReference collection = mFirestore.collection("user_rankings/" + game + "/rankings");//in rankings
        Query q = collection.whereGreaterThan("player_rating", mPlayerRating - 10).whereLessThan("player_rating", mPlayerRating + 10);//querying for profiles whose ratings are within a difference of 10
        /*for example: if a player has a rating of 90, profile whose ratings are within range of 80-100 are shown as matches to the user*/
        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                matches = new ArrayList<>(snapshots.getDocuments());//stores the documents fetched into an array list
                matchesIterator = matches.listIterator();
                if (matchesIterator.hasNext()) {/*iterator to show each match*/

                    currentMatch = matchesIterator.next();//show next match
                    matchOverAllRating.setText(currentMatch.get("player_rating").toString());/*different kinds of ratings of that particular user*/
                    matchTeamPlayer.setText(currentMatch.get("rank_var_1_avg").toString());
                    matchCamper.setText(currentMatch.get("rank_var_2_avg").toString());
                    matchStriker.setText(currentMatch.get("rank_var_3_avg").toString());
                    displayProfileData(currentMatch.getId().toString());//displaying the data on the xml layout



                }

                totalMatches.setText("" + matches.size());
                mDialog.dismiss();
            }
        });
    }


    private void getNextMatch() {//show the next match's ratings and details
        if (matchesIterator.hasNext()) {

            currentMatch = matchesIterator.next();
            matchOverAllRating.setText(currentMatch.get("player_rating").toString());
            matchTeamPlayer.setText(currentMatch.get("rank_var_1_avg").toString());
            matchCamper.setText(currentMatch.get("rank_var_2_avg").toString());
            matchStriker.setText(currentMatch.get("rank_var_3_avg").toString());
            displayProfileData(currentMatch.getId().toString());
            //   mDialog.dismiss();
            btnPreviousMatch.setEnabled(true);

        } else {
            btnNextMatch.setEnabled(false);
            mDialog.setMessage("No more matches to show!");
        }
    }

    private void getPreviousMatch() {/*show the previous match's details and ratings*/
        if (matchesIterator.hasPrevious()) {

            currentMatch = matchesIterator.previous();
            matchOverAllRating.setText(currentMatch.get("player_rating").toString());
            matchTeamPlayer.setText(currentMatch.get("rank_var_1_avg").toString());
            matchCamper.setText(currentMatch.get("rank_var_2_avg").toString());
            matchStriker.setText(currentMatch.get("rank_var_3_avg").toString());
            displayProfileData(currentMatch.getId().toString());
            //   mDialog.dismiss();
            btnNextMatch.setEnabled(true);
        } else {
            btnPreviousMatch.setEnabled(false);
            mDialog.setMessage("No matches to show!");
        }
    }

    private String getUserName() {/*retrieves the username of the current user from the database*/
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);
        return restoredText;
    }

    /*private void setmatchdata() {

    }*/

    private void getPlayerData() {/*extracting the data from firestore(username and gamename)*/
        username = getUserName();
        game = HomeScreenMatches.this.getActivity().getIntent().getStringExtra("gamename");

        CollectionReference collection = mFirestore.collection("user_rankings/" + game + "/rankings");//in user_rankings/'game'/rankinngs
        collection.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mDialog = new ProgressDialog(HomeScreenMatches.this.getActivity());
              //  mDialog.setMessage("getting user data");
                //mDialog.show();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {//player rating assigned to mPlayerRating
                        mPlayerRating = Integer.parseInt(document.get("player_rating").toString());
                    } else {
                        Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player ratings.",
                                Toast.LENGTH_SHORT).show();
                    }
                    getdata();
                }


            }

        });
    }


    private void displayProfileData(final String uname) {/*fetch the profile data from firestore and display on the xml layout file*/

        CollectionReference collection = mFirestore.collection("users/" + uname + "/gamer_profiles");
        collection.document(game).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player preferences." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {


                        matchUserName.setText(uname);
                        matchAge.setText(checkNull(document.get(Globals.GAMER_AGE_PREF)).toString());
                        matchLanguage.setText(checkNull(document.get(Globals.GAMER_LANGUAGE_PREF)).toString());
                        matchTime.setText(checkNull(document.get(Globals.GAMER_TIME_PREF)).toString());


                    } else {
                        Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player preferences.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                mDialog.dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {


            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player preferences." + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(HomeScreenMatches.this.getActivity(), "Error reading player preferences cancelled.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String checkNull(Object o) {/*making sure that null values are not entered*/
        if (o == null)
            return "";
        else
           return o.toString();


    }
}

