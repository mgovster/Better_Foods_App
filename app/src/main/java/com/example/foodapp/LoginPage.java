package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginPage extends AppCompatActivity {
    private Button login,mCreateAccount, mForgotPass;
    private EditText mUsername, mPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "BetterFoodApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mUsername = (EditText) findViewById(R.id.Emailtxt);
        mPassword = (EditText) findViewById(R.id.Passwordtxt);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.loginBut);
        login.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v) {
                                         signIn(mUsername.getText().toString(),mPassword.getText().toString());
                                     }
                                 }
        );
        mCreateAccount = (Button) findViewById(R.id.CreateAccountBut);
        mCreateAccount.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        createAccount(mUsername.getText().toString(),mPassword.getText().toString());
                    }
        });
        mForgotPass = (Button) findViewById(R.id.ForgotPassBut);
        mForgotPass.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        sendPasswordReset(mUsername.getText().toString());
                    }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //openFoodInputPage(currentUser);
    }

    public void openFoodInputPage(FirebaseUser f){
        if(f == null)
            return;
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("USERID", f.getUid());
        startActivity(intent);
    }

    public void sendPasswordReset(String email) {
        // [START send_password_reset]
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // [END send_password_reset]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            openFoodInputPage(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            openFoodInputPage(null);

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            openFoodInputPage(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            openFoodInputPage(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

}