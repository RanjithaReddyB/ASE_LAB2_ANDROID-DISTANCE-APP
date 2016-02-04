package com.example.chanti.distance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class Home extends AppCompatActivity {

    TextView displayText;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        displayText = (TextView) findViewById(R.id.display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            Intent i = new Intent(Home.this, MainActivity.class);
            startActivity(i);
            finish();
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void weather(View v) {
        if (v.getId() == R.id.distance) {
            String city = ((EditText) findViewById(R.id.city)).getText().toString();
            final String API_KEY = "a441269177776e803e0aa5df30b2748e";
            final String urlText = "http://maps.googleapis.com/maps/api/directions/json?origin=kansascity&destination="+ city +"&sensor=false,us&appid=" + API_KEY;

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlText);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoInput(true);
                        urlConnection.setDoOutput(true);
                        StringBuilder stringBuilder = new StringBuilder();
                        InputStream is;
                        is = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = br.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        response = stringBuilder.toString();
                        System.out.println(stringBuilder.toString());
                        is.close();
                        urlConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try{
                        JSONObject j = new JSONObject(response);
                        JSONArray k = j.getJSONArray("routes");
                        JSONArray l = k.getJSONObject(0).getJSONArray("legs");
                     //   JSONArray l = k.getJSONArray("legs");
                        response = "Distance: " + l.getJSONObject(0).getJSONObject("distance").getString("text").toString();

                        //Log.d("jobj", jobj.toString());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayText.setText(response);
                }
            }, 1500);

        }
    }
}

