package com.example.mkjli;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mkjli.medcin.MainActivity_for_doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView nomDoctor, sipDoctor, ta2kid_1 , ta2kid_2;
    private EditText heur_d,heur_f,nomber_p,dure_n,dure_d,dure_c,prix_n,prix_d,prix_c;
    ExtendedFloatingActionButton go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_planning);


        db = FirebaseFirestore.getInstance();

        ta2kid_1 =  findViewById(R.id.textView49y);
        ta2kid_2 =  findViewById(R.id.textView49hdy);
        heur_d =  findViewById(R.id.editTextTimey);
        heur_f =  findViewById(R.id.editTextTime2y);
        nomber_p =  findViewById(R.id.editTextNumbery);
        dure_n =  findViewById(R.id.editTextTime3y);
        dure_d =  findViewById(R.id.editTextTimex3y);
        dure_c =  findViewById(R.id.editTextTimex3qy);
        prix_n =  findViewById(R.id.editTextTime3xzy);
        prix_d =  findViewById(R.id.editTextTime3xxzy);
        prix_c =  findViewById(R.id.editTextTime3xxzqy);
        go = findViewById(R.id.cancely);
        CheckBox checkboxTout =  findViewById(R.id.checkbox_touty);
        CheckBox checkBoxTout2 =  findViewById(R.id.checkbox_tout2y);
        Spinner spinner =  findViewById(R.id.spinnery);
        Spinner spinner2 =  findViewById(R.id.spinner2y);
        Spinner spinner3 =  findViewById(R.id.spinner3y);

        List<String> categories = new ArrayList<>();
        categories.add("الأحد");
        categories.add("الاثنين");
        categories.add("الثلاثاء");
        categories.add("الأربعاء");
        categories.add("الخميس");
        categories.add("الجمعة");
        categories.add("السبت");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PlanningActivity.this, android.R.layout.simple_spinner_item, categories);
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
                    Toast.makeText(PlanningActivity.this,"no",Toast.LENGTH_LONG).show();
                }
            }
        });


        ta2kid_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifierConsulation(dure_n.getText().toString(),dure_d.getText().toString(),dure_c.getText().toString(),prix_n.getText().toString(),prix_d.getText().toString(),prix_c.getText().toString());
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanningActivity.this, MainActivity_for_doctor.class);
                startActivity(intent);
            }
        });
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
                        Toast.makeText(PlanningActivity.this,"Nouveau document de planing ajouté avec succès !",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de l'ajout du nouveau document
                        Log.e(TAG, "Erreur lors de l'ajout du nouveau document de planing", e);
                        Toast.makeText(PlanningActivity.this,"Erreur lors de l'ajout du nouveau document de planing",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void modifierConsulation(String dure_n, String dure_d, String dure_c, String prix_n, String prix_d, String prix_c){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference planningCollection = FirebaseFirestore.getInstance().collection("consultation");
        planningCollection.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document supprimé avec succès
                        Log.d(TAG, "Document de planing existant supprimé avec succès !");

                        // Maintenant, ajoutez le nouveau document de planing
                        ajouterNouveauConsulation(uid, dure_n, dure_d, dure_c, prix_n, prix_d,prix_c);

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

    private void ajouterNouveauConsulation(String uid, String dureN, String dureD, String dureC, String prixN, String prixD, String prixC) {
        CollectionReference cnsultationCollection = FirebaseFirestore.getInstance().collection("consultation");
        Map<String, Object> consultationData = new HashMap<>();
        consultationData.put("dureN", dureN);
        consultationData.put("dureD", dureD);
        consultationData.put("dureC", dureC);
        consultationData.put("prixN", prixN);
        consultationData.put("prixD", prixD);
        consultationData.put("prixC", prixC);

        cnsultationCollection.document(uid)
                .set(consultationData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Nouveau document de planing ajouté avec succès
                        Log.d(TAG, "Nouveau document de planing ajouté avec succès !");
                        Toast.makeText(PlanningActivity.this,"Nouveau document de planing ajouté avec succès !",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de l'ajout du nouveau document
                        Log.e(TAG, "Erreur lors de l'ajout du nouveau document de planing", e);
                        Toast.makeText(PlanningActivity.this,"Erreur lors de l'ajout du nouveau document de planing",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}