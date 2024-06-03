package com.example.mkjli.medcin.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mkjli.R;
import com.example.mkjli.databinding.FragmentChatBinding;
import com.example.mkjli.ui.chat.ChatActivity;
import com.example.mkjli.ui.chat.ChatView;
import com.example.mkjli.ui.chat.ChatViewAdapter;
import com.example.mkjli.ui.chat.Medcin;

import com.example.mkjli.ui.chat.SearchUserRecyclerAdapter;
import com.example.mkjli.ui.chat.UserId;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


interface RDVdsCallback {
    void onCallback(ArrayList<Patient> maladsList);
}
interface NomsCallback {
    void onCallback(String nomPatioon);
}

public class ChatDoctorFragment extends Fragment {

    private RecyclerView.Adapter ChatViewAdapter;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewChatView;
    private FragmentChatBinding binding;
    SearchUserRecycler adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChatDoctorFragment() {
        // Required empty public constructor
    }


    public static ChatDoctorFragment newInstance(String param1, String param2) {
        ChatDoctorFragment fragment = new ChatDoctorFragment();
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
        return inflater.inflate(R.layout.fragment_chat_doctor, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerViewChatView = view.findViewById(R.id.rcylclr11doctor);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        Query query = db.collection("RDV")
                .whereEqualTo("id_medcin", currentUser.getUid()); // Remplacez "id_medcin" par le champ approprié si nécessaire

        FirestoreRecyclerOptions<UserId> options = new FirestoreRecyclerOptions.Builder<UserId>()
                .setQuery(query, UserId.class)
                .build();
        adapter = new SearchUserRecycler(options, getContext());
        recyclerViewChatView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChatView.setAdapter(adapter);
        adapter.startListening();

    }


}




















