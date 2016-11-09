package com.rohan.user.httpurldemo2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    HttpURLConnection connection;
    BufferedReader reader;
    TextView txt;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=(Button) findViewById(R.id.getData);
        listView=(ListView) findViewById(R.id.listView);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("http://services.hanselandpetal.com/feeds/flowers.json");
            }



        });

    }
    public class JsonTask extends AsyncTask<String,String,String[]>{
        @Override
        protected void onPostExecute(String[] values) {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, values);
            listView.setAdapter(itemsAdapter);
            listView.setBackgroundColor(1234);
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream= connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();

                while((line=reader.readLine()) !=null){
                    buffer.append(line);
                }
                String[] category = new String[100];
                JSONArray parentArray = new JSONArray(buffer.toString());
                for(int i=0;i< parentArray.length();i++) {
                    JSONObject parentObject = parentArray.getJSONObject(i);
                 category[i] = parentObject.getString("category");
                    Log.d("TAG:  ", category[i]);
                }

                return category;

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if(connection!=null) {
                    connection.disconnect();
                }
                try {
                    if(reader!=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
