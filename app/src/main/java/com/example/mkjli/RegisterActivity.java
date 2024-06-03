package com.example.mkjli;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNom;
    private EditText editTextPrenom;
    private EditText editTextNumTelephone;
    private EditText editTextAdresse;
    private EditText editTextEmail;
    private EditText editTextDateNaissance;
    private EditText editTextPassword; // Ajout du champ de mot de passe
    private RadioGroup radioGroupSexe; // Ajout du groupe de boutons radio pour le sexe
    private Button buttonRegister;
    private boolean isDeleting = false;
    private TextView rgister;

    private FirebaseAuth mAuth; // Authentification Firebase
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Initialize views
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNumTelephone = findViewById(R.id.editTextNumTelephone);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword); // Initialisation du champ de mot de passe
        buttonRegister = findViewById(R.id.buttonRegister);
        radioGroupSexe = findViewById(R.id.radioGroupSexe); // Initialisation du groupe de boutons radio pour le sexe

        editTextDateNaissance = findViewById(R.id.editTextDateNaissance);

        editTextDateNaissance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ne rien faire avant que le texte change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Ne rien faire lorsque le texte change
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 2 || input.length() == 5) {
                    if (!input.endsWith("/")) {
                        editTextDateNaissance.setText(String.format("%s/", s.toString()));
                        editTextDateNaissance.setSelection(editTextDateNaissance.getText().length());
                    }
                }
            }
        });

        // Set onClickListener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                final String nom = editTextNom.getText().toString().trim();
                final String prenom = editTextPrenom.getText().toString().trim();
                final String numTelephone = editTextNumTelephone.getText().toString().trim();
                final String adresse = editTextAdresse.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String dateNaissance = editTextDateNaissance.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim(); // Récupération du mot de passe



                // Get selected sexe
                int selectedSexeId = radioGroupSexe.getCheckedRadioButtonId();
                RadioButton radioButtonSexe = findViewById(selectedSexeId);
                final String sexe = radioButtonSexe.getText().toString(); // Récupération du sexe

                // Validate input
                if (nom.isEmpty() || prenom.isEmpty() || numTelephone.isEmpty() || adresse.isEmpty() ||
                        email.isEmpty() || dateNaissance.isEmpty() || password.isEmpty() || sexe.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Create user with email and password using Firebase Auth
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Continue with Firestore data creation
                                        createFirestorePatient(user, nom, prenom, numTelephone, adresse, email, dateNaissance, sexe);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    private void createFirestorePatient(FirebaseUser user, String nom, String prenom, String numTelephone, String adresse, String email, String dateNaissance, String sexe) {
        if (user != null) {
            // Create a new patient

            Map<String, Object> patientData = new HashMap<>();
            patientData.put("nom", nom);
            patientData.put("prenom", prenom);
            patientData.put("numTelephone", numTelephone);
            patientData.put("adresse", adresse);
            patientData.put("email", email);
            patientData.put("dateNaissance", dateNaissance);
            patientData.put("sexe", sexe); // Ajout du sexe du patient

            // Add the patient to Firestore
            mFirestore.collection("patients").document(user.getUid())
                    .set(patientData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Patient enregistré avec succès", Toast.LENGTH_SHORT).show();
                                Intent k = new Intent(RegisterActivity.this, LoginPatient.class);
                                startActivity(k);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Erreur lors de l'enregistrement du patient. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
