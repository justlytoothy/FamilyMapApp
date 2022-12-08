package com.example.familymapapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapapp.handlers.ResyncHandler;

import request.LoginRequest;

public class SettingsActivity extends AppCompatActivity {

    private Settings mSettings;

    private Switch mLifeStorySwitch;
    private Switch mFamilyTreeSwitch;
    private Switch mSpouseSwitch;

    private Spinner mLifeStoryLineSpinner;
    private Spinner mFamilyTreeSpinner;
    private Spinner mSpouseLineSpinner;
    private Spinner mMapTypeSpinner;

    private ArrayAdapter<CharSequence> mColorMenu;
    private ArrayAdapter<CharSequence> mMapViewMenu;

    private RelativeLayout mResyncButton;
    private RelativeLayout mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setTitle("Family Map: Settings");

        //Initialize the switches
        mLifeStorySwitch = (Switch) findViewById(R.id.settings_activity_lifestory_switch);
        mFamilyTreeSwitch = (Switch) findViewById(R.id.settings_activity_family_switch);
        mSpouseSwitch = (Switch) findViewById(R.id.settings_activity_spouse_switch);

        //Intialize the Spinners
        mLifeStoryLineSpinner = (Spinner) findViewById(R.id.settings_activity_lifestory_spinner);
        mFamilyTreeSpinner = (Spinner) findViewById(R.id.settings_activity_family_spinner);
        mSpouseLineSpinner = (Spinner) findViewById(R.id.settings_activity_spouse_spinner);
        mMapTypeSpinner = (Spinner) findViewById(R.id.settings_activity_maptype_spinner);

        //Initialize the color menu
        mColorMenu = ArrayAdapter.createFromResource(this, R.array.color_values, android.R.layout.simple_spinner_item);
        mColorMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLifeStoryLineSpinner.setAdapter(mColorMenu);
        mFamilyTreeSpinner.setAdapter(mColorMenu);
        mSpouseLineSpinner.setAdapter(mColorMenu);


        //Initialize the map menu
        mMapViewMenu = ArrayAdapter.createFromResource(this, R.array.map_menu, android.R.layout.simple_spinner_item);
        mMapViewMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMapTypeSpinner.setAdapter(mMapViewMenu);

        //Initialize the RelativeLayouts
        mResyncButton = (RelativeLayout) findViewById(R.id.RESYNC);
        mLogoutButton = (RelativeLayout) findViewById(R.id.LOGIN);

        settingsListeners();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Grab the current settings
        mSettings = DataCache.getInstance().getSettings();

        //Set spinners to the correct locations
        String color = mSettings.getLifeStoryLineColor();
        mLifeStoryLineSpinner.setSelection(findPosition(color));
        color = mSettings.getFamilyTreeLineColor();
        mFamilyTreeSpinner.setSelection(findPosition(color));
        color = mSettings.getSpouseLineColor();
        mSpouseLineSpinner.setSelection(findPosition(color));
        String map = mSettings.getMapView();
        mMapTypeSpinner.setSelection(findPosition(map));

        //Set switched to the correct locations
        boolean checked = mSettings.isLifeStoryLinesFilter();
        mLifeStorySwitch.setChecked(checked);
        checked = mSettings.isFamilyTreeLinesFilter();
        mFamilyTreeSwitch.setChecked(checked);
        checked = mSettings.isSpouseLinesFilter();
        mSpouseSwitch.setChecked(checked);

        settingsListeners();
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

    private void settingsListeners(){
        mLifeStoryLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String)parent.getItemAtPosition(position);
                DataCache.getInstance().getSettings().setLifeStoryLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setLifeStoryLinesFilter(isChecked);
            }
        });

        mFamilyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);
                DataCache.getInstance().getSettings().setFamilyTreeLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFamilyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setFamilyTreeLinesFilter(isChecked);
            }
        });

        mSpouseLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String)parent.getItemAtPosition(position);
                DataCache.getInstance().getSettings().setSpouseLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setSpouseLinesFilter(isChecked);
            }
        });

        mMapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mapView = (String)parent.getItemAtPosition(position);
                DataCache.getInstance().getSettings().setMapView(mapView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mResyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resync();
            }
        });
    }

    private void resync(){
        String username = DataCache.getInstance().getUsername();
        String password = DataCache.getInstance().getPassword();

        //Save the settings model
        Settings settings = DataCache.getInstance().getSettings();

        //Clear the model class
        DataCache.getInstance().reset();

        LoginRequest request = new LoginRequest(username, password);

        //Create a new Async task to carry out the API request
        ResyncHandler task = new ResyncHandler(this);

        task.execute(request);

        //Restore previous models
        DataCache.getInstance().setUsername(username);
        DataCache.getInstance().setPassword(password);
        DataCache.getInstance().setSettings(settings);

        //Set Re-sync to true to notify the map
        DataCache.getInstance().setResync(true);
    }

    private void logout(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        finishAffinity();
        finish();
        DataCache.getInstance().reset();
        startActivity(intent);
    }

    private int findPosition(String arg){
        int position;
        switch (arg){
            case "red": position = 0;
                break;
            case "blue": position = 1;
                break;
            case "cyan": position = 2;
                break;
            case "green": position = 3;
                break;
            case "magenta": position = 4;
                break;
            case "yellow": position =5;
                break;
            case "white": position = 6;
                break;
            case "black": position = 7;
                break;
            case "Normal": position = 0;
                break;
            case "Hybrid": position = 1;
                break;
            case "Satellite": position = 2;
                break;
            case "Terrain": position = 3;
                break;
            default: position = 0;
        }

        return position;
    }
}