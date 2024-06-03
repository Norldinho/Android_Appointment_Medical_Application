package com.example.mkjli;



import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MaladerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String id_malade,date_rdv,heur_rdv,tell,nom,prenom,sexe,age,email,datenai;
    private TextView nomMalad,daterdv,heurrdv,tel;
    private ImageView imageTel,iimage;
    private FirebaseFirestore db;
    private ExtendedFloatingActionButton cancel;

    public MaladerFragment() {
        // Required empty public constructor
    }

    public static MaladerFragment newInstance(String param1, String param2) {
        MaladerFragment fragment = new MaladerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            id_malade = bundle.getString("id_malade");
            date_rdv = bundle.getString("jeur_rdv");
            heur_rdv = bundle.getString("heur_rdv");
            tell = bundle.getString("tell");
            // Récupérez d'autres données si nécessaire
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_malader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        cancel = view.findViewById(R.id.cancel);
        nomMalad = view.findViewById(R.id.maladName1);
        daterdv = view.findViewById(R.id.textView34);
        heurrdv = view.findViewById(R.id.textView33);
        tel = view.findViewById(R.id.numbermalad1);
        imageTel = view.findViewById(R.id.imageView3v);
        iimage = view.findViewById(R.id.imageView14);
        daterdv.setText(date_rdv);
        heurrdv.setText(heur_rdv);
        fetchMaladFromFirestore(id_malade);

        imageTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer l'intent pour ouvrir l'application de téléphone avec le numéro de téléphone
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // Ajouter le numéro de téléphone à l'intent
                intent.setData(Uri.parse("tel:" + tell));
                // Vérifier si l'application de téléphone est disponible avant de démarrer l'activité
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Gérer le cas où aucune application de téléphone n'est disponible
                    Toast.makeText(getContext(), "Aucune application de téléphone disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });



        iimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un StringBuilder pour construire le message du dialogue
                StringBuilder message = new StringBuilder();
                // Ajouter le titre "Nom du malade" en gras
                message.append("<b>Nom du malade</b>: ").append(nom+" "+prenom).append("<br>");
                // Ajouter l'âge du patient
                message.append("<b>Âge</b>: ").append(age).append("<br>");
                // Ajouter le sexe du patient
                message.append("<b>Sexe</b>: ").append(sexe).append("<br>");
                // Ajouter l'email du patient
                message.append("<b>Email</b>: ").append(email);

                // Créer une nouvelle instance de AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Définir le titre du dialogue
                builder.setTitle("Informations du patient");
                // Définir le message du dialogue (converti en HTML pour le texte en gras)
                builder.setMessage(Html.fromHtml(message.toString()));
                // Ajouter un bouton "OK" au dialogue
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fermer le dialogue lorsque l'utilisateur clique sur le bouton "OK"
                        dialog.dismiss();
                    }
                });

                // Afficher le dialogue
                AlertDialog dialog = builder.create();
                dialog.show();

                // Définir le texte en gras pour le titre du dialogue
                TextView titleView = dialog.findViewById(getResources().getIdentifier("alertTitle", "id", "android"));
                if (titleView != null) {
                    titleView.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("هل تريد حقًا حذف الموعد؟" + date_rdv);
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Supprimer le rendez-vous du patient


                        senEmail();
                        deleteRDVByIDmalade(id_malade);


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

    private void senEmail() {
        String mEmail = email;
        String mSubject = "Simpler";
        String mMessage = "تم إلغاء موعدك من قبل الطبيب ";


        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mEmail, mSubject, mMessage);

        javaMailAPI.execute();
    }
    private void deleteRDVByIDmalade(String id_m) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();

        // Référence à la collection de rendez-vous
        CollectionReference rdvCollectionRef = FirebaseFirestore.getInstance().collection("RDV");

        // Effectuer une requête pour obtenir les documents correspondants
        rdvCollectionRef.whereEqualTo("id_patient", id_m)
                .whereEqualTo("id_medcin", udi)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Parcourir les documents récupérés
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Supprimer chaque document de la collection de rendez-vous
                        deleteRDV(document.getId());
                    }
                })
                .addOnFailureListener(e -> {
                    // Gérer les erreurs lors de la récupération des rendez-vous
                    Log.e(TAG, "Erreur lors de la récupération des RDVs", e);
                });
    }

    // Méthode pour supprimer un rendez-vous spécifique par son ID
    private void deleteRDV(String rdvId) {
        // Référence à la collection de rendez-vous
        CollectionReference rdvCollectionRef = FirebaseFirestore.getInstance().collection("RDV");

        // Supprimer le rendez-vous spécifié par son ID
        rdvCollectionRef.document(rdvId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Le rendez-vous a été supprimé avec succès
                    Log.d(TAG, "RDV supprimé avec succès");
                    Toast.makeText(getContext(), "RDV supprimé avec succès " , Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Gérer les erreurs lors de la suppression du rendez-vous
                    Log.e(TAG, "Erreur lors de la suppression du RDV", e);
                    Toast.makeText(getContext(), "Erreur lors de la suppression du RDV " , Toast.LENGTH_SHORT).show();
                });
    }



    private void fetchMaladFromFirestore(String id_patient) {
        db.collection("patients")
                .document(id_patient)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Le document du patient existe dans Firestore
                        // Récupérez les données du document et utilisez-les comme vous le souhaitez
                        String nomPatient = documentSnapshot.getString("nom");
                        String prenomPatient = documentSnapshot.getString("prenom");
                        String numTel = documentSnapshot.getString("numTelephone");
                        String sexee = documentSnapshot.getString("sexe");
                        String em = documentSnapshot.getString("email");
                        String datenaissence  = documentSnapshot.getString("dateNaissance");
                        nomMalad.setText(nomPatient+" "+prenomPatient);
                        tel.setText(numTel);
                        nom = nomPatient;
                        prenom = prenomPatient;
                        sexe = sexee;
                        email = em;
                        datenai = datenaissence;
                        age = String.valueOf(calculateAgeFromDateOfBirth(datenai));


                    } else {
                        // Le document du patient n'existe pas dans Firestore pour cet identifiant

                    }
                })
                .addOnFailureListener(e -> {
                    // Gérer les erreurs lors de la récupération des données du patient depuis Firestore

                });
    }

    private int calculateAgeFromDateOfBirth(String dateOfBirth) {
        // Divisez la date de naissance en jour, mois et année
        String[] parts = dateOfBirth.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; // Les mois commencent à partir de 0 dans Calendar
        int year = Integer.parseInt(parts[2]);

        // Obtenez la date actuelle
        Calendar currentCalendar = Calendar.getInstance();

        // Créez un objet Calendar pour la date de naissance
        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.set(year, month, day);

        // Calcul de la différence entre la date actuelle et la date de naissance
        int age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }


    private void sendNotificationToPatient(String patientToken, String title, String message) {
        // Créer le contenu de la notification
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("message", message);

        // Créer le message de notification
        RemoteMessage.Builder builder = new RemoteMessage.Builder(patientToken);
        builder.setData(notificationData);
        RemoteMessage notificationMessage = builder.build();

        // Envoyer le message de notification via FCM
        FirebaseMessaging.getInstance().send(notificationMessage);
    }


}