package com.example.pokewatchlist;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonHelper {

    private static final String API_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";

    public interface PokemonCallback {
        void onSuccess(String name, int weight, int height, String type);

        void onError(String error);
    }

    public static void getPokemonInfo(Context context, String pokemonName, final PokemonCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = API_BASE_URL + pokemonName.toLowerCase();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            int weight = response.getInt("weight");
                            int height = response.getInt("height");

                            // Get the first type for simplicity; modify as needed
                            String type = response.getJSONArray("types")
                                    .getJSONObject(0)
                                    .getJSONObject("type")
                                    .getString("name");

                            callback.onSuccess(name, weight, height, type);
                            Log.d("PokemonApiHelper", "API Response: " + response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PokemonApiHelper", "Error: " + error.getMessage());
                        callback.onError("Error fetching data");
                    }
                });

        queue.add(request);
    }



}
