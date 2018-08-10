package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private String gameName = "fortnite";
    private String userName = "5622847102";
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirestore();

        //This call should be done at game selection screen with a wait bar!
     // doesProfileAlreadyExist(gameName);

        setContentView(R.layout.activity_game_profile_creation);

        btnCreateGameProfile = findViewById(R.id.btnCreateGameProfile);
        spinnerAgeGroup = findViewById(R.id.preferredAgeGroupSpinner);
        spinnerAgeGroup.setItems("0-10", "11-16", "17-21", "21+");



        spinnerPreferedTime = findViewById(R.id.preferredPlaytimeSpinner);
        spinnerPreferedTime.setItems("Morning", "Afternoons", "Evening", "Late Nights");



       spinnerPreferedLanguage = findViewById(R.id.preferredLangaugepSpinner);
       spinnerPreferedLanguage.setItems("English", "Spanish", "French", "Hindi");





        btnCreateGameProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final ProgressDialog mDialog = new ProgressDialog(GameProfileCreation.this);
                mDialog.setMessage("Please Wait. We Appreciate Your Patience :)");
                mDialog.show();
                saveProfile();
               // mDialog.dismiss();
            }
        });
    }




    private void doesProfileAlreadyExist(String gameName){
        CollectionReference gameProfile = mFirestore.collection("users");
        Task snapshot = gameProfile.document(userName + "/game_profiles/"+gameName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {
                        goToHomeScreen();
                    }
                    else{
                        Toast.makeText(GameProfileCreation.this, "Profile does not exist!",
                                Toast.LENGTH_SHORT).show();
                    }
                    }
            }

        });


    }




    private void initFirestore(){

        mFirestore = FirebaseFirestore.getInstance();
    }

    private void saveProfile(){
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

            gameProfile.document(userName + "/game_profiles/"+gameName).set(newProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(GameProfileCreation.this, "Profile saved.",
                                    Toast.LENGTH_SHORT).show();
                            goToHomeScreen();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GameProfileCreation.this, "ERROR saving profile." +e.toString(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });


        }
        catch (Exception ex){
            Toast.makeText(GameProfileCreation.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
        }




    }



    private void goToHomeScreen(){
        Intent homeScreen = new Intent(GameProfileCreation.this,HomeScreen.class);
        startActivity(homeScreen);
    }
}
