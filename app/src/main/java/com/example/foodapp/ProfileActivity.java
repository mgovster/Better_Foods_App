package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;



public class ProfileActivity extends AppCompatActivity {

    private EditText mNameEditText, mEmailEditText, mPasswordEditText;
    private List<CheckBox> mAllergyCheckBoxes;
    private FirebaseUser mUser;
    private Button mSaveButton;
    private static final String TAG = "BFA::ProfileActivity";
    private DatabaseReference mDatabase;
    private String mAllergyBinary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);
        populateAllergens();

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mNameEditText.setText(mUser.getDisplayName());
        mEmailEditText.setText(mUser.getEmail());


        mSaveButton = (Button) findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                saveTextToDataBase(mNameEditText.getText().toString(), mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                                setAllergyBinary();
                                saveAllergyBinary(mAllergyBinary);
                                goToHome();
                            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saving Profile", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                goToHome();
            }
        });



    }

    private void goToHome(){
        Intent intent = new Intent(this, FoodInputPage.class);
        intent.putExtra("binary",mAllergyBinary);
        startActivity(intent);
    }

    private void saveAllergyBinary(String b) {
        mDatabase.child("users").child(mUser.getUid()).setValue(b)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Binary string updated.");
                        }
                    }
                });
    }

    private void saveTextToDataBase(String name, String email, String password) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        if(mNameEditText.getText() != null) {
            mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User name updated.");
                            }
                        }
                    });
        }
        if(mEmailEditText.getText() != null) {
            mUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });
        }

        if(mPasswordEditText.getText() != null) {
            mUser.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                            }

                        }
                    });
        }
    }


    private void populateAllergens(){
        setAllergyBinaryFromDatabase();
        CheckBox dairy = (CheckBox)findViewById(R.id.dairyCBtn);
        CheckBox eggs = (CheckBox)findViewById(R.id.eggsCBtn);
        CheckBox fish = (CheckBox)findViewById(R.id.fishCBtn);
        CheckBox shellfish = (CheckBox)findViewById(R.id.shellfishCBtn);
        CheckBox treenuts = (CheckBox)findViewById(R.id.treenutsCBtn);
        CheckBox peanuts = (CheckBox)findViewById(R.id.peanutsCBtn);
        CheckBox gluten = (CheckBox)findViewById(R.id.wheatCBtn);
        CheckBox soy = (CheckBox)findViewById(R.id.soyCBtn);
        CheckBox vegan = (CheckBox)findViewById(R.id.veganCBtn);
        CheckBox vegetarian = (CheckBox)findViewById(R.id.vegetarianCBtn);
        CheckBox pescatarian = (CheckBox)findViewById(R.id.pescatarianCBtn);

        mAllergyCheckBoxes = new ArrayList<CheckBox>();
        mAllergyCheckBoxes.add(dairy);
        mAllergyCheckBoxes.add(eggs);
        mAllergyCheckBoxes.add(fish);
        mAllergyCheckBoxes.add(shellfish);
        mAllergyCheckBoxes.add(treenuts);
        mAllergyCheckBoxes.add(peanuts);
        mAllergyCheckBoxes.add(gluten);
        mAllergyCheckBoxes.add(soy);
        mAllergyCheckBoxes.add(vegan);
        mAllergyCheckBoxes.add(vegetarian);
        mAllergyCheckBoxes.add(pescatarian);
        Log.d(TAG,"mAllegeryBinary = " + mAllergyBinary);
        if(mAllergyBinary == null)
            return;
        char[] c = mAllergyBinary.toCharArray();
        int i = 0;
        for(CheckBox box : mAllergyCheckBoxes){

            if(c[i++] == '1')
                box.setChecked(true);

        }
    }

    private void setBinary(String b){
        mAllergyBinary = b;
    }

    private void setAllergyBinaryFromDatabase() {
        DatabaseReference ref = mDatabase.child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bin = (String) dataSnapshot.child(mUser.getUid()).getValue();
                setBinary(bin);
                Log.d(TAG, "binary set to " + bin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,  "The read failed: " + error.getMessage());
            }
        });
    }


    private void setAllergyBinary(){
        StringBuilder binary = new StringBuilder();
        for(CheckBox box : mAllergyCheckBoxes){
            if(box.isChecked())
                binary.append("1");
            else
                binary.append("0");
        }
        mAllergyBinary = binary.toString();
    }

}