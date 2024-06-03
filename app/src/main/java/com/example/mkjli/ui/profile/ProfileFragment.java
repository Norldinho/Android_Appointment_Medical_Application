package com.example.mkjli.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;
import com.example.mkjli.databinding.FragmentHomeBinding;
import com.example.mkjli.databinding.FragmentProfileBinding;
import com.example.mkjli.ui.home.HomeViewModel;
import com.example.mkjli.ui.home.TrendsAdapter;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private RecyclerView.Adapter adapterfoodlist;
    private RecyclerView recyclerViewfood;


    private FragmentProfileBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ProfileFragment(){

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<HomeViewModel> items = new ArrayList<>();

//        items.add(new HomeViewModel("younes","koko","11-22","3 may"));
//        items.add(new HomeViewModel("haithem","koko","11-22","3 may"));
//        items.add(new HomeViewModel("houda","koko","11-22","3 may"));
        recyclerViewfood = view.findViewById(R.id.view22);
        recyclerViewfood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewfood.setHasFixedSize(true);

        adapterfoodlist = new TrendsAdapter(getContext(),items);
        recyclerViewfood.setAdapter(adapterfoodlist);
        adapterfoodlist.notifyDataSetChanged();
    }



}