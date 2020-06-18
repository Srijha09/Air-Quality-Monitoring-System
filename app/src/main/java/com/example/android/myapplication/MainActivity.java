package com.example.android.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public TextView temp,humid,qIndex,co_ppm,lastupdate;

    public Button refresh;
    public CardView crd;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //feed1  replace with your username feedname and AIO key
        final String link_temp="https://io.adafruit.com/api/v2/aish_raj/feeds/temp/data?x-aio-key=5d4b96bcd7ae460fb1fdfe0c0fe4b75c";
        //feed2 replace with your username feedname and AIO key
        final String link_moist="https://io.adafruit.com/api/v2/aish_raj/feeds/h/data?x-aio-key=5d4b96bcd7ae460fb1fdfe0c0fe4b75c";
        final String link_qIndex = "https://io.adafruit.com/api/v2/aish_raj/feeds/qindex/data?x-aio-key=5d4b96bcd7ae460fb1fdfe0c0fe4b75c";
        final String link_co ="https://io.adafruit.com/api/v2/aish_raj/feeds/ppm1/data?x-aio-key=5d4b96bcd7ae460fb1fdfe0c0fe4b75c";

        temp=(TextView)findViewById(R.id.temp) ;
        humid=(TextView)findViewById(R.id.humid);
        qIndex =(TextView)findViewById(R.id.qIndex);
        co_ppm =(TextView)findViewById(R.id.co_ppm);
        refresh=(Button)findViewById(R.id.button4);
        lastupdate=(TextView)findViewById(R.id.textView15);
        crd=(CardView)findViewById(R.id.cardView);
        HttpGetRequest hp=new HttpGetRequest();
        hp.execute(link_temp);
        HttpGetRequestMoist m=new HttpGetRequestMoist();
        m.execute(link_moist);
        HttpGetRequestQIndex qi=new HttpGetRequestQIndex();
        qi.execute(link_qIndex);
        HttpGetRequestCO co=new HttpGetRequestCO();
        co.execute(link_co);
        //Values gets updated on clicking the refresh button
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpGetRequest t1=new HttpGetRequest();
                t1.execute(link_temp);
                HttpGetRequestMoist m1=new HttpGetRequestMoist();
                m1.execute(link_moist);
                HttpGetRequestQIndex q = new HttpGetRequestQIndex();
                q.execute(link_qIndex);
                HttpGetRequestCO co_ = new HttpGetRequestCO();
                co_.execute(link_co);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d = new Date();
                lastupdate.setText(formatter.format(d));
                //  crd.setCardBackgroundColor(Color.RED);
            }
        });
    }
    //Async Task Method to GET
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("stuff", "doInBackground: "+result);
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);

                JSONObject explrObject = jsonArray.getJSONObject(0);
                // Log.d("values", "onPostExecute: "+explrObject.get("value"));
                String value=explrObject.getString("value");

                temp.setText(value+" C");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //Request for 2nd feed
    public class HttpGetRequestMoist extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
//            String feed=params[1];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("stuff", "doInBackground: "+result);

            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);

                JSONObject explrObject = jsonArray.getJSONObject(0);
                String value=explrObject.getString("value");
                humid.setText(value+" %");
                //replace with your condition


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Async Task Method to GET
    public class HttpGetRequestQIndex extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("stuff", "doInBackground: "+result);
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);

                JSONObject explrObject = jsonArray.getJSONObject(0);
                // Log.d("values", "onPostExecute: "+explrObject.get("value"));
                String value=explrObject.getString("value");

                qIndex.setText(value);
                //replace with whatever condition you want
                //Change the data type accordingly
                if(Float.parseFloat(value)==1){
                    Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();
                }

                if(Float.parseFloat(value)==2){
                    Toast.makeText(getApplicationContext(),"Moderate",Toast.LENGTH_SHORT).show();
                }

                if(Float.parseFloat(value)==3){
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            MainActivity.this).create();
                    alertDialog.setTitle("WARNING");
                    alertDialog.setMessage("Unhealthy");
                    // alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK" ,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            Toast.makeText(getApplicationContext(), "Unhealthy, try to keep the air quality in check", Toast.LENGTH_SHORT).show();
                            crd.setCardBackgroundColor(Color.RED);
                        }
                    });
                    alertDialog.show();

                }

                if(Float.parseFloat(value)==4){
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            MainActivity.this).create();
                    alertDialog.setTitle("WARNING");
                    alertDialog.setMessage("Very Unhealthy");
                    // alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK" ,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            Toast.makeText(getApplicationContext(), "Very Unhealthy,Health alert: everyone may experience more serious conditions", Toast.LENGTH_SHORT).show();
                            crd.setCardBackgroundColor(Color.RED);
                        }
                    });
                    alertDialog.show();

                }

                if(Float.parseFloat(value)== 5){
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            MainActivity.this).create();
                    alertDialog.setTitle("WARNING");
                    alertDialog.setMessage("Hazardous");
                    // alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK" ,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            Toast.makeText(getApplicationContext(), " Healthy warnings of emergency ", Toast.LENGTH_SHORT).show();
                            crd.setCardBackgroundColor(Color.RED);
                        }
                    });
                    alertDialog.show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class HttpGetRequestCO extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
//            String feed=params[1];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("stuff", "doInBackground: "+result);

            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);

                JSONObject explrObject = jsonArray.getJSONObject(0);
                String value=explrObject.getString("value");
                co_ppm.setText(value+"ppm ");
                //replace with your condition
                if(Float.parseFloat(value)<50){
                    Toast.makeText(getApplicationContext(),"Low Level",Toast.LENGTH_SHORT).show();
                }

                if(Float.parseFloat(value)>51 && Float.parseFloat(value)<100 ){
                    Toast.makeText(getApplicationContext(),"Mid Level",Toast.LENGTH_SHORT).show();
                }
                if(Float.parseFloat(value)>101){
                    Toast.makeText(getApplicationContext(),"High Level",Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

