package com.example.mkjli.ui.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.MainActivity;
import com.example.mkjli.R;
import com.example.mkjli.ResultFragment;
import com.example.mkjli.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {

    private DoctorDonnee doctorDonnee;

    private FirebaseFirestore db;
    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter adapter2;
    private RecyclerView recyclerView2;

    private RecyclerView.Adapter adapter3;
    private RecyclerView recyclerView3;
    private FragmentAddBinding binding;
    private Spinner spinnerSpecialite, spinnerWilaya;
    private EditText nomdoctor;
    private TextView search;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        // Initialisation des spinners
        spinnerSpecialite = view.findViewById(R.id.spinnerSpecialite);
        spinnerWilaya = view.findViewById(R.id.spinnerWilaya);
        nomdoctor = view.findViewById(R.id.editTextText2);
        search = view.findViewById(R.id.textView23x);

        nomdoctor.requestFocus();


        // Définition des données pour les spinners
        String[] specialiteArray = {
                "",
                "طب الاسنان",
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
                "",
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
        ArrayAdapter<String> specialiteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, specialiteArray);
        specialiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> wilayaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, wilayaArray);
        wilayaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Liaison des adaptateurs aux spinners
        spinnerSpecialite.setAdapter(specialiteAdapter);
        spinnerWilaya.setAdapter(wilayaAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                String selectedSpecialite = spinnerSpecialite.getSelectedItem().toString();
                String selectedWilaya = spinnerWilaya.getSelectedItem().toString();
                String searchTerm = nomdoctor.getText().toString();
                if (searchTerm.isEmpty() && selectedSpecialite.isEmpty() && selectedWilaya.isEmpty()) {
                    nomdoctor.setError("entrer ton recherch svp");
                } else if (searchTerm.isEmpty() && !selectedSpecialite.isEmpty() && selectedWilaya.isEmpty()) {
                    searchDoctorBySpiscialite(selectedSpecialite);
                } else if (!searchTerm.isEmpty() && selectedSpecialite.isEmpty() && selectedWilaya.isEmpty()) { //tmchi
                    searchDoctorByName(searchTerm);
                } else if (!searchTerm.isEmpty() && !selectedSpecialite.isEmpty() && !selectedWilaya.isEmpty()) { //tmchi
                    searchDoctorByNameAndSpicsialteAndWilaya(searchTerm, selectedSpecialite, selectedWilaya);
                } else if (searchTerm.isEmpty() && !selectedSpecialite.isEmpty() && !selectedWilaya.isEmpty()) { //tmchi
                    searchDoctorBySpicialteAndWilaya(selectedSpecialite,selectedWilaya);
                } else if (!searchTerm.isEmpty() && selectedSpecialite.isEmpty() && !selectedWilaya.isEmpty()) { //tmchi
                    searchDoctorByNameAndByWilaya(searchTerm,selectedWilaya);
                } else if (!searchTerm.isEmpty() && !selectedSpecialite.isEmpty() && selectedWilaya.isEmpty()) {  //tmchi
                    searchDoctorByNameAndBySpicsialite(searchTerm,selectedSpecialite);
                }


            }
        });



    }

    private void searchDoctorByNameAndBySpicsialite(String doctorname, String selectedSpecialite) {
        db.collection("medecins")
                .whereEqualTo("nom", doctorname)
                .whereEqualTo("specialite", selectedSpecialite)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String id = document.getId();
                                String wilaya = document.getString("wilaya");
                                String spisia = document.getString("specialite");
                                donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));

                            }
                            // je veut deplacer au auter fragment
                            ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchDoctorByNameAndByWilaya(String doctorname, String doctorWilaya) {
        db.collection("medecins")
                .whereEqualTo("nom", doctorname)
                .whereEqualTo("wilaya", doctorWilaya)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String id = document.getId();
                                String wilaya = document.getString("wilaya");
                                String spisia = document.getString("specialite");
                                donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));

                            }
                            // je veut deplacer au auter fragment
                            ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchDoctorBySpicialteAndWilaya(String doctorSpicsialite, String doctorWilaya) {
        db.collection("medecins")
                .whereEqualTo("specialite", doctorSpicsialite)
                .whereEqualTo("wilaya", doctorWilaya)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String id = document.getId();
                                String wilaya = document.getString("wilaya");
                                String spisia = document.getString("specialite");
                                donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));

                            }
                            // je veut deplacer au auter fragment
                            ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchDoctorByNameAndSpicsialteAndWilaya(String doctorName, String doctorSpicsialite, String doctorWilaya) {
        db.collection("medecins")
                .whereEqualTo("nom", doctorName)
                .whereEqualTo("specialite", doctorSpicsialite)
                .whereEqualTo("wilaya", doctorWilaya)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String id = document.getId();
                                String wilaya = document.getString("wilaya");
                                String spisia = document.getString("specialite");
                                donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));

                            }
                            // je veut deplacer au auter fragment
                            ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchDoctorByName(String doctorName) {
        db.collection("medecins")
                .whereEqualTo("nom", doctorName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String id = document.getId();
                                String wilaya = document.getString("wilaya");
                                String spisia = document.getString("specialite");
                                donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));

                            }
                            // je veut deplacer au auter fragment
                            ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void searchDoctorBySpiscialite(String spicialite) {
        db.collection("medecins")
                .whereEqualTo("specialite",spicialite)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DoctorDonnee> donnerDoctorList = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String nom = document.getString("nom");
                        String prenom = document.getString("prenom");
                        String id = document.getId();
                        String wilaya = document.getString("wilaya");
                        String spisia = document.getString("specialite");
                        donnerDoctorList.add(new DoctorDonnee(nom,prenom,id,wilaya,spisia));
                    }
                    ((MainActivity) requireActivity()).goToResultFragment(donnerDoctorList);
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la recherche du médecin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
