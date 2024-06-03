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
import java.util.List;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder> {
    private List<DoctorDonnee> items;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DoctorDonnee doctor);
    }

    public Adapter1(List<DoctorDonnee> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.k1, parent, false);
        return new ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorDonnee doctor = items.get(position);
        holder.nameDoctor.setText(doctor.getNom());
        holder.spisialitedoctor.setText(doctor.getSpisia());
        holder.wilaya.setText(doctor.getWilaya());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameDoctor, spisialitedoctor, wilaya;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDoctor = itemView.findViewById(R.id.textView26);
            spisialitedoctor = itemView.findViewById(R.id.textView27);
            wilaya = itemView.findViewById(R.id.textView36);
        }
    }
}
