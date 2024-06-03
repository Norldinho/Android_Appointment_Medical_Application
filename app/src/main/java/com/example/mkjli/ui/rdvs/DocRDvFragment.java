package com.example.mkjli.ui.rdvs;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkjli.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class DocRDvFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private String mParam1;
    private String mParam2;
    private ImageView imageTel,iimage;
    private String id_docor,date_rdv,heur_rdv,tell,email,sexe,nom,spisia;
    private TextView nomdoctor,spi,date,heur;
    private ExtendedFloatingActionButton cancel;

    public DocRDvFragment() {
        // Required empty public constructor
    }

    public static DocRDvFragment newInstance(String param1, String param2) {
        DocRDvFragment fragment = new DocRDvFragment();
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
            id_docor = bundle.getString("id_doctor");
            date_rdv = bundle.getString("jeur_rdv");
            heur_rdv = bundle.getString("heur_rdv");
            spisia = bundle.getString("spi");
            tell = bundle.getString("tell");
            email = bundle.getString("email");
            sexe = bundle.getString("sexe");
            nom = bundle.getString("nom");
            // Récupérez d'autres données si nécessaire
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doc_r_dv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nomdoctor = view.findViewById(R.id.ydoctorName1);
        spi = view.findViewById(R.id.numberyadocotor1);
        date = view.findViewById(R.id.textView34yadoctor);
        heur = view.findViewById(R.id.textView33yadoctor);
        imageTel = view.findViewById(R.id.imageView3vyadoctor);
        iimage = view.findViewById(R.id.imageView14yadoctor);
        cancel = view.findViewById(R.id.cancelyadoctor);

        nomdoctor.setText(nom);
        spi.setText(spisia);
        date.setText(date_rdv);
        heur.setText(heur_rdv);


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
                message.append("<b>Nom du medcin</b>: ").append(nom).append("<br>");
                // Ajouter le sexe du patient
                message.append("<b>Sexe</b>: ").append(sexe).append("<br>");
                // Ajouter l'email du patient
                message.append("<b>Email</b>: ").append(email);

                // Créer une nouvelle instance de AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Définir le titre du dialogue
                builder.setTitle("Informations du medcin");
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
                        // حذف جميع المواعيد لهذا التاريخ
                        deleteRDVByIDDoctor(String.valueOf(id_docor));
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

    private void deleteRDVByIDDoctor(String id_m) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String udi = currentUser.getUid();

        // Référence à la collection de rendez-vous
        CollectionReference rdvCollectionRef = FirebaseFirestore.getInstance().collection("RDV");

        // Effectuer une requête pour obtenir les documents correspondants
        rdvCollectionRef.whereEqualTo("id_patient", udi)
                .whereEqualTo("id_medcin", id_m)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Parcourir les documents récupérés
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Supprimer chaque document de la collection de rendez-vous
                        rdvCollectionRef.document(document.getId())
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
                })
                .addOnFailureListener(e -> {
                    // Gérer les erreurs lors de la récupération des rendez-vous
                    Log.e(TAG, "Erreur lors de la récupération des RDVs", e);
                });
    }
}