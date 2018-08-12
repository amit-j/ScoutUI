package com.example.sarve.scoutui;

import android.content.Intent;
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

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarve.scoutui.Model.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class HomeScreenProfile extends Fragment {
    Button btnLookForMatches, btnChangeGame, btnLogOut;

    private String username;
    private String game = "pubg";
    private FirebaseFirestore mFirestore;
    private TextView txtPlayerRatingValue,
                     txtRatingOneValue,
                     txtRatingTwoValue,
                     txtRatingThreeValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen_profile, container, false);

        btnLookForMatches = (Button) rootView.findViewById(R.id.btnLookForMatches);
         txtPlayerRatingValue = rootView.findViewById(R.id.overAllRatingValue);
        txtRatingOneValue= rootView.findViewById(R.id.teamPlayerValue);
        txtRatingTwoValue = rootView.findViewById(R.id.camperValue);
        txtRatingThreeValue = rootView.findViewById(R.id.strikerValue);

        username = getUserName();
        mFirestore = FirebaseFirestore.getInstance();

        displayProfileData();

        return rootView;
    }


    private void displayProfileData(){

        CollectionReference collection = mFirestore.collection("users/"+username+"/gamer_profiles");
        collection.document(game).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {


                        txtPlayerRatingValue.setText(document.get("player_rating").toString());
                        txtRatingOneValue.setText(document.get("rank_var_1_avg").toString());
                        txtRatingTwoValue.setText(document.get("rank_var_2_avg").toString());
                        txtRatingThreeValue.setText(document.get("rank_var_3_avg").toString());




                    }
                    else{
                        Toast.makeText(HomeScreenProfile.this.getActivity(), "Error reading player data.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }





    private String getUserName(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }

}
