package com.example.mkjli.medcin.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.mkjli.DateUtil.getCurrentDayAndMonth;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkjli.R;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

interface RDVXCaallback {
    void onCallback(ArrayList<MaladeView> homeViewModellist);
}
interface PatientCallbacks {
    void onCallback(PatentDonnee patentDonnee);
}
public class PatientFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;

    private RecyclerView.Adapter adapterfoodlist;
    private RecyclerView recyclerViewfood;
    private String mParam1;
    private String mParam2;
    private TextView dateText;
    private ExtendedFloatingActionButton cancelAllButton;

    public PatientFragment() {
        // Required empty public constructor
    }


    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
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
        return inflater.inflate(R.layout.fragment_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        dateText = view.findViewById(R.id.textView5);
        cancelAllButton = view.findViewById(R.id.cancel_all);
        dateText.setText(getCurrentDayAndMonth());



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();
        ArrayList<MaladeView> items = new ArrayList<>();

        getRDVbyIdPatient(udi,getCurrentDayAndMonth(),new RDVXCaallback() {
            @Override
            public void onCallback(ArrayList<MaladeView> homeViewModellist) {
                if (homeViewModellist != null) {
                    for (MaladeView rdv : homeViewModellist) {
                        items.add(new MaladeView(rdv.getNameMalade(), rdv.getNumberMalade(), rdv.getHeurRDV(), rdv.getJourRDV(),udi,rdv.getId()));
                    }
                } else {

                    Toast.makeText(getContext(), "Aucun rendez-vous trouvé.", Toast.LENGTH_SHORT).show();
                }

                adapterfoodlist.notifyDataSetChanged();
            }
        });


        AtomicReference<String> dateselected = new AtomicReference<>();
        dateselected.set(getCurrentDayAndMonth());
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year1, month1, dayOfMonth) -> {
                            String monthAbbreviation = getMonthAbbreviation(month1);
                            dateText.setText(String.valueOf(dayOfMonth) + "/" + monthAbbreviation); // Afficher la date dans la TextView
                            String selectedDate = String.valueOf(dayOfMonth) + "/" + monthAbbreviation;
                            dateselected.set(String.valueOf(dayOfMonth) + "/" + monthAbbreviation);
                            getRDVbyIdPatient(udi, selectedDate, new RDVXCaallback() {
                                @Override
                                public void onCallback(ArrayList<MaladeView> homeViewModellist) {
                                    // Supprimer les anciens rendez-vous de la liste
                                    items.clear();
                                    // Vérifier s'il y a des rendez-vous
                                    if (homeViewModellist != null && !homeViewModellist.isEmpty()) {
                                        // Ajouter les nouveaux rendez-vous à la liste
                                        items.addAll(homeViewModellist);
                                    } else {
                                        // Aucun rendez-vous trouvé, afficher un message ou effectuer une action supplémentaire si nécessaire
                                        Toast.makeText(getContext(), "Aucun rendez-vous trouvé pour cette date.", Toast.LENGTH_SHORT).show();
                                    }
                                    // Actualiser le RecyclerView avec les nouveaux rendez-vous ou une liste vide
                                    adapterfoodlist.notifyDataSetChanged();
                                }
                            });
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        recyclerViewfood = view.findViewById(R.id.recyclerViewsas);
        recyclerViewfood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewfood.setHasFixedSize(true);

        adapterfoodlist = new AdapterHomeDoctor(items, getContext());
        recyclerViewfood.setAdapter(adapterfoodlist);

        cancelAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("هل تريد حقًا حذف جميع المواعيد لهذا التاريخ؟" + dateselected);
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // حذف جميع المواعيد لهذا التاريخ
                        deleteAllRDVForDate(String.valueOf(dateselected));
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // إغلاق صندوق الحوار
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



    }
    private void deleteAllRDVForDate(String dateselected) {
        // Obtenez une référence à la collection "RDV" dans Firestore
        CollectionReference rdvCollectionRef = FirebaseFirestore.getInstance().collection("RDV");

        // Effectuer une requête pour récupérer tous les rendez-vous pour la date sélectionnée
        rdvCollectionRef.whereEqualTo("date", dateselected).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Parcourir tous les documents récupérés
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Supprimer chaque rendez-vous
                        rdvCollectionRef.document(documentSnapshot.getId()).delete()
                                .addOnSuccessListener(aVoid -> {
                                    // La suppression réussie
                                    Log.d(TAG, "Rendez-vous supprimé avec succès.");
                                })
                                .addOnFailureListener(e -> {
                                    // Gérer les erreurs lors de la suppression
                                    Log.e(TAG, "Erreur lors de la suppression du rendez-vous.", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Gérer les erreurs lors de la récupération des rendez-vous
                    Log.e(TAG, "Erreur lors de la récupération des rendez-vous pour la date sélectionnée.", e);
                });
    }


    private void getRDVbyIdPatient(String udi, String date_selectionee, RDVXCaallback callback) {
        ArrayList<MaladeView> donnerRDV = new ArrayList<>();
        db.collection("RDV")
                .whereEqualTo("id_medcin", udi)
                .whereEqualTo("date", date_selectionee) // Utiliser la date actuelle filtrée
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
                            getDataPatient(idPatient, new PatientCallbacks() {
                                @Override
                                public void onCallback(PatentDonnee patientDonnee) {
                                    if (patientDonnee != null) {
                                        String nomm = patientDonnee.getNom();
                                        String prenomm = patientDonnee.getPrenom();
                                        String numtel = patientDonnee.getNumtel();
                                        // Ajoutez les données à la liste une fois qu'elles sont disponibles
                                        donnerRDV.add(new MaladeView(nomm + " " + prenomm, numtel, heur, date_selectionee, idPatient,document.getId()));
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

    private void getDataPatient(String idDocto, PatientCallbacks callback) {
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
    private String getMonthAbbreviation(int month) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthAbbreviations = symbols.getShortMonths();
        return monthAbbreviations[month];
    }


}