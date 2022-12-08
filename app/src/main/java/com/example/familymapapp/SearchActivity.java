package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter searchAdapter;

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    private List<SearchResult> searchResults;

    private EditText searchBox;
    private ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Family Map: Search");

        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResults = new ArrayList<>();

        searchBox = (EditText) findViewById(R.id.search_bar);
        icon = (ImageView) findViewById(R.id.search_icon);
        icon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search));

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(searchBox.getText());
                StringBuilder str = new StringBuilder();
                str.append("SearchResults: ");
                for(SearchResult r : searchResults){
                    str.append(" " + r.getInformation());
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setAdapter();
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
    public void search(CharSequence sequence){
        Map<String, Person> persons = DataCache.getInstance().getPeople();
        Map<String, Event> events = DataCache.getInstance().getEvents();

        sequence = sequence.toString().toLowerCase();
        searchResults.clear();
        for(Person person : persons.values()){
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();
            if(firstName.contains(sequence) ||
                    lastName.contains(sequence)){

                StringBuilder resultString = new StringBuilder();
                resultString.append(person.getFirstName() + " " + person.getLastName());

                SearchResult result = new SearchResult(resultString.toString());
                result.setPerson(true);
                result.setEvent(false);
                result.setID(person.getPersonID());
                result.setGender(person.getGender());
                searchResults.add(result);
            }
        }

        for(Event event : events.values()){
            String country = event.getCountry().toLowerCase();
            String city = event.getCity().toLowerCase();
            String type = event.getEventType().toLowerCase();
            String year = String.valueOf(event.getYear()).toLowerCase();
            if(country.contains(sequence) ||
                    city.contains(sequence) ||
                    type.contains(sequence) ||
                    year.contains(sequence)){
                if(Boolean.TRUE.equals(DataCache.getInstance().getFilters().getEventFilter().get(type))) {
                    Person person = persons.get(event.getPersonID());
                    StringBuilder resultString = new StringBuilder();
                    resultString.append(event.getEventType() + ": " + event.getCity() + "  "
                            + event.getCountry() + " (" + event.getYear() + ")\n"
                            + person.getFirstName() + " " + person.getLastName());

                    SearchResult result = new SearchResult(resultString.toString());
                    result.setEvent(true);
                    result.setPerson(false);
                    result.setID(event.getEventID());
                    searchResults.add(result);
                }
            }
        }
    }

    public void setAdapter(){
        searchAdapter = new SearchResultAdapter(this, searchResults);
        recyclerView.setAdapter(searchAdapter);
    }
}