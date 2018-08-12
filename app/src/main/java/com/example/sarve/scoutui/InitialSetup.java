package com.example.sarve.scoutui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialSetup extends AppCompatActivity {

    Button btnLetsGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        btnLetsGo = (Button)findViewById(R.id.btnLetsGo);

        btnLetsGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent letsGo = new Intent(InitialSetup.this,ChooseGame.class);
                startActivity(letsGo);
            }
        });
    }


    public void onBackPressed() {
       //dont go anywhere@!

    }
}
