package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

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
        setTitle("Family Map: Person Details");

        nombre = (TextView) findViewById(R.id.person_activity_nombre);
        apellido = (TextView) findViewById(R.id.person_activity_apellido);
        gender = (TextView) findViewById(R.id.person_activity_gender);
        icon = (ImageView) findViewById(R.id.person_activity_icon);
        setPersonDetails(DataCache.getInstance().getActivityPerson());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        PersonDetailInserter pump = new PersonDetailInserter();
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
                if (i == 1){
                    Person person = (Person)DataCache.getInstance().getFamily().get(i1);
                    DataCache.getInstance().setActivityPerson(person);
                    startNewActivity(false);
                }
                else{
                    Event event = (Event)DataCache.getInstance().getPersonLifeEvents().get(i1);
                    DataCache.getInstance().setActivityEvent(event);
                    startNewActivity(true);


                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed(){
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public String eventToString(Event e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ");
        sb.append(e.getEventID());
        sb.append(", ");
        sb.append("Event Type: ");
        sb.append(e.getEventType());
        sb.append(", ");
        sb.append("Event User: ");
        sb.append(e.getAssociatedUsername());
        sb.append(", ");
        sb.append("Event Year: ");
        sb.append(e.getYear());
        return sb.toString();
    }
    public String personToString(Person e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Person ID: ");
        sb.append(e.getPersonID());
        sb.append(", ");
        sb.append("Person Name: ");
        sb.append(e.getFirstName());
        sb.append(" ");
        sb.append(e.getLastName());
        sb.append(", ");
        sb.append("Person User: ");
        sb.append(e.getAssociatedUsername());
        sb.append(", ");
        sb.append("Person Gender: ");
        sb.append(e.getGender());
        return sb.toString();
    }
}