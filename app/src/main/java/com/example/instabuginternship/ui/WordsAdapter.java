package com.example.instabuginternship.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instabuginternship.R;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    //make a private container
    private final ArrayList<MainActivity.Entry> list;

    public WordsAdapter(ArrayList<MainActivity.Entry> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsAdapter.ViewHolder holder, int position) {
        holder.word_string.setText(list.get(position).getKey());
        holder.word_count.setText(Integer.toString(list.get(position).getValue()));
        Log.d("LIST", "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView word_string;
        public final TextView word_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word_string = itemView.findViewById(R.id.tv_Word);
            word_count = itemView.findViewById(R.id.tv_Count);
        }
    }

}
