package com.example.mkjli.medcin.home;


import static com.example.mkjli.DateUtil.getCurrentDayAndMonth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkjli.R;
import com.example.mkjli.databinding.FragmentHomeBinding;
import com.example.mkjli.databinding.FragmentHomeDoctorBinding;
import com.example.mkjli.medcin.MainActivity_for_doctor;
import com.example.mkjli.ui.add.DoctorDonnee;

import com.example.mkjli.ui.home.HomeViewModel;

import com.example.mkjli.ui.home.TrendsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

interface RDVXCallback {
    void onCallback(ArrayList<MaladeView> homeViewModellist);
}
interface PatientCallback {
    void onCallback(PatentDonnee patentDonnee);
}
public class HomeDoctorFragment extends Fragment {

    private RecyclerView.Adapter adapterfoodlist;
    private FragmentHomeDoctorBinding binding;
    private RecyclerView recyclerViewfood;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private TextView pluss,nomdctor;


    private String mParam1;
    private String mParam2;

    public HomeDoctorFragment() {
        // Required empty public constructor
    }


    public static HomeDoctorFragment newInstance(String param1, String param2) {
        HomeDoctorFragment fragment = new HomeDoctorFragment();
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
        return inflater.inflate(R.layout.fragment_home_doctor, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        pluss = view.findViewById(R.id.nike);
        nomdctor = view.findViewById(R.id.textViewsbahdoctorname);



        fetchCurrentUserFromFirestore();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();

        ArrayList<MaladeView> items = new ArrayList<>();

        getRDVbyIdPatient(udi, new RDVXCallback() {
            @Override
            public void onCallback(ArrayList<MaladeView> homeViewModellist) {
                if (homeViewModellist != null) {
                    for (MaladeView rdv : homeViewModellist) {
                        items.add(new MaladeView(rdv.getNameMalade(), rdv.getNumberMalade(), rdv.getHeurRDV(), rdv.getJourRDV(),udi,rdv.getId()));
                    }
                } else {
                    // Traitez le cas où la liste des rendez-vous est vide ou nulle
                    Toast.makeText(getContext(), "Aucun rendez-vous trouvé.", Toast.LENGTH_SHORT).show();
                }
                // Mettez à jour l'adaptateur et le RecyclerView
                adapterfoodlist.notifyDataSetChanged();
            }
        });


        recyclerViewfood = view.findViewById(R.id.view1docotor);
        recyclerViewfood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewfood.setHasFixedSize(true);

        adapterfoodlist = new AdapterHomeDoctor(items, getContext());
        recyclerViewfood.setAdapter(adapterfoodlist);



        pluss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_for_doctor) requireActivity()).gotoPatientFragment();
            }
        });


    }

    private void getRDVbyIdPatient(String udi, RDVXCallback callback) {
        String currentDate = getCurrentDayAndMonth(); // Obtenir la date actuelle au format "jj/mm"
        ArrayList<MaladeView> donnerRDV = new ArrayList<>();
        db.collection("RDV")
                .whereEqualTo("id_medcin", udi)
                .whereEqualTo("date", currentDate) // Utiliser la date actuelle filtrée
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Aucun rendez-vous trouvé, retourner "Aucun"
                            callback.onCallback(new ArrayList<>());
                            return;
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String heur = document.getString("heur");
                            String idPatient = document.getString("id_patient");
                            getDataPatient(idPatient, new PatientCallback() {
                                @Override
                                public void onCallback(PatentDonnee patientDonnee) {
                                    if (patientDonnee != null) {
                                        String nomm = patientDonnee.getNom();
                                        String prenomm = patientDonnee.getPrenom();
                                        String numtel = patientDonnee.getNumtel();
                                        // Ajoutez les données à la liste une fois qu'elles sont disponibles
                                        donnerRDV.add(new MaladeView(nomm + " " + prenomm, numtel, heur, currentDate, idPatient,document.getId()));
                                        // Vérifiez si toutes les données ont été récupérées
                                        if (donnerRDV.size() == task.getResult().size()) {
                                            // Appelez le callback pour retourner les résultats
                                            callback.onCallback(donnerRDV);
                                        }
                                    } else {
                                        // Traitez le cas où les informations du patient sont nulles
                                        Toast.makeText(getContext(), "Aucune information de patient trouvée.", Toast.LENGTH_SHORT).show();
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


    private void getDataPatient(String idDocto, PatientCallback callback) {
        db.collection("patients").document(idDocto).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nom = document.getString("nom");
                    String prenom = document.getString("prenom");
                    String id = document.getId();
                    String numTelephone = document.getString("numTelephone");
                    String email = document.getString("email");
                    String dateNaissance = document.getString("dateNaissance");
                    String sexe = document.getString("sexe");
                    String adresse = document.getString("adresse");

                    PatentDonnee doctorPatient = new PatentDonnee(nom, prenom, numTelephone, email, adresse,sexe,dateNaissance,id);
                    callback.onCallback(doctorPatient);
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

    private void fetchCurrentUserFromFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String udi = currentUser.getUid();
            db.collection("medecins").document(udi)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    nomdctor.setText(document.getData().get("nom").toString() + " " +document.getString("prenom"));
                                } else {
                                    nomdctor.setText("nop");
                                }
                            } else {
                                nomdctor.setText("get failed with " + task.getException());
                            }
                        }
                    });
        }
    }
}