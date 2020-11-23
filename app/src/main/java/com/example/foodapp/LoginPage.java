package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginPage extends AppCompatActivity {
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login = (Button) findViewById(R.id.loginBut);
        login.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v) {
                                         openFoodInputPage();
                                     }
                                 }
        );
    }

    public void openFoodInputPage(){
        Intent intent = new Intent(this, FoodInputPage.class);
        startActivity(intent);
    }

}