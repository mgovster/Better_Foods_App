package com.example.foodapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodInputPage extends AppCompatActivity {
EditText txt;
int textCount, allergyNum, allegiesChecked;
Map<Integer,EditText> myList;
List<String> ingredients;
List<String> allergies;
Map<Integer,Boolean> allergySelection;
List<CheckBox> allegButs;
String mUserID;
FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserID = getIntent().getStringExtra("USERID");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        myList = new HashMap<Integer, EditText>();  //myList of text views will ignore the one, so as long as this has elements, it can remove
        textCount=1;
        allergyNum = 11;
        allegiesChecked=0;
        allergySelection = new HashMap<Integer, Boolean>();
        setContentView(R.layout.activity_food_input_page);
        Button submitButt = (Button)findViewById(R.id.submitBtn);
        submitButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFoodActivityPage();
            }
        });

        Button addButt = (Button)findViewById(R.id.addButton);
        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextField();
            }
        });

        Button remButt = (Button)findViewById(R.id.remButton);
        remButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                removeTextView();
            }
        });

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

        allegButs = new ArrayList<CheckBox>();
        allegButs.add(dairy);
        allegButs.add(eggs);
        allegButs.add(fish);
        allegButs.add(shellfish);
        allegButs.add(treenuts);
        allegButs.add(peanuts);
        allegButs.add(gluten);
        allegButs.add(soy);
        allegButs.add(vegan);
        allegButs.add(vegetarian);
        allegButs.add(pescatarian);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Saving Profile", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                goToProfile();
//            }
//        });




    }

    private void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"Welcome " + mUser.getDisplayName(),Toast.LENGTH_SHORT);
    }

    public void getAllergies(){
        Boolean checkDaBox;
        for(int i = 0; i < allergyNum; i++){
            checkDaBox = allegButs.get(i).isChecked();
            if(checkDaBox && allegButs.get(i) != null){
                allergies.add(allegButs.get(i).getText().toString());
                allegiesChecked++;
            }

        }


    }
    public void getIngredients(){
        String wurd ="";
        for (int i =0; i<myList.size(); i++){
            wurd = myList.get(i).getText().toString();
            ingredients.add(wurd);
        }

    }

    public void openFoodActivityPage(){
        //add extra bits as list of strings as ingredients, list of strings for allergies,
        getAllergies();
        getIngredients();


        Intent intent = new Intent(this, FoodActivityPage.class);

        String[] ing = new String[textCount];
        String[] cc = new String[allegiesChecked];

        if(!allergies.isEmpty()){
            for(int i=0; i < allergies.size(); i++){
                cc[i] = allergies.get(i);
            }
        }
        if(!ingredients.isEmpty()){
            for(int j=0; j < ingredients.size(); j++){
                ing[j] = ingredients.get(j);
            }
        }



        intent.putExtra("EXTRA_INGREDIENTS", ing);
        intent.putExtra("EXTRA_ALLERGY", cc);

        startActivity(intent);

    }

    public void removeTextView(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.ingredientLayout);
        if (!(myList.isEmpty())){
            int size = myList.size();
            layout.removeView(myList.get(size));
            myList.remove(size);
            textCount-=1;
        }
    }

    public void addTextField(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.ingredientLayout);
        txt = new EditText(this);
        txt.setText("");
        layout.addView(txt);
        myList.put(textCount,txt);
        textCount+=1;

    }
}