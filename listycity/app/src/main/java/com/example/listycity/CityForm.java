package com.example.listycity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CityForm extends AppCompatActivity {
    Button btnConfirm, btnCancel;       // the buttons to confirm/cancel the creation
    EditText textField;                 // the text field to enter new city name
    ArrayList<String> dataList;         // the list of cities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_city_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_city_form), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataList = getIntent().getStringArrayListExtra(MainActivity.CITIES_EXTRA_NAME);

        // variable init
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        textField = findViewById(R.id.input);

        // listeners
        btnConfirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(true);
            }
        });
        btnCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(false);
            }
        });
    }

    // returns to the main activity; confirmed is true if the form is submitted.
    private void submitForm(boolean confirmed) {
        // do not submit with empty string.
        if (confirmed) {
            if (textField.getText().length() == 0)
                return;
            // add the new city name
            dataList.add(textField.getText().toString());
        }

        Intent formIntent = new Intent(
                CityForm.this, MainActivity.class);
        formIntent.putExtra(MainActivity.CITIES_EXTRA_NAME, dataList);
        startActivity(formIntent);
    }
}
