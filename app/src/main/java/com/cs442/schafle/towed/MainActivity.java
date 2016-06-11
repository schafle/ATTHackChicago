package com.cs442.schafle.towed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    EditText vehicleNumber;
    TextView display_text;
    String vehicleNumberValue = "";
    FancyButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vehicleNumber = (EditText) findViewById(R.id.vehicle_number);
        display_text = (TextView) findViewById(R.id.text12);
        btnSubmit = (FancyButton) findViewById(R.id.btnSubmit);
        display_text.setVisibility(View.INVISIBLE);
        //OnCLick Listener Event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Submitted successfully!", Toast.LENGTH_SHORT).show();
                display_text.setText(vehicleNumber.getText());           //Resetting the form
                vehicleNumber.setVisibility(View.INVISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);
                display_text.setVisibility(View.VISIBLE);
            }
        });
    }
}
