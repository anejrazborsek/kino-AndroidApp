package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView filmi;
    private String url = "https://kino-anej.azurewebsites.net/api/v1/film";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        filmi = (TextView) findViewById(R.id.osebe);
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    String film_id = object.getString("filmID");
                    String film_ime = object.getString("film_ime");
                    String film_trajanje = object.getString("film_trajanje");

                    data.add(film_id + " " + film_ime + " " + film_trajanje);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            filmi.setText("");


            for (String row: data){
                String currentText = filmi.getText().toString();
                filmi.setText(currentText + "\n\n" + row);
            }

        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    public static final String EXTRA_MESSAGE = "com.example.universityapp.MESSAGE";

    public void addStudentActivity (View view) {
        Intent intent = new Intent(this,AddStudentActivity.class);
        String message = "Dodaj film.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void prikaziOsebe(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener)
            {
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "SecretKey");
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }


}