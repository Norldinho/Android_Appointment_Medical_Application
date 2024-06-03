package com.example.mkjli.ui.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Logique de traitement des messages ici
        Log.d(TAG, "Message received: " + remoteMessage.getData());
    }



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        // Enregistrer le token dans la base de données Firebase associée à l'utilisateur connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userTokenRef = FirebaseDatabase.getInstance().getReference("patients").child(userId).child("token");
            userTokenRef.setValue(token)
                    .addOnSuccessListener(aVoid -> Log.d("Token", "Token ajouté à la base de données : " + token))
                    .addOnFailureListener(e -> Log.e("Token", "Erreur lors de l'ajout du token à la base de données : " + e.getMessage()));
        }
    }
}
