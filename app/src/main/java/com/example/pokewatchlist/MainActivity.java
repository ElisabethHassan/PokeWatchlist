package com.example.pokewatchlist;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    ImageView imageView;
    LinkedList<String> pokeList = new LinkedList<>();
    EditText search;
    Button enterButton;
    String pokemon;
    ArrayAdapter<String> adapter;
    TextView name, number, weight, height, base_xp, move, ability;
    String url = "https://pokeapi.co/api/v2/pokemon/";

    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String poke = (String) adapterView.getItemAtPosition(i);
            makeReq(poke);

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
                        makeReq(pokemon);
                        pokeList.add(pokemon);
                    }
                } else if (Integer.parseInt(pokemon) > 0 && Integer.parseInt(pokemon) < 1010){
                    makeReq(pokemon);
                    pokeList.add(pokemon);
                } else Toast.makeText(getApplicationContext(), "This input is invalid.", Toast.LENGTH_LONG).show();
            }
        }
    };


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
        move = findViewById(R.id.move_value);
        ability = findViewById(R.id.ability_value);
        enterButton = findViewById(R.id.searchButton_id);
        search = findViewById(R.id.searchET_id);
        listview = findViewById(R.id.listview_id);
        imageView = findViewById(R.id.imageView);

        int newWidth = 200;
        int newHeight = 200;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = newWidth;
        layoutParams.height = newHeight;
        imageView.setLayoutParams(layoutParams);

        pokemon = search.getText().toString();

        enterButton.setOnClickListener(enterButtonListener);
        listview.setOnItemClickListener(clickListener);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, pokeList);
        listview.setAdapter(adapter);
        pokeList.add("bulbasaur");
        pokeList.add("eevee");
        makeReq("ditto");

    }

    public void makeReq(String poke){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        url += poke.toLowerCase();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    JSONArray jsonArray = response.getJSONArray("1");
                    String numberD = String.valueOf(response.getInt("id"));
                    String heightD = String.valueOf(response.getInt("height"));
                    String weightD = String.valueOf(response.getInt("weight"));
                    String baseD = response.getString("base_experience");
                    String nameD = response.getString("name");
                    String abilityD = response.getJSONArray("abilities").getJSONObject(0).getJSONObject("ability").getString("name");
                    String moveD = response.getJSONArray("moves").getJSONObject(0).getJSONObject("move").getString("name");

                    height.setText(heightD);
                    weight.setText(weightD);
                    number.setText(numberD);
                    base_xp.setText(baseD);
                    name.setText(nameD.toUpperCase());
                    ability.setText(abilityD);
                    move.setText(moveD);
                    if(response.getInt("id") < 10){
                        String url = "https://github.com/HybridShivam/Pokemon/blob/master/assets/images/00" + numberD + ".png?raw=true";
                        Picasso.get().load(url).into(imageView);
                    } else if (response.getInt("id") < 100){
                        String url = "https://github.com/HybridShivam/Pokemon/blob/master/assets/images/0" + numberD + ".png?raw=true";
                        Picasso.get().load(url).into(imageView);
                    } else {
                        String url = "https://github.com/HybridShivam/Pokemon/blob/master/assets/images/" + numberD + ".png?raw=true";
                        Picasso.get().load(url).into(imageView);
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
        queue.add(request);

    }








}