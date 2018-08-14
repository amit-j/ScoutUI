package com.example.sarve.scoutui.Model;

import android.content.Intent;
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

import com.example.sarve.scoutui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.CollectionReference;

public class HomeScreenRatePlayer extends AppCompatActivity {
    private Button submit_button;
    private Switch switch1, switch2, switch3;
    private String username, game;
    private FirebaseFirestore mFirestore;
    private TextView imgGameImage_rating,
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

    private void displayDat(){

      /*  CollectionReference collection = mFirestore.collection("user_rankings/"+game+"/rankings");
        collection.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {


                        .setText(document.get("player_rating").toString());
                        .setText(document.get("rank_var_1_avg").toString());
                        .setText(document.get("rank_var_2_avg").toString());
                        .setText(document.get("rank_var_3_avg").toString());




                    }
                    else{
                        Toast.makeText(HomeScreenProfile.this.getActivity(), "Error reading player data.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                mDialog.setProgress(0);
            }

        });*/
    }
}
