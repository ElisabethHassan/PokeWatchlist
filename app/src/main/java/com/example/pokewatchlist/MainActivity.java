package com.example.pokewatchlist;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    LinkedList<String> pokeList = new LinkedList<>();
    EditText search;
    Button enterButton;
    String pokemon;
    ArrayAdapter<String> adapter;
    TextView name, number, weight, height, base_xp;
    String url = "https://pokeapi.co/api/v2/pokemon/";
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.pokeName_id);
        number = findViewById(R.id.num_value);
        weight = findViewById(R.id.weight_value);
        height = findViewById(R.id.height_value);
        base_xp = findViewById(R.id.base_value);
        enterButton = findViewById(R.id.searchButton_id);
        search = findViewById(R.id.searchET_id);
        listview = findViewById(R.id.listview_id);


        pokemon = search.getText().toString();

        enterButton.setOnClickListener(enterButtonListener);
        listview.setOnItemClickListener(clickListener);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, pokeList);
        listview.setAdapter(adapter);
        pokeList.add("bulbasaur");
        pokeList.add("eevee");

        mQueue = Volley.newRequestQueue(this);


    }

    public void getPoke(String p){
        PokemonHelper.getPokemonInfo(getApplicationContext(), p, new PokemonHelper.PokemonCallback() {
            @Override
            public void onSuccess(String n, int w, int h, String t) {
                // Update UI with Pokémon information
                // For example, set the text of TextViews
                height.setText(h);
                weight.setText(w);
                base_xp.setText(t);
            }

            @Override
            public void onError(String error) {
                // Handle error, e.g., show an error message
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void makeReq(String poke){
        url += poke;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("forms");
                    String heightD = response.getString("height");
                    String weightD = response.getString("weight");
                    String baseD = response.getString("base_experience");
                    height.setText(heightD);
                    weight.setText(weightD);
                    base_xp.setText(baseD);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject nameObj = jsonArray.getJSONObject(i);

                        String nameD = nameObj.getString("name");
                        name.setText(nameD);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error on getting data ", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void makeRequest(String poke){
        ANRequest req = AndroidNetworking.get("https://pokeapi.co/api/v2/pokemon/{pokemon}")
                .addPathParameter("pokemon", poke)
                .setPriority(Priority.LOW)
                .build();
        req.getAsObjectList(Pokemon.class, new ParsedRequestListener<List<Pokemon>>() {
            @Override
            public void onResponse(List<Pokemon> pokemons) {
                for (Pokemon p : pokemons) {
                    number.setText(p.getNumber());
                    weight.setText(p.getWeight());
                    height.setText(p.getHeight());
                    base_xp.setText(p.getBaseXP());
                    Toast.makeText(getApplicationContext(),"API Call Successful", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onError(ANError anError) {
                // handle error
                Toast.makeText(getApplicationContext(),"Error on getting data ", Toast.LENGTH_LONG).show();
            }
        });
    }


    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String poke = (String) adapterView.getItemAtPosition(i);
//            makeRequest(poke);
//            makeReq(pokemon);
            getPoke(pokemon);
            name.setText(pokemon);

        }
    };

    View.OnClickListener enterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < pokemon.length(); i++){
                if((Character.isLetter(pokemon.charAt(i)) == false)) {
                    if(!pokemon.contains("%") && !pokemon.contains("&") && !pokemon.contains("*")
                            && !pokemon.contains("(") && !pokemon.contains("@") &&
                            !pokemon.contains("!") && !pokemon.contains(";") && !pokemon.contains(":")
                            && !pokemon.contains("<>")       ){
//                        makeRequest(pokemon);
                        getPoke(pokemon);
                        name.setText(pokemon);
                        pokeList.add(pokemon);
                    }
                } else if (Integer.parseInt(pokemon) > 0 && Integer.parseInt(pokemon) < 1010){
//                    makeRequest(pokemon);
                    getPoke(pokemon);
//                    makeReq(pokemon);
                    pokeList.add(pokemon);
                    name.setText(pokemon);
                } else Toast.makeText(getApplicationContext(), "This input is invalid.", Toast.LENGTH_LONG).show();
            }
        }
    };





}