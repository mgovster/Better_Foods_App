package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FoodHoldings{
    public String name;
    public ImageView picture;
    public String ingredients;
    public double calories;
    FoodHoldings(String n, ImageView p, String i, double cc){
        name=n;
        picture=p;
        ingredients=i;
        calories=cc;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public void setPicture(ImageView picture) {
        this.picture = picture;
    }
}


public class FoodActivityPage extends AppCompatActivity {
//or the food output page
    EditText txt;
    int textCount;

    Map<Integer, FoodHoldings> myList;      //each one will have a slot for picture, name, ingredients, calories
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);
        myList = new HashMap<Integer, FoodHoldings>();      //will be a list of integer (to search id number),
        textCount=0;

        String[] ingredients = getIntent().getStringArrayExtra("EXTRA_INGREDIENTS");
        String[] allergies = getIntent().getStringArrayExtra("EXTRA_ALLERGY");
        String word = "";

        /*
        for(int i = 0; i < ingredients.length; i++){
            word += ingredients[i] + "\n";
        }*/

        TextView testView = (TextView)findViewById(R.id.MainConstraint);
        //testView.setText(ingredients.length);

        Button backBtn = (Button)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                backToInput();
            }
        });




    }
    public void enterFoodOptions(int amount, List<FoodHoldings> options){   //will take parameters of: int amount of items, list of FoodHoldings objects
        if(options.isEmpty()){
            //noFoodSubstitute();       //void function to update page to no substitute found (IMPLEMENT LATER)
            return;
        }
        for(textCount = 0; textCount < amount; textCount++){
            myList.put(textCount,options.get(textCount));
        }
    }
    public void backToInput(){
        Intent inent = new Intent(this, FoodInputPage.class);
        startActivity(inent);
    }
}