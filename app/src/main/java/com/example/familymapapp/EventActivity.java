package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.activity_event);

        if (fragment == null) {
            fragment = new MapsFragment();
            fm.beginTransaction()
                    .add(R.id.activity_event, fragment)
                    .commit();
        }
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}