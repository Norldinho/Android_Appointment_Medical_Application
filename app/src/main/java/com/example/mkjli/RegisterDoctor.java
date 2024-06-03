package com.example.mkjli;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mkjli.ui.login.Login_for_doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterDoctor extends AppCompatActivity {

    private EditText editTextNom, editTextPrenom, editTextEmail, editTextTelephone, editTextMotDePasse;
    private Spinner spinnerWilaya, spinnerSpecialite;
    private RadioButton radioButtonHomme, radioButtonFemme;
    private Button buttonInscrire;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialisation des vues
        editTextNom = findViewById(R.id.editTextDoctorNom);
        editTextPrenom = findViewById(R.id.editTextDoctorPrenom);
        editTextEmail = findViewById(R.id.editTextDoctorEmail);
        editTextTelephone = findViewById(R.id.editTextDoctorTelephone);
        editTextMotDePasse = findViewById(R.id.editTextDoctorMotDePasse);
        spinnerWilaya = findViewById(R.id.spinnerDoctorWilaya);
        spinnerSpecialite = findViewById(R.id.spinnerDoctorSpecialite);
        radioButtonHomme = findViewById(R.id.radioButtonDoctorHomme);
        radioButtonFemme = findViewById(R.id.radioButtonDoctorFemme);
        buttonInscrire = findViewById(R.id.buttonDoctorInscrire);

        String[] specialiteArray = {
                "طب الأمراض الباطنية",
                "جراحة العظام",
                "طب الأطفال",
                "طب الأسنان",
                "طب الجلدية",
                "جراحة التجميل",
                "طب النساء والتوليد",
                "طب الأنف والأذن والحنجرة",
                "طب الأمراض العصبية",
                "جراحة القلب",
                "طب الجهاز التنفسي",
                "طب الأمراض العقلية",
                "طب العيون",
                "طب الطوارئ",
                "جراحة الجهاز الهضمي",
                "طب الأمراض العضلية والهيكلية",
                "طب الأمراض المعدية",
                "طب العلاج الطبيعي",
                "طب الأورام",
                "طب الأمراض الجراحية"
        };

        String[] wilayaArray = {
                "أدرار",
                "الشلف",
                "الأغواط",
                "أم البواقي",
                "باتنة",
                "بجاية",
                "بسكرة",
                "بشار",
                "البليدة",
                "البويرة",
                "تمنراست",
                "تبسة",
                "تلمسان",
                "تيارت",
                "تيزي وزو",
                "الجزائر",
                "الجلفة",
                "جيجل",
                "سطيف",
                "سعيدة",
                "سكيكدة",
                "سيدي بلعباس",
                "عنابة",
                "قالمة",
                "قسنطينة",
                "المدية",
                "مستغانم",
                "المسيلة",
                "معسكر",
                "ورقلة",
                "وهران",
                "البيض",
                "إليزي",
                "برج بوعريريج",
                "بومرداس",
                "الطارف",
                "تندوف",
                "تيسمسيلت",
                "الوادي",
                "خنشلة",
                "سوق أهراس",
                "تيبازة",
                "ميلة",
                "عين الدفلى",
                "النعامة",
                "عين تموشنت",
                "غرداية",
                "غليزان",
                "تيميمون",
                "برج باجي مختار",
                "أولاد جلال",
                "بني عباس",
                "إن صالح",
                "عين قزام",
                "تقرت",
                "جانت",
                "المغير",
                "المنيعة"
        };

        // Création des adaptateurs pour les spinners
        ArrayAdapter<String> specialiteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialiteArray);
        specialiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> wilayaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wilayaArray);
        wilayaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Liaison des adaptateurs aux spinners
        spinnerSpecialite.setAdapter(specialiteAdapter);
        spinnerWilaya.setAdapter(wilayaAdapter);

        // Écouteur pour le bouton "Inscrire"
        buttonInscrire.setOnClickListener(v -> inscrireMedecin());
    }

    private void inscrireMedecin() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telephone = editTextTelephone.getText().toString().trim();
        String motDePasse = editTextMotDePasse.getText().toString().trim();
        String wilaya = spinnerWilaya.getSelectedItem().toString();
        String specialite = spinnerSpecialite.getSelectedItem().toString();
        String sexe = radioButtonHomme.isChecked() ? "Homme" : "Femme";

        // Vérifier si les champs requis sont vides
        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(motDePasse)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inscription de l'utilisateur avec Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, motDePasse)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // L'utilisateur est inscrit avec succès
                        String userId = mAuth.getCurrentUser().getUid();
                        // Enregistrer les informations du médecin dans Firestore
                        Map<String, Object> medecin = new HashMap<>();
                        medecin.put("nom", nom);
                        medecin.put("prenom", prenom);
                        medecin.put("email", email);
                        medecin.put("telephone", telephone);
                        medecin.put("wilaya", wilaya);
                        medecin.put("specialite", specialite);
                        medecin.put("sexe", sexe);

                        db.collection("medecins").document(userId)
                                .set(medecin)
                                .addOnSuccessListener(aVoid -> {
                                    // Succès de l'inscription dans Firestore
                                    Toast.makeText(RegisterDoctor.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterDoctor.this, Login_for_doctor.class);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    // Échec de l'inscription dans Firestore
                                    Log.e("RegisterDoctor", "Erreur lors de l'inscription dans Firestore", e);
                                    Toast.makeText(RegisterDoctor.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Échec de l'inscription avec Firebase Auth
                        Log.e("RegisterDoctor", "Erreur lors de l'inscription avec Firebase Auth", task.getException());
                        Toast.makeText(RegisterDoctor.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
