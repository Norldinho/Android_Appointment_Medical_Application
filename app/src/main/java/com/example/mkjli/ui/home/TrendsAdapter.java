package com.example.mkjli.ui.home;

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


import com.example.mkjli.R;
import com.example.mkjli.ui.rdvs.DocRDvFragment;

import java.util.ArrayList;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.Viewholder> {

    ArrayList<HomeViewModel> items;
    Context context;

    public TrendsAdapter(Context context,ArrayList<HomeViewModel> items) {

        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public TrendsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_trend_list,parent,false);
        context = parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendsAdapter.Viewholder holder, int position) {
        holder.nameDoctor.setText(items.get(position).getnameDoctr());
        holder.spisialitedoctor.setText(items.get(position).getspisialitedoctor());
        holder.heurRDV.setText(items.get(position).getheurRDV());
        holder.jourRDV.setText(items.get(position).getjourRDV());



    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameDoctor,spisialitedoctor,jourRDV,heurRDV,prix;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            nameDoctor = itemView.findViewById(R.id.doctorName);
            spisialitedoctor = itemView.findViewById(R.id.spisialitiDoctor);
            jourRDV = itemView.findViewById(R.id.jourRDV);
            heurRDV = itemView.findViewById(R.id.heurRDV);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            HomeViewModel homeViewModel = items.get(position);
            DocRDvFragment docRDvFragment = new DocRDvFragment();

            Bundle bundle = new Bundle();
            bundle.putString("id_doctor",homeViewModel.getIddoctor());
            bundle.putString("heur_rdv",homeViewModel.getHeurRDV());
            bundle.putString("jeur_rdv",homeViewModel.getJourRDV());
            bundle.putString("tell",homeViewModel.getTelephone());
            bundle.putString("email",homeViewModel.getEmail());
            bundle.putString("sexe",homeViewModel.getSexe());
            bundle.putString("spi",homeViewModel.getspisialitedoctor());
            bundle.putString("nom",homeViewModel.getNameDoctr());

            docRDvFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, docRDvFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
