package com.cs442.schafle.towed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    EditText vehicleNumber;
    TextView display_text;
    String vehicleNumberValue = "";
    FancyButton btnSubmit;
    JSONObject jobj;
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
                //display_text.setText(vehicleNumber.getText());           //Resetting the form
                vehicleNumber.setVisibility(View.INVISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);
                String url = "https://data.cityofchicago.org/resource/rp42-fxjt.json?plate="+vehicleNumber.getText();
                new Request().execute(url);
                //Log.d("Towed: ", url);
                //Log.d("Towed: ", jobj.toString());
                display_text.setVisibility(View.VISIBLE);
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
        protected void onPostExecute(String json) {

            display_text.setText(json);

        }

    }
}

