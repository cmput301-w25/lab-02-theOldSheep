package com.example.listycity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // the remove state and what the button is displaying
    enum RemoveState {
        IDLE("Remove"), REMOVING("Done");

        final String btnDisplay;
        RemoveState(String display) {
            this.btnDisplay = display;
        }
        public String getBtnDisplay() {
            return btnDisplay;
        }
    }
    public static final String CITIES_EXTRA_NAME = "cities";

    RemoveState removeState;                // whether to remove items on click
    Button btnAdd, btnRmv;                  // the buttons to add/remove cities
    ListView cityList;                      // the list of all cities
    ArrayAdapter<String> cityAdapter;       // the adapter providing city names
    ArrayList<String> dataList;             // the data of all city names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // elements init
        btnAdd = findViewById(R.id.btn_add);
        btnRmv = findViewById(R.id.btn_rmv);
        cityList = findViewById(R.id.city_list);
        // remove state init
        setRemoveState(RemoveState.IDLE);
        // data init
        dataInit(getIntent());
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        // listeners
        btnAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formIntent = new Intent(
                        MainActivity.this, CityForm.class);
                formIntent.putExtra(CITIES_EXTRA_NAME, dataList);
                startActivity(formIntent);
            }
        });
        btnRmv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRemoveState();
            }
        });
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when removing, drop the item
                if (Objects.requireNonNull(removeState) == RemoveState.REMOVING) {
                    dataList.remove(position);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    // initializes the city data
    private void dataInit(Intent intent) {
        if (intent.hasExtra(CITIES_EXTRA_NAME)) {
            dataList = intent.getStringArrayListExtra(CITIES_EXTRA_NAME);
        }
        else {
            dataList = new ArrayList<>(
                    List.of("Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo",
                            "Beijing", "Osaka", "New Delhi"));
        }
    }
    // updates the state to remove cities
    private void setRemoveState(RemoveState newState) {
        this.removeState = newState;
        if (this.btnRmv != null) {
            this.btnRmv.setText(newState.getBtnDisplay());
        }
    }
    // toggles the state to remove cities
    private void toggleRemoveState() {
        switch (removeState) {
            case IDLE:
                setRemoveState(RemoveState.REMOVING);
                break;
            case REMOVING:
                setRemoveState(RemoveState.IDLE);
                break;
        }
    }
}