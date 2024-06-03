package com.example.mkjli.ui.login;
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

import com.example.mkjli.LoginPatient;
import com.example.mkjli.MainActivity;
import com.example.mkjli.PlanningActivity;
import com.example.mkjli.R;
import com.example.mkjli.RegisterDoctor;
import com.example.mkjli.medcin.MainActivity_for_doctor;
import com.example.mkjli.medcin.home.HomeDoctorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_for_doctor extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private ProgressBar loadingProgressBar;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_doctor);

        editTextUsername = findViewById(R.id.usernamedocter);
        editTextPassword = findViewById(R.id.passworddocter);
        buttonLogin = findViewById(R.id.login_buttondd);
        progressBar = findViewById(R.id.loadingdoctor);
        register = findViewById(R.id.textView36q);
        loadingProgressBar = findViewById(R.id.loadingdoctor);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_for_doctor.this, RegisterDoctor.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(Login_for_doctor.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }






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

                            Toast.makeText(Login_for_doctor.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_for_doctor.this, PlanningActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_for_doctor.this, "Authentication failed.",
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
}
