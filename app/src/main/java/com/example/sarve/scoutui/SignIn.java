package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sarve.scoutui.Model.Globals;
import com.example.sarve.scoutui.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.opencensus.tags.Tag;

public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnSignIn;
    private FirebaseFirestore mFirestore;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        mFirestore = FirebaseFirestore.getInstance();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Logging in..");
                mDialog.setProgress(0);

                mDialog.show();

                final DocumentReference docRef = mFirestore.collection("users").document(editPhone.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists())
                            {
                                DocumentReference pref = mFirestore.collection("users").document(editPhone.getText().toString());
                                pref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot pwd = task.getResult();
                                            if(pwd.exists() && pwd.get("password").equals(editPassword.getText().toString()))  //fixed the sign in bug and verified passwords correct data : amit J
                                            {

                                                //adding usernames to the shared preference so we can access it later : amit J
                                                SharedPreferences.Editor editor = getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE).edit();
                                                editor.putString("username", editPhone.getText().toString());
                                                editor.apply();


                                                SharedPreferences prefs = getSharedPreferences(Globals.SCOUT_PREFERENCENAME, MODE_PRIVATE);
                                                String restoredText = prefs.getString("username",null);

                                                Intent i = new Intent(SignIn.this,InitialSetup.class);
                                                startActivity(i);
                                                //Toast.makeText(SignIn.this,"pwd correct",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(SignIn.this,"pwd incorrect",Toast.LENGTH_SHORT).show();
                                                mDialog.dismiss();
                                            }
                                        }
                                    }
                                });

                                }

                                else{
                                Toast.makeText(SignIn.this,"check your phone number",Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        }
                    }
                });



            }
        });

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait. We Appreciate Your Patience :)");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(editPassword.getText().toString())) {
                                //Toast.makeText(SignIn.this, "Sign In Was Successful :)", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignIn.this, InitialSetup.class));
                            } else {
                                Toast.makeText(SignIn.this, "Sign In Failed :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User Not Found In Database!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
*/
    }
}
