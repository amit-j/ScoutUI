package com.example.sarve.scoutui;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sarve.scoutui.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    MaterialEditText editPhone, editName, editPassword;
    Button btnSignUp;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirestore = FirebaseFirestore.getInstance();

        editName = (MaterialEditText) findViewById(R.id.editName);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String password = editPassword.getText().toString();

                Map<String, String> userMap = new HashMap<>();

                userMap.put("name",name);
                userMap.put("phone",phone);
                userMap.put("password",password);



                mFirestore.collection("users").document(editPhone.getText().toString()).set(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Intent i = new Intent(SignUp.this, SignIn.class);

                                Toast.makeText(SignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                               // startActivity(i);
                         //   }
                       // })
                       // .addOnFailureListener(new OnFailureListener() {
                           // @Override
                         //   public void onFailure(@NonNull Exception e) {
                           //     Toast.makeText(SignUp.this, "Please try later!" + e.toString(),
                            //            Toast.LENGTH_SHORT).show();

                            }
                        });

            /*    mFirestore.collection("users").document(editPhone.getText().toString()).set(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                    }
                });
*/
            }
        });
    }

    }



/*        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait. We Appreciate Your Patience :)");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(editPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone Number Already Exists In Database", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(editName.getText().toString(),editPassword.getText().toString());
                            table_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Sign Up Was Successful :)", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
*/