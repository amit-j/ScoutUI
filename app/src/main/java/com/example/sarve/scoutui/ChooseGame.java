/*Activity where the user has to choose a game(pubg or fortnite)*/
package com.example.sarve.scoutui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sarve.scoutui.Model.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseGame extends AppCompatActivity {

    Button btnGameOne, btnGameTwo;
    FirebaseFirestore mFirestore;
    private String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        mFirestore = FirebaseFirestore.getInstance();
        btnGameOne = (Button)findViewById(R.id.btnGameOne);  //fortnite
        btnGameTwo = (Button)findViewById(R.id.btnGameTwo); //pubg
/*fortnite*/
        btnGameOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                gameName="fortnite";
                doesProfileAlreadyExist(gameName);
            }
        });
/*pubg*/
        btnGameTwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                gameName="pubg";
                doesProfileAlreadyExist(gameName);
            }
        });
    }

    public void onBackPressed() {
      //dont go anywhere!
    }


    private void doesProfileAlreadyExist(final String gameName){/*to check if the user profile already exists*/
        CollectionReference gameProfile = mFirestore.collection("users");
        Task snapshot = gameProfile.document( getUserName()+ "/gamer_profiles/"+gameName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();/*gets the profiles from the database*/


                    if (document.exists()) {
                        goToHomeScreen();/*if profile already exists then go to home screen, skip game profile creation screen*/
                    }
                    else{/*go to home profile creation screen*/
                        Intent homeScreen = new Intent(ChooseGame.this,GameProfileCreation.class);
                        homeScreen.putExtra("gamename",gameName);
                        startActivity(homeScreen);
                    }
                }


            }

        });


    }

    private String getUserName(){//gets the username of the user
        SharedPreferences prefs = getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }

    private void goToHomeScreen(){/*intent to go to home screen*/
        Intent homeScreen = new Intent(ChooseGame.this,HomeScreen.class);
        homeScreen.putExtra("gamename",gameName);
        startActivity(homeScreen);
    }

}
