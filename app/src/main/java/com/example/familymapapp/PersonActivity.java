package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    public ExpandableListView expandableListView;
    public ExpandableListAdapter expandableListAdapter;
    public List<String> headers;
    private TextView nombre;
    private TextView apellido;
    private TextView gender;
    private ImageView icon;
    public HashMap<String, List<String>> expandableListDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        nombre = (TextView) findViewById(R.id.person_activity_nombre);
        apellido = (TextView) findViewById(R.id.person_activity_apellido);
        gender = (TextView) findViewById(R.id.person_activity_gender);
        icon = (ImageView) findViewById(R.id.person_activity_icon);
        setPersonDetails(DataCache.getInstance().getActivityPerson());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        PersonExpandableListDataPump pump = new PersonExpandableListDataPump();
        pump.getData(DataCache.getInstance().getActivityPerson());
        expandableListDetail = pump.getExpandableListDetail();
        headers = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new PersonExpandableListAdapter(this, headers, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                HashMap<Integer, Person> mFamilyPersons =  DataCache.getInstance().getFamily();
                HashMap<Integer, Event> mPersonLifeEvents = DataCache.getInstance().getPersonLifeEvents();

                if(i == 1){
                    Event event = (Event)DataCache.getInstance().getPersonLifeEvents().get(i1);
                    DataCache.getInstance().setActivityEvent(event);
                    startNewActivity(true);
                    System.out.println("Event you clicked on: " + event.toString());
                }
                else{
                    //User has selected a person. Create a new PersonActivity
                    Person person = (Person)DataCache.getInstance().getFamily().get(i1);
                    DataCache.getInstance().setActivityPerson(person);
                    startNewActivity(false);
                    System.out.println("Person you clicked on: " + person.toString());
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed(){
        finish();
    }
    private void setPersonDetails(Person person){
        nombre.setText(person.getFirstName());
        apellido.setText(person.getLastName());
        if(person.getGender().equals("m")){
            gender.setText("Male");
            icon.setImageResource(R.drawable.man);
        }
        else{
            gender.setText("Female");
            icon.setImageResource(R.drawable.woman);
        }
    }

    public void startNewActivity(boolean eventActivity){
        Intent intent;
        if(eventActivity) {
            intent = new Intent(this, EventActivity.class);
        }
        else{
            intent = new Intent(this, PersonActivity.class);
        }
        startActivity(intent);
    }
}