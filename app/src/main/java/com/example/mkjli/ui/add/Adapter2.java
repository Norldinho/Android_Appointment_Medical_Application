package com.example.mkjli.ui.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;

import java.util.ArrayList;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.Viewholder>{
    ArrayList<Modle2View> items;
    Context context;

    public Adapter2(ArrayList<Modle2View> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter2.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.k2,parent,false);
        context = parent.getContext();
        return new Adapter2.Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2.Viewholder holder, int position) {
        holder.spisialitedoctor.setText(items.get(position).getSpisialite());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView spisialitedoctor;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            spisialitedoctor = itemView.findViewById(R.id.textView24);
        }
    }
}
