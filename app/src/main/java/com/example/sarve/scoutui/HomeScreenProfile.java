package com.example.sarve.scoutui;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class HomeScreenProfile extends Fragment {
    Button btnLookForMatches, btnChangeGame, btnLogOut;
    private String username;
    private String game ;
    private FirebaseFirestore mFirestore;
    private TextView txtPlayerRatingValue,
                     txtRatingOneValue,
                     txtRatingTwoValue,
                     txtRatingThreeValue,
                     txtGamename;

    private ImageView gamePicture;
    ProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDialog = new ProgressDialog(HomeScreenProfile.this.getActivity());
        mDialog.setMessage("Please wait");
        mDialog.setProgress(0);

        View rootView = inflater.inflate(R.layout.fragment_home_screen_profile, container, false);

        btnLookForMatches =  rootView.findViewById(R.id.btnLookForMatches);
        btnLookForMatches.setVisibility(View.INVISIBLE);
         txtPlayerRatingValue = rootView.findViewById(R.id.overAllRatingValue);
         btnChangeGame=rootView.findViewById(R.id.btnChangeGame);
         btnLogOut=rootView.findViewById(R.id.btnLogOut);
        txtRatingOneValue= rootView.findViewById(R.id.teamPlayerValue);
        txtRatingTwoValue = rootView.findViewById(R.id.camperValue);
        txtRatingThreeValue = rootView.findViewById(R.id.strikerValue);
        txtGamename = rootView.findViewById(R.id.txtGamename);
        gamePicture = rootView.findViewById(R.id.imgGameImage);


        username = getUserName();
       //
       mFirestore = FirebaseFirestore.getInstance();
        game= HomeScreenProfile.this.getActivity().getIntent().getStringExtra("gamename");
        txtGamename.setText(game.toUpperCase());

        if(game.equals("pubg")){
            gamePicture.setImageResource(R.mipmap.pubg);
        }
        else{
            gamePicture.setImageResource(R.mipmap.fortnite);
        }


        displayProfileData();
        setButtonListners();


        return rootView;



    }



    private void setButtonListners(){

        btnChangeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreenProfile.this.getActivity(), ChooseGame.class);
                startActivity(i);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreenProfile.this.getActivity(), SignIn.class);
                startActivity(i);

            }
        });

        btnLookForMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    /*private void test(){

        CollectionReference collection = mFirestore.collection("user_rankings/pubg/rankings");
    Query q =       collection.whereLessThan("total_rank",70);
    q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot snapshots) {


            for(DocumentSnapshot document:snapshots.getDocuments()) {

                Toast.makeText(HomeScreenProfile.this.getActivity(), "User:"+document.getId(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeScreenProfile.this.getActivity(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
            );


    }
*/

    private void displayProfileData(){

        CollectionReference collection = mFirestore.collection("user_rankings/"+game+"/rankings");
        collection.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                mDialog.setProgress(0);
            }

        });
    }



/*    public HomeScreenProfile(FirebaseFirestore fire){
        mFirestore  = fire;
    }
*/

    private String getUserName(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }

}
