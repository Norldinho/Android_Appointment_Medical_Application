package com.example.mkjli.medcin.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.MaladerFragment;
import com.example.mkjli.R;

import java.util.ArrayList;

public class AdapterHomeDoctor extends RecyclerView.Adapter<AdapterHomeDoctor.Viewholder>{
    ArrayList<MaladeView> items;
    Context context;

    public AdapterHomeDoctor(ArrayList<MaladeView> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterHomeDoctor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_viewholder, parent, false);
        context = parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHomeDoctor.Viewholder holder, int position) {

        holder.nameMalade.setText(items.get(position).getNameMalade());
        holder.number.setText(items.get(position).getNumberMalade());
        holder.heurRDV.setText(items.get(position).getHeurRDV());
        holder.jourRDV.setText(items.get(position).getJourRDV());
        holder.prixconsultation.setText(items.get(position).getPrixConsultation());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameMalade,number,jourRDV,heurRDV,prixconsultation;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameMalade = itemView.findViewById(R.id.maladName);
            number = itemView.findViewById(R.id.numbermalad);
            jourRDV = itemView.findViewById(R.id.jourRDV1);
            heurRDV = itemView.findViewById(R.id.heurRDV1);
            prixconsultation = itemView.findViewById(R.id.prixx);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

                int position = getAdapterPosition();
                MaladeView maladeView = items.get(position);

                // Créer une instance du fragment MaladerFragment
                MaladerFragment maladerFragment = new MaladerFragment();

                // Passer des données supplémentaires à MaladerFragment, si nécessaire
                Bundle bundle = new Bundle();
                bundle.putString("id_malade",maladeView.getIdrdv());
                bundle.putString("heur_rdv",maladeView.getHeurRDV());
                bundle.putString("jeur_rdv",maladeView.getJourRDV());
                bundle.putString("tell",maladeView.getNumberMalade());

                // Ajoutez d'autres données si nécessaire
                maladerFragment.setArguments(bundle);

                // Remplacer le fragment actuel par MaladerFragment
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main_doctor, maladerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

        }
    }
}
