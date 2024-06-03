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

public class Adapter3 extends RecyclerView.Adapter<Adapter3.Viewholder>{
    ArrayList<Modle3View> items;
    Context context;

    public Adapter3(ArrayList<Modle3View> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter3.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.k3,parent,false);
        context = parent.getContext();
        return new Adapter3.Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter3.Viewholder holder, int position) {
        holder.adress.setText(items.get(position).getAdress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView adress;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            adress = itemView.findViewById(R.id.textView242);
        }
    }
}
