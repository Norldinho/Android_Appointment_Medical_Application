package com.example.mkjli.ui.lorem;

import static android.content.ContentValues.TAG;
import static com.example.mkjli.DateUtil.getCurrentDayAndMonth;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;
import com.example.mkjli.databinding.FragmentLoremBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

interface RDVCallback {
    void onCallback(ArrayList<RDV> rdvList);
}

interface PlaningCallback {
    void onCallback(Planing planing);
}

interface PatientExistCallback {
    void onPatientExist(boolean exists);
}

interface ConsultationCallback {
    void onConsultationReceived(ArrayList<Consultation> consultations);
}


public class LoremFragment extends Fragment {

    private Spinner spinnertype;

    private RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private TextView nomprenom,t,tt;
    private TextView spis;
    private ImageView ilogo,pphone;
    private String nomprenomView, date_f,numeroTelephone;
    private String mParam1;
    private String mParam2;
    private ImageView add;
    private Bundle bundle;
    ArrayList<Consultation> kk;
    private String id;
    private HeurAdapter adapter;

    private FragmentLoremBinding binding;
    private String selectedDate;

    public LoremFragment() {
        // Required empty public constructor
    }

    public static LoremFragment newInstance(String id) {
        LoremFragment fragment = new LoremFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoremBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
        nomprenom = view.findViewById(R.id.textView7);
        spis = view.findViewById(R.id.textView8);
        pphone = view.findViewById(R.id.hbelt);
        ilogo = view.findViewById(R.id.imageView7);
        recyclerView = view.findViewById(R.id.rcid2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView jour = view.findViewById(R.id.textView13);
        TextView month = view.findViewById(R.id.textView15);
        t = view.findViewById(R.id.textView54);
        tt = view.findViewById(R.id.textView53);
        Calendar calendar = Calendar.getInstance();

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int monthOfYear = calendar.get(Calendar.MONTH);

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());

        String formattedDay = dayFormat.format(calendar.getTime());
        String formattedMonth = monthFormat.format(calendar.getTime());

        jour.setText(formattedDay);
        month.setText(formattedMonth);




        // Récupérer le Bundle et les données supplémentaires
        if (getArguments() != null) {
            id = getArguments().getString("id");

            searchDoctorById(id);

            getRDVbyIdMedcin(id, getCurrentDayAndMonth(), new RDVCallback() {
                @Override
                public void onCallback(ArrayList<RDV> rdvList) {
                    int n = rdvList.size();
                    getPlaningByIdDoctor(id, new PlaningCallback() {
                        @Override
                        public void onCallback(Planing planing) {
                            int n_pa = Integer.valueOf(planing.getPatient_jour());
                            n_pa = n_pa - n;
                            String heur_depar = planing.getHeur_depart();
                            updateRecyclerViewData(n_pa, heur_depar,rdvList,String.valueOf(dayOfMonth),formattedMonth);
                        }
                    });
                }
            });

            binding.textView15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            (view1, year1, month1, dayOfMonth) -> {
                                String monthAbbreviation = getMonthAbbreviation(month1);
                                binding.textView15.setText(monthAbbreviation); // Afficher la date dans la TextView
                                binding.textView13.setText(String.valueOf(dayOfMonth));
                                getRDVbyIdMedcin(id, String.valueOf(dayOfMonth) + "/" + monthAbbreviation, new RDVCallback() {
                                    @Override
                                    public void onCallback(ArrayList<RDV> rdvList) {
                                        int n = rdvList.size();
                                        getPlaningByIdDoctor(id, new PlaningCallback() {
                                            @Override
                                            public void onCallback(Planing planing) {
                                                int n_pa = Integer.valueOf(planing.getPatient_jour());
                                                n_pa = n_pa - n;
                                                String heur_depar = planing.getHeur_depart();
                                                updateRecyclerViewData(n_pa, heur_depar,rdvList,String.valueOf(dayOfMonth),monthAbbreviation);
                                            }
                                        });
                                    }
                                });

                            }, year, month, day);

                    datePickerDialog.show();
                }
            });



            ilogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchConsultationByIdDoctor(id, new ConsultationCallback() {
                        @Override
                        public void onConsultationReceived(ArrayList<Consultation> consultations) {
                            AlertDialog.Builder builderr = new AlertDialog.Builder(getContext());
                            builderr.setMessage("فحص عادي"+"    "+consultations.get(0).getDure_n()+ "   " + consultations.get(0).getPrix_n()+"DA" + "\n" + "فحص حاد" +"  " +consultations.get(0).getDure_d() + "   " + consultations.get(0).getPrix_d()+"DA" +
                                    "\n" +  "فحص مجهول"+"   "+consultations.get(0).getDure_c()+"    "+consultations.get(0).getPrix_c()+"DA");
                            builderr.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // L'utilisateur a cliqué sur le bouton "تأكيد", vous pouvez gérer l'action de confirmation ici
                                    // Par exemple, afficher un message ou effectuer une action spécifique
                                    dialog.dismiss();
                                }
                            });

                            // Créer et afficher la boîte de dialogue
                            AlertDialog dialog = builderr.create();
                            dialog.show();
                        }
                    });

                }

            });




        }
        pphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer l'intent pour ouvrir l'application de téléphone avec le numéro de téléphone
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // Ajouter le numéro de téléphone à l'intent
                intent.setData(Uri.parse("tel:" + numeroTelephone));
                // Vérifier si l'application de téléphone est disponible avant de démarrer l'activité
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Gérer le cas où aucune application de téléphone n'est disponible
                    Toast.makeText(getContext(), "Aucune application de téléphone disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getMonthAbbreviation(int month) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthAbbreviations = symbols.getShortMonths();
        return monthAbbreviations[month];
    }

//    private ArrayList<Consultation> searchConsultationByIdDoctor(String doctorId) {
//
//        ArrayList<Consultation> donnerConsulation = new ArrayList<>(); // La liste doit être finale pour être accessible dans l'écouteur onComplete
//
//        db.collection("consultation").whereEqualTo("id_medecin", doctorId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String type = document.getString("type");
//                        String prix = document.getString("prix");
//                        String idco = document.getId();
//
//                        donnerConsulation.add(new Consultation(type, prix));
//                    }
//                    // Ici, vous pouvez notifier un adaptateur ou mettre à jour l'interface utilisateur avec la liste mise à jour
//                } else {
//                    // Gérer l'échec de la recherche ici
//                }
//            }
//        });
//
//        return donnerConsulation; // Cette ligne ne fonctionnera pas comme prévu, car la requête est asynchrone
//    }

    private void searchConsultationByIdDoctor(String doctorId, ConsultationCallback callback) {
        ArrayList<Consultation> donnerConsulation = new ArrayList<>();

        DocumentReference consultationDocument = FirebaseFirestore.getInstance().collection("consultation").document(doctorId);

        consultationDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String dure_n = document.getString("dureN");
                        String dure_d = document.getString("dureD");
                        String dure_c = document.getString("dureC");
                        String prix_n = document.getString("prixN");
                        String prix_d = document.getString("prixD");
                        String prix_c = document.getString("prixC");
                        donnerConsulation.add(new Consultation(dure_n,dure_d,dure_c,prix_n,prix_d,prix_c));
                    } else {
                        Log.d(TAG, "Aucune consultation trouvée pour l'ID du médecin: " + doctorId);
                    }
                    // Une fois que les données ont été récupérées, appeler la méthode de rappel avec la liste des consultations
                    callback.onConsultationReceived(donnerConsulation);
                } else {
                    Log.e(TAG, "Erreur lors de la récupération de la consultation: ", task.getException());
                }
            }
        });
    }





    private void searchDoctorById(String doctorId) {
        db.collection("medecins").document(doctorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Affichez les détails du médecin ici
                        // Par exemple:
                        String doctorName = document.getString("nom");
                        String doctorPrenom = document.getString("prenom");
                        String doctornumTel = document.getString("telephone");
                        String doctorspi = document.getString("specialite");
                        String doctorwilaya = document.getString("wilaya");
                        numeroTelephone = doctornumTel;
                        // Utilisez doctorName pour mettre à jour l'interface utilisateur
                        nomprenom.setText("د."+doctorName+" "+doctorPrenom+" ");
                        nomprenomView = ("د."+doctorName+" "+doctorPrenom+" ");
                        spis.setText(doctorspi);
                        t.setText(doctornumTel);
                        tt.setText(doctorwilaya);

                    } else {
                        Toast.makeText(getContext(), "Doctor not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error getting doctor details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void ajouterRDV(String idMedecin, String dateRDV, String heureRDV) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        // Créer un nouvel objet RDV

        Map<String, Object> rdv = new HashMap<>();
        rdv.put("id_patient", currentUser.getUid());
        rdv.put("id_medcin", idMedecin);
        rdv.put("date", dateRDV);
        rdv.put("heur", heureRDV);



        // Ajouter le RDV à la collection Firestore
        db.collection("RDV").add(rdv)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Le RDV a été ajouté avec succès", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Erreur lors de l'ajout du RDV", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRDVbyIdMedcin(String idmedcin,String date_selectionee, RDVCallback callback) {
        ArrayList<RDV> donnerRDV = new ArrayList<>();
        db.collection("RDV").whereEqualTo("id_medcin", idmedcin).whereEqualTo("date",date_selectionee).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String date = document.getString("date");
                        String heur = document.getString("heur");
                        donnerRDV.add(new RDV(date, heur));
                    }
                    callback.onCallback(donnerRDV); // Utilisez le callback pour retourner les résultats
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la récupération des RDVs", Toast.LENGTH_LONG).show();
                    callback.onCallback(new ArrayList<>()); // Retournez une liste vide en cas d'erreur
                }
            }
        });
    }
//    private void updateRecyclerViewData(ArrayList<RDV> m) {
//        // Obtenez vos données basées sur la date sélectionnée
//        List<RDV> newData = getDataForDate(m);
//        // Mettez à jour les données de l'adaptateur
//        if (adapter == null) {
//            adapter = new HeurAdapter(newData,getContext() );
//            recyclerView.setAdapter(adapter);
//        } else {
//            adapter.updateData(newData);
//        }
//    }


    private void updateRecyclerViewData(int m , String h,ArrayList<RDV> rdvs,String j, String ms) {
        // Obtenez vos données basées sur la date sélectionnée
        List<RDV> newData = getDataForDate(m,h,rdvs);
        // Mettez à jour les données de l'adaptateur
        if (adapter == null) {

            adapter = new HeurAdapter(newData, getContext(), getChildFragmentManager(), this);


            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(newData);
        }

        if (adapter != null) {
            adapter.setSelectedDate(j + "/" + ms);
        }
    }

    private List<RDV> getDataForDate(int nomber_max, String heur, ArrayList<RDV> rdvs) {
        List<RDV> data = new ArrayList<>();
        boolean[] existingHours = new boolean[24 * 4]; // Un tableau de boolean pour représenter chaque tranche de 15 minutes dans une journée

        // Remplir le tableau des heures existantes
        for (RDV rdv : rdvs) {
            String[] parts = rdv.getHeur().split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            existingHours[hour * 4 + minute / 15] = true;
        }

        String heur_comilativ = heur;

        for (int j = 1; j <= nomber_max; j++) {
            // Vérifier si l'heure cumulative est déjà présente dans rdvs
            String[] parts = heur_comilativ.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            if (!existingHours[hour * 4 + minute / 15]) {
                data.add(new RDV(heur_comilativ));
            }

            // Passez à l'heure suivante
            heur_comilativ = addMinutes(heur_comilativ, 15);
        }
        return data;
    }



    private String addMinutes(String heure, int minutesToAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date = sdf.parse(heure);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return heure; // En cas d'erreur, retourne l'heure d'origine
        }
    }
    private void getPlaningByIdDoctor(String id, PlaningCallback callback) {
        DocumentReference planningRef = FirebaseFirestore.getInstance()
                .collection("planing")
                .document(id);

        // Récupérez les données du document de planing
        planningRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Vérifiez si le document existe
                        if (documentSnapshot.exists()) {
                            // Le document existe, récupérez les données
                            String heurDepart = documentSnapshot.getString("heur_depart");
                            String heurFin = documentSnapshot.getString("heur_fin");
                            String jourDepart = documentSnapshot.getString("jour_depart");
                            String jourFin = documentSnapshot.getString("jour_fin");
                            String patientJour = documentSnapshot.getString("patient_jour");
                            Planing planing = new Planing(heurDepart, heurFin, jourDepart, jourFin, patientJour);
                            callback.onCallback(planing);
                        } else {
                            // Aucun document trouvé
                            callback.onCallback(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gestion des erreurs
                        Log.e("Firestore", "Erreur lors de la récupération du planing", e);
                        callback.onCallback(null);
                    }
                });
    }


    public void checkPatientExist(String idMedcin, final PatientExistCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Créer une requête pour vérifier si le patient a déjà un rendez-vous avec ce médecin
            db.collection("RDV")
                    .whereEqualTo("id_patient", currentUser.getUid())
                    .whereEqualTo("id_medcin", idMedcin)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Vérifiez si des documents correspondants ont été trouvés
                                boolean exists = !task.getResult().isEmpty();
                                // Utilisez la valeur "exists" dans votre logique
                                callback.onPatientExist(exists);
                            } else {
                                // Une erreur s'est produite lors de la récupération des données
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                // Indiquez que le patient n'existe pas (car une erreur s'est produite)
                                callback.onPatientExist(false);
                            }
                        }
                    });
        }
    }









}
