/*Activity for home profile creation*/
package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class GameProfileCreation extends AppCompatActivity {

    Button btnCreateGameProfile;
    MaterialSpinner spinnerAgeGroup;
    MaterialSpinner spinnerPreferedTime;
    MaterialSpinner spinnerPreferedLanguage;


    //Temp strings for testing
    private String gameName ;
    private String userName;
    FirebaseFirestore mFirestore;

    TextView txtGameName;
    ImageView imgGameImage;

     ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirestore();

        //This call should be done at game selection screen with a wait bar!

        setContentView(R.layout.activity_game_profile_creation);

         mDialog = new ProgressDialog(GameProfileCreation.this);
        mDialog.setMessage("Please wait while we set things up");//dialog box showing some info
        mDialog.setProgress(0);

        mDialog.show();


        txtGameName = findViewById(R.id.txtGamename);

        imgGameImage = findViewById(R.id.imgGameImage);

        gameName = getIntent().getStringExtra("gamename");/*retrieves gamename */
        if(gameName.equals("pubg")){/*checks the gamename and displays that particular image and text accordingly*/
            imgGameImage.setImageResource(R.mipmap.pubg);
            txtGameName.setText("PUBG");
        }
        else{
            imgGameImage.setImageResource(R.mipmap.fortnite);
            txtGameName.setText("FORTNITE");
        }


        userName =getUserName();//gets username



        doesProfileAlreadyExist(gameName);//checks if profile already exists for the particular game





        //fill spinners from firestore



        btnCreateGameProfile = findViewById(R.id.btnCreateGameProfile);
/*drop downs for each of the preferences in game profile creation screen*/
        spinnerAgeGroup = findViewById(R.id.preferredAgeGroupSpinner);
        fillSpinner(spinnerAgeGroup,"age");

        spinnerPreferedTime = findViewById(R.id.preferredPlaytimeSpinner);
        fillSpinner(spinnerPreferedTime,"time");



        spinnerPreferedLanguage = findViewById(R.id.preferredLangaugepSpinner);
        fillSpinner(spinnerPreferedLanguage,"languages");


      btnCreateGameProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(validateScreen()) {/*to check if gamerID is being entered or not*/
                    ProgressDialog progressBar = new ProgressDialog(GameProfileCreation.this);
                    progressBar.setMessage("Saving your profile.");
                    progressBar.show();
                    progressBar.setProgress(0);
                    saveProfile();
                    progressBar.setProgress(100);
                }
                else
                    Toast.makeText(getBaseContext(), "Please enter a gamerID to be saved to your profile.",    Toast.LENGTH_SHORT).show();
            }
        });


    }




    private void doesProfileAlreadyExist(String gameName){/*check if profile exists or not*/
        CollectionReference gameProfile = mFirestore.collection("users");
        Task snapshot = gameProfile.document(userName + "/gamer_profiles/"+gameName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {
                        goToHomeScreen();
                    }
                    else{
                      //do nothing!
                    }
                    }

                mDialog.dismiss();
            }

        });


    }




    private void initFirestore(){

        mFirestore = FirebaseFirestore.getInstance();
    }



    private boolean validateScreen(){

        MaterialEditText edtgamerID = findViewById(R.id.gamerID);/*gamerID is entered or not*/
        if(edtgamerID.getText().toString().trim().isEmpty())
            return false;

        return true;
    }


    private void saveProfile(){/*stores the gamer profile details into database*/
        boolean isSuccessful;
        try {
            String gamerID;
            String ageGroup;
            String Language;
            String availableTime;

            MaterialEditText edtgamerID = findViewById(R.id.gamerID);
            gamerID = edtgamerID.getText().toString();

            MaterialSpinner edtPreferedLanguage = findViewById(R.id.preferredLangaugepSpinner);
            Language = edtPreferedLanguage.getText().toString();

            MaterialSpinner edtAvailableTime = findViewById(R.id.preferredPlaytimeSpinner);
            availableTime = edtAvailableTime.getText().toString();

            ageGroup = spinnerAgeGroup.getText().toString();


            // Get a reference to the restaurants collection
            CollectionReference gameProfile = mFirestore.collection("users");

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
        catch (Exception ex){
            Toast.makeText(GameProfileCreation.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
        }




    }



    private void createRankings(){/*assigns the default ratings for the gamer's profile*/
        boolean isSuccessful;
        try {



            // Get a reference to the restaurants collection
            CollectionReference gameProfile = mFirestore.collection("user_rankings/"+gameName+"/rankings");

            Map<String, Object> newProfile = new HashMap<>();
            newProfile.put("rank_var_1_avg", 0);
            newProfile.put("rank_var_1_positive", 0);
            newProfile.put("rank_var_1_total", 0);
            newProfile.put("rank_var_2_avg", 0);
            newProfile.put("rank_var_2_positive", 0);
            newProfile.put("rank_var_2_total", 0);
            newProfile.put("rank_var_3_avg", 0);
            newProfile.put("rank_var_3_positive", 0);
            newProfile.put("rank_var_3_total", 0);
            newProfile.put("player_rating", 0);


            gameProfile.document(userName).set(newProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        //    Toast.makeText(GameProfileCreation.this, "Profile saved.",
                              //      Toast.LENGTH_SHORT).show();
                          //  goToHomeScreen();


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
        catch (Exception ex){
            Toast.makeText(GameProfileCreation.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
        }



    }

/*function that takes to home screen*/
    private void goToHomeScreen(){
        Intent homeScreen = new Intent(GameProfileCreation.this,HomeScreen.class);
        homeScreen.putExtra("gamename",gameName);
        startActivity(homeScreen);
    }



    private void fillSpinner(final MaterialSpinner mSpinner, String preference){

        CollectionReference collection = mFirestore.collection("preferences");
        collection.document(preference).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {

                        mSpinner.setItems( document.getData().values().toArray());


                    }
                    else{
                        Toast.makeText(GameProfileCreation.this, "Error reading preference data from database.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }



    private String getUserName(){
        SharedPreferences prefs = getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }
}
