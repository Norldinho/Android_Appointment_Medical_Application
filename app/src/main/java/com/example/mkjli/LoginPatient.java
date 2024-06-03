package com.example.mkjli;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginPatient extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        loadingProgressBar = findViewById(R.id.loading_progress);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginPatient.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register = findViewById(R.id.textView35);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPatient.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If user is already signed in, redirect to another activity
            updateUI(currentUser);
        }
    }

    private void loginUser(String email, String password) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Toast.makeText(LoginPatient.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginPatient.this,MainActivity.class);
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                String patientToken = task.getResult();
                                                // Enregistrer le token dans la base de données Firebase associée à l'utilisateur connecté
                                                saveTokenToDatabase(patientToken);

                                            } else {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                            }
                                        }
                                    });
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPatient.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Redirect the user to another activity here
        }
    }

    private void saveTokenToDatabase(String token) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Référence au document utilisateur dans la collection "patients"
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("patients").document(userId);

            // Mettre à jour le champ "token" dans le document utilisateur
            userRef.update("token", token)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Token ajouté à la base de données : " + token))
                    .addOnFailureListener(e -> Log.e(TAG, "Erreur lors de l'ajout du token à la base de données : " + e.getMessage()));
        }
    }

}
