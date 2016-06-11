package com.cs442.schafle.towed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    EditText vehicleNumber;
    TextView displayFound;
    TextView displayLocation;
    TextView displayContact;
    FancyButton btnSubmit;
    JSONObject jobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vehicleNumber = (EditText) findViewById(R.id.vehicle_number);
        displayFound = (TextView) findViewById(R.id.display_found);
        displayLocation = (TextView) findViewById(R.id.display_location);
        displayContact = (TextView) findViewById(R.id.display_contact);
        btnSubmit = (FancyButton) findViewById(R.id.btnSubmit);
        displayFound.setVisibility(View.INVISIBLE);
        displayLocation.setVisibility(View.INVISIBLE);
        displayContact.setVisibility(View.INVISIBLE);
        //OnCLick Listener Event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Submitted successfully!", Toast.LENGTH_SHORT).show();
                //display_text.setText(vehicleNumber.getText());           //Resetting the form
                vehicleNumber.setVisibility(View.INVISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);
                String url = "https://data.cityofchicago.org/resource/rp42-fxjt.json?plate="+vehicleNumber.getText();
                new Request().execute(url);
                //Log.d("Towed: ", url);
                //Log.d("Towed: ", jobj.toString());
                displayFound.setVisibility(View.VISIBLE);
                displayLocation.setVisibility(View.VISIBLE);
                displayContact.setVisibility(View.VISIBLE);
            }
        });
    }


    private static class Params {
        String url;

        Params(String url) {
            this.url = url;
        }
    }

    private class Request extends AsyncTask<String, String, String> {
        String url;
        String result = "fail";
        @Override
        protected String doInBackground(String...args) {
            String json_string = "";

            //String url = "http://www.elvenware.com/charlie/development/web/Php/Presidents01.php";
            //String url = "http://www.elvenware.com/charlie/development/web/Php/PresidentsXml01.php";
            // String url = "http://www.elvenware.com/cgi-bin/LatLongReadData.py";
            url = args[0];
            Log.d("Towed:", url);
            BufferedReader inStream = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpRequest = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpRequest);
                inStream = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent()));

                StringBuffer buffer = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = inStream.readLine()) != null) {
                    buffer.append(line + NL);
                }
                inStream.close();

                result = buffer.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute (String json){

            String license_no = "";
            String tow_address = "";
            String tow_phone = "";
            //String relocated_address = "";
            //String relocated_phone = "";
            if (json.isEmpty()) {

                displayFound.setText("DATA NOT FOUND !!");
            } else {
                //Toast.makeText(MainActivity.this,"JSON data KK="+json,Toast.LENGTH_SHORT).show();
                //display_found.setText("Yes->Towed !!");

                try {
                    //if(request_id.equalsIgnoreCase("tow")) {
                    JSONArray arr = new JSONArray(json);

                    //loop through each object
                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonProductObject = arr.getJSONObject(i);
                        license_no = jsonProductObject.getString("plate");
                        //Toast.makeText(MainActivity.this, "LICENSE data KK=" + license_no, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, "VH NO data KK=" + vehicleNumber.getText().toString(), Toast.LENGTH_SHORT).show();


                        tow_address = jsonProductObject.getString("towed_to_address");
                        tow_phone = jsonProductObject.getString("tow_facility_phone");
                        // relocated_address = jsonProductObject.getString("relocated_to_street_name");
                        //relocated_phone = jsonProductObject.getString("service_request_number");

                    }

                    //handle first request
                    // if(request_id.equalsIgnoreCase("tow")) {
                    if (vehicleNumber.getText().toString().equals(license_no)) {
                        displayFound.setText("Towed !!");
                        displayLocation.setText(tow_address);
                        displayContact.setText(tow_phone);
                        Linkify.addLinks(displayLocation, Linkify.MAP_ADDRESSES);
                        Linkify.addLinks(displayContact, Linkify.PHONE_NUMBERS);
                        //        request_id = "tow_successful";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //display_location.setText("");
            }
        }


    }
}

