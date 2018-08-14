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

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarve.scoutui.Model.Globals;
import com.example.sarve.scoutui.Model.HomeScreenRatePlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeScreenRating extends Fragment {

    private ListView listView;
    private FirebaseFirestore mFirestore;
    private ArrayList<DocumentSnapshot> matches;
    private String game ="fortnite";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen_rating, container, false);

        mFirestore = FirebaseFirestore.getInstance();


         listView =  rootView.findViewById(R.id.mobile_list);
        getMatches();



        return rootView;

    }


    private void getMatches(){

        CollectionReference collection = mFirestore.collection("users/"+getUserName()+"/gamer_profiles/"+game+"/matches");
        Query q = collection.limit(25);

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                matches = new ArrayList<>(snapshots.getDocuments());
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
        }
            );




    }


    private String getUserName(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
        String restoredText = prefs.getString("username",null);
        return restoredText ;
    }

    class  CustomAdapter extends BaseAdapter{
        @Override
        public int getCount(){
            if(matches == null)
                return 0;
            return matches.size();
        }

        @Override
        public Object getItem(int i ){
            return null;
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int i,View view,ViewGroup viewGroup){

            if (matches==null)
                return null;
            view =getLayoutInflater().inflate(R.layout.custom_layout,null);
            TextView playerName = view.findViewById(R.id.textView);
            Button rateButton = view.findViewById(R.id.button);
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeScreenRating.this.getActivity(), HomeScreenRatePlayer.class);
                    startActivity(i);
                }
            });
            playerName.setText(matches.get(i).get("gamer_ID").toString());
            return view;
        }


    }
}
