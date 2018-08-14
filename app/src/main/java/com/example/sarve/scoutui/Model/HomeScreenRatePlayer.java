package com.example.sarve.scoutui.Model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarve.scoutui.GameProfileCreation;
import com.example.sarve.scoutui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.CollectionReference;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class HomeScreenRatePlayer extends AppCompatActivity {
    private Button submit_button;
    private Switch switch1, switch2, switch3;
    private String username, game;
    private FirebaseFirestore mFirestore;
    private TextView imgGameImage_rating,gameName,
            gamename_rating,
            description_rating,
            var_1_name,
            var_1_rating,
            var_2_name,
            var_2_rating,
            var_3_name,
            var_3_rating;
    private ImageView gamePicture;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_rate_player);
        TextView txtGameName = findViewById(R.id.gamename_rating);
        txtGameName.setText(getIntent().getStringExtra("gamename"));
        game =getIntent().getStringExtra("gamename");
        ImageView imgGameImagae = findViewById(R.id.imgGameImage_rating);
        if(game.equals("pubg")){
            imgGameImagae.setImageResource(R.mipmap.pubg);
        }
        else{
            imgGameImagae.setImageResource(R.mipmap.fortnite);
        }

        Button btnSubmit = findViewById(R.id.btnSubmitRating_rating);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mDialog = new ProgressDialog(HomeScreenRatePlayer.this);
                mDialog.setMessage("Rating Submitted!");
                mDialog.show();
            }
        });

    }
    public void setButtonListners(){

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch1.isChecked()){
                    //save to db w/ value 1
                }else{
                    //save to db w/ value 0
                }
                if(switch2.isChecked()){
                    //save to db 1
                }else{
                    //save to db w/ value 0

                }
                if(switch3.isChecked()) {
                    //save to db 1
                }else{
                    //save to db w/ value 0

                }
            }
        });

    }

    private void saveMatchData(){


        // Get a reference to the restaurants collection
        CollectionReference gameProfile = mFirestore.collection("users/"+username);

        Map<String, Object> newProfile = new HashMap<>();
        newProfile.put(Globals.GAMERID_KEY, gamerID);
        newProfile.put(Globals.GAMER_LANGUAGE_PREF, Language);
        newProfile.put(Globals.GAMER_AGE_PREF, ageGroup);
        newProfile.put(Globals.GAMER_TIME_PREF, availableTime);



        gameProfile.document(userName + "/gamer_profiles/"+gameName).set(newProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GameProfileCreation.this, "Profile saved.",
                                Toast.LENGTH_SHORT).show();
                        createRankings();

                        goToHomeScreen();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GameProfileCreation.this, "ERROR saving profile." +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();


                    }
                });


    }







}

    private St ring getUserName(){
        SharedPreferences prefs = getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }

}
