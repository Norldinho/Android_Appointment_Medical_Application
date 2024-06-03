package com.example.mkjli.ui.rdvs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mkjli.R;
import com.example.mkjli.ui.add.DoctorDonnee;

import com.example.mkjli.ui.home.HomeViewModel;
import com.example.mkjli.ui.home.TrendsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

interface RDVseCallback {
    void onCallback(ArrayList<HomeViewModel> homeViewModellist);
}
interface DoctorsCallback {
    void onCallback(DoctorDonnee doctorDonnee);
}
public class RdvsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private RecyclerView.Adapter adapterfoodlist;
    private RecyclerView recyclerViewfood;

    public RdvsFragment() {

    }


    public static RdvsFragment newInstance(String param1, String param2) {
        RdvsFragment fragment = new RdvsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rdvs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();
        ArrayList<HomeViewModel> items = new ArrayList<>();
        getRDVbyIdPatient(udi, new RDVseCallback() {
            @Override
            public void onCallback(ArrayList<HomeViewModel> homeViewModellist) {
                if (homeViewModellist != null) {
                    for (HomeViewModel rdv : homeViewModellist) {
                        items.add(new HomeViewModel(rdv.getnameDoctr(), rdv.getspisialitedoctor(), rdv.getheurRDV(), rdv.getjourRDV(),rdv.getIddoctor(),rdv.getTelephone(),rdv.getEmail(),rdv.getSexe()));
                    }
                } else {
                    // Traitez le cas où la liste des rendez-vous est vide ou nulle
                    Toast.makeText(getContext(), "Aucun rendez-vous trouvé.", Toast.LENGTH_SHORT).show();
                }
                // Mettez à jour l'adaptateur et le RecyclerView
                adapterfoodlist.notifyDataSetChanged();
            }
        });

        recyclerViewfood = view.findViewById(R.id.reci);
        recyclerViewfood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewfood.setHasFixedSize(true);

        adapterfoodlist = new TrendsAdapter(getContext(), items);
        recyclerViewfood.setAdapter(adapterfoodlist);
    }

    private void getRDVbyIdPatient(String idpatient, RDVseCallback callback) {
        ArrayList<HomeViewModel> donnerRDV = new ArrayList<>();
        db.collection("RDV").whereEqualTo("id_patient", idpatient).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String date = document.getString("date");
                    String heur = document.getString("heur");
                    String idDoctor = document.getString("id_medcin");
                    getDataDoctor(idDoctor, new DoctorsCallback() {
                        @Override
                        public void onCallback(DoctorDonnee doctorDonnee) {
                            if (doctorDonnee != null) {
                                String nomDoctor = doctorDonnee.getNom();
                                String prenomDoctor = doctorDonnee.getPrenom();
                                String spisialitDoctor = doctorDonnee.getSpisia();
                                String tele = doctorDonnee.getTelephon();
                                String email = doctorDonnee.getEmail();
                                String sexe = doctorDonnee.getSexe();

                                // Ajoutez les données à la liste une fois qu'elles sont disponibles
                                donnerRDV.add(new HomeViewModel(nomDoctor + " " + prenomDoctor, spisialitDoctor, heur, date,idDoctor,tele,email,sexe));
                                // Vérifiez si toutes les données ont été récupérées
                                if (donnerRDV.size() == task.getResult().size()) {
                                    // Appelez le callback pour retourner les résultats
                                    callback.onCallback(donnerRDV);
                                }
                            } else {
                                // Traitez le cas où les informations du médecin sont nulles
                                Toast.makeText(getContext(), "Aucune information de médecin trouvée.", Toast.LENGTH_SHORT).show();
                                callback.onCallback(null);
                            }
                        }
                    });
                }
            } else {
                // Gérez les erreurs lors de la récupération des rendez-vous
                Toast.makeText(getContext(), "Erreur lors de la récupération des RDVs", Toast.LENGTH_LONG).show();
                callback.onCallback(null); // Retournez null en cas d'erreur
            }
        });
    }






    private void getDataDoctor(String idDocto, DoctorsCallback callback) {
        db.collection("medecins").document(idDocto).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nom = document.getString("nom");
                    String prenom = document.getString("prenom");
                    String id = document.getId();
                    String wilaya = document.getString("wilaya");
                    String specialite = document.getString("specialite");
                    String email = document.getString("email");
                    String tele = document.getString("telephone");
                    String sexe = document.getString("sexe");

                    DoctorDonnee doctorData = new DoctorDonnee(nom, prenom, id, wilaya, specialite,email,tele,sexe);
                    callback.onCallback(doctorData);
                } else {
                    // Gérer le cas où le document du médecin n'existe pas
                    Toast.makeText(getContext(), "Aucun médecin trouvé avec l'ID: " + idDocto, Toast.LENGTH_SHORT).show();
                    // Vous pouvez également déclencher l'appel à onCallback avec null ou une valeur par défaut
                    callback.onCallback(null);
                }
            } else {
                // Gérer les erreurs lors de la récupération des détails du médecin
                Toast.makeText(getContext(), "Erreur lors de la récupération des détails du médecin: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                // Vous pouvez également déclencher l'appel à onCallback avec null ou une valeur par défaut
                callback.onCallback(null);
            }
        });
    }
}