package com.example.mkjli.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;
import com.example.mkjli.databinding.FragmentHomeBinding;
import com.example.mkjli.ui.add.DoctorDonnee;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
interface RDVsCallback {
    void onCallback(ArrayList<HomeViewModel> homeViewModellist);
}
interface DoctorCallback {
    void onCallback(DoctorDonnee doctorDonnee);
}

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore db;
    private RecyclerView.Adapter adapterfoodlist;
    private RecyclerView recyclerViewfood;

    private TextView nomp,plus;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;




    public HomeFragment(){

    }

    public static HomeFragment newInstance(String param1,String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        if (getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container,false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Fetch data of current user from Firestore
        nomp = view.findViewById(R.id.textView2);
        plus = view.findViewById(R.id.amchi);
        fetchCurrentUserFromFirestore();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();

        ArrayList<HomeViewModel> items = new ArrayList<>();
        getRDVbyIdPatient(udi, new RDVsCallback() {
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

        recyclerViewfood = view.findViewById(R.id.view1);
        recyclerViewfood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewfood.setHasFixedSize(true);

        adapterfoodlist = new TrendsAdapter(getContext(), items);
        recyclerViewfood.setAdapter(adapterfoodlist);





        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le NavController à partir du FragmentParent
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

                // Naviguer vers la RdvsFragment
                navController.navigate(R.id.navigation_rdv);
            }
        });



    }


    private void fetchCurrentUserFromFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();




        if (currentUser != null) {
            String udi = currentUser.getUid();
            db.collection("patients").document(udi)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                     nomp.setText(document.getData().get("nom").toString());
                                } else {
                                     nomp.setText("nop");
                                }
                            } else {
                                nomp.setText("get failed with " + task.getException());
                            }
                        }
                    });
        }
    }

    private void getRDVbyIdPatient(String idpatient, RDVsCallback callback) {
        ArrayList<HomeViewModel> donnerRDV = new ArrayList<>();
        db.collection("RDV").whereEqualTo("id_patient", idpatient).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // Aucun rendez-vous trouvé, retourner "Aucun"
                    callback.onCallback(new ArrayList<>());
                    return;
                }
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String date = document.getString("date");
                    String heur = document.getString("heur");
                    String idDoctor = document.getString("id_medcin");

                    getDataDoctor(idDoctor, new DoctorCallback() {
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






    private void getDataDoctor(String idDocto, DoctorCallback callback) {
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