package com.example.pokewatchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PokeAdapater extends ArrayAdapter<Pokemon> {
    private static final String TAG = "PokeAdapater";
    private Context context;
    int mResource;
    List<Pokemon> pokeList;

    public PokeAdapater(@NonNull Context context, int resource, @NonNull List<Pokemon> objects) {
        super(context, resource, objects);
        this.context = context;
        mResource = resource;
        pokeList = objects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String num = getItem(position).getNumber();

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Pokemon poke = pokeList.get(position);

        TextView tvName = convertView.findViewById(R.id.textView);
        TextView tvNum = convertView.findViewById(R.id.textView2);

        tvName.setText(name);
        tvNum.setText(num);

        return convertView;
    }
}
