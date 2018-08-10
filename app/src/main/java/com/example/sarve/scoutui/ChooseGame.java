package com.example.sarve.scoutui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseGame extends AppCompatActivity {

    Button btnGameOne, btnGameTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        btnGameOne = (Button)findViewById(R.id.btnGameOne);
        btnGameTwo = (Button)findViewById(R.id.btnGameTwo);



        btnGameOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){



                Intent homeScreen = new Intent(ChooseGame.this,GameProfileCreation.class);
                startActivity(homeScreen);
            }
        });

        btnGameTwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent homeScreen = new Intent(ChooseGame.this,HomeScreen.class);
                startActivity(homeScreen);
            }
        });
    }
}
