package com.example.mkjli.ui.lorem;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import com.example.mkjli.R;


public class HeurAdapter extends RecyclerView.Adapter<HeurAdapter.ViewHolder>{
    List<RDV> heurslist;
    private LayoutInflater mInflater;
    private FragmentManager fragmentManager;
    private String selectedDate;
    private LoremFragment fragment;

    public HeurAdapter(List<RDV> heurslist, Context context, FragmentManager fragmentManager, LoremFragment fragment) {
        this.heurslist = heurslist;
        this.mInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.fragment = fragment; // Stocker la référence du fragment
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewwa9trdv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RDV item = heurslist.get(position);
        holder.heur.setText(item.getHeur());

        // Associer un écouteur de clic à cet élément de RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment != null && fragment.isAdded()) {
                    // Assurez-vous que le fragment est attaché à une activité
                    String ids = fragment.getArguments().getString("id");
                    fragment.checkPatientExist(ids, new PatientExistCallback() {
                        @Override
                        public void onPatientExist(boolean exists) {
                            if (exists) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
                                builder.setMessage("تم تحديد موعدك بالفعل.")
                                        .setCancelable(true)
                                        .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                            } else {
                                showDeleteConfirmationDialog(holder.getAdapterPosition());
                            }
                        }
                    });

                }



            }
        });
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        builder.setMessage("قم بتأكيد الموعد")
                .setCancelable(true)
                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Récupérer la date sélectionnée et l'heure de l'élément supprimé
                        String date = selectedDate; // Utilisez la date sélectionnée définie précédemment
                        String heure = heurslist.get(position).getHeur(); // Récupérez l'heure de l'élément supprimé

                        // Créer un nouvel objet RDV avec la date et l'heure récupérées
                        RDV newRDV = new RDV(date, heure); // Assurez-vous que votre constructeur RDV prend en charge ces paramètres

                        // Appeler la méthode ajouterRDV du fragment
                        if (fragment != null) {
                            String ids = fragment.getArguments().getString("id");
                            fragment.ajouterRDV(ids,date, heure); // Appel à la méthode du fragment
                        }

                        // Supprimer l'élément de la liste de données
                        heurslist.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public int getItemCount() {
        return heurslist.size();
    }

    public void updateData(List<RDV> newData) {
        heurslist.clear(); // Effacer les données existantes
        heurslist.addAll(newData); // Ajouter de nouvelles données
        notifyDataSetChanged(); // Notifier le RecyclerView que les données ont changé
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView heur;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            heur = itemView.findViewById(R.id.textView10);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Voulez-vous vraiment supprimer cet élément ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Supprimer l'élément de la liste de données
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                heurslist.remove(position);
                                notifyItemRemoved(position);
                            }
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}

