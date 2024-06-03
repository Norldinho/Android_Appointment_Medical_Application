package com.example.mkjli.medcin.lorem;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkjli.R;
import com.example.mkjli.ui.lorem.Planing;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlaningFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private TextView nomDoctor, sipDoctor, ta2kid_1 , ta2kid_2;
    private EditText heur_d,heur_f,nomber_p,dure_n,Dure_d,dure_c,prix_n,prix_d,prix_c;

    public PlaningFragment() {

    }


    public static PlaningFragment newInstance(String param1, String param2) {
        PlaningFragment fragment = new PlaningFragment();
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
        return inflater.inflate(R.layout.fragment_planing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        nomDoctor = view.findViewById(R.id.textViewop);
        sipDoctor = view.findViewById(R.id.textView3dd1);
        ta2kid_1 = view.findViewById(R.id.textView49);
        ta2kid_2 = view.findViewById(R.id.textView49hd);
        heur_d = view.findViewById(R.id.editTextTime);
        heur_f = view.findViewById(R.id.editTextTime2);
        nomber_p = view.findViewById(R.id.editTextNumber);
        dure_n = view.findViewById(R.id.editTextTime3);
        Dure_d = view.findViewById(R.id.editTextTimex3);
        dure_c = view.findViewById(R.id.editTextTimex3q);
        prix_n = view.findViewById(R.id.editTextTime3xz);
        prix_d = view.findViewById(R.id.editTextTime3xxz);
        prix_c = view.findViewById(R.id.editTextTime3xxzq);
        CheckBox checkboxTout = view.findViewById(R.id.checkbox_tout);
        CheckBox checkBoxTout2 = view.findViewById(R.id.checkbox_tout2);
        Spinner spinner = view.findViewById(R.id.spinner);
        Spinner spinner2 = view.findViewById(R.id.spinner2);
        Spinner spinner3 = view.findViewById(R.id.spinner3);

        List<String> categories = new ArrayList<>();
        categories.add("الأحد");
        categories.add("الاثنين");
        categories.add("الثلاثاء");
        categories.add("الأربعاء");
        categories.add("الخميس");
        categories.add("الجمعة");
        categories.add("السبت");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        spinner3.setAdapter(dataAdapter);

        ta2kid_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxTout2.isChecked() && !heur_d.getText().toString().isEmpty() && !heur_f.getText().toString().isEmpty() && !nomber_p.getText().toString().isEmpty()) {
                    modifierPlaning(spinner2.getSelectedItem().toString(), spinner3.getSelectedItem().toString(), heur_d.getText().toString(), heur_f.getText().toString(), nomber_p.getText().toString());
                } else {
                    Toast.makeText(getContext(),"no",Toast.LENGTH_LONG).show();
                }
            }
        });


        ta2kid_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    nomDoctor.setText(document.getData().get("nom").toString() + " " +document.getString("prenom"));
                                } else {
                                    nomDoctor.setText("nop");
                                }
                            } else {
                                nomDoctor.setText("get failed with " + task.getException());
                            }
                        }
                    });
        }
    }

    private void modifierPlaning(String jour_depart, String jour_fin, String heure_depart, String heure_fin, String patient_jour) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();

        // Obtenez une référence à la collection "planning"
        CollectionReference planningCollection = FirebaseFirestore.getInstance().collection("planing");

        // Supprimer le document de planing existant du médecin
        planningCollection.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document supprimé avec succès
                        Log.d(TAG, "Document de planing existant supprimé avec succès !");

                        // Maintenant, ajoutez le nouveau document de planing
                        ajouterNouveauPlaning(uid, jour_depart, jour_fin, heure_depart, heure_fin, patient_jour);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de la suppression du document
                        Log.e(TAG, "Erreur lors de la suppression du document de planing existant", e);

                    }
                });
    }

    private void ajouterNouveauPlaning(String uid, String jour_depart, String jour_fin, String heure_depart, String heure_fin, String patient_jour) {
        // Obtenez une référence à la collection "planning"
        CollectionReference planningCollection = FirebaseFirestore.getInstance().collection("planing");

        // Créer un objet Map contenant les détails du nouveau planning
        Map<String, Object> planningData = new HashMap<>();
        planningData.put("heur_depart", heure_depart);
        planningData.put("heur_fin", heure_fin);
        planningData.put("jour_depart", jour_depart);
        planningData.put("jour_fin", jour_fin);
        planningData.put("patient_jour", patient_jour);

        // Ajouter le nouveau document de planing
        planningCollection.document(uid)
                .set(planningData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Nouveau document de planing ajouté avec succès
                        Log.d(TAG, "Nouveau document de planing ajouté avec succès !");
                        Toast.makeText(getContext(),"Nouveau document de planing ajouté avec succès !",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de l'ajout du nouveau document
                        Log.e(TAG, "Erreur lors de l'ajout du nouveau document de planing", e);
                        Toast.makeText(getContext(),"Erreur lors de l'ajout du nouveau document de planing",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
