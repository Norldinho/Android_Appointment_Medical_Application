package com.example.mkjli;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mkjli.ui.add.Adapter1;
import com.example.mkjli.ui.add.DoctorDonnee;
import com.example.mkjli.ui.add.Mdol1View;
import com.example.mkjli.ui.home.HomeViewModel;
import com.example.mkjli.ui.home.TrendsAdapter;
import com.example.mkjli.ui.lorem.LoremFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    private RecyclerView.Adapter adaptermedlist;
    private RecyclerView recyclerViewmed;

    private static final String ARG_DOCTOR_LIST = "doctorList";

    private List<DoctorDonnee> mDoctorList;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(List<DoctorDonnee> doctorList) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOCTOR_LIST, (Serializable) doctorList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDoctorList = (List<DoctorDonnee>) getArguments().getSerializable(ARG_DOCTOR_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewmed = view.findViewById(R.id.resrecherchview);
        recyclerViewmed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewmed.setHasFixedSize(true);

        adaptermedlist = new Adapter1(mDoctorList, getContext(), new Adapter1.OnItemClickListener() {
            @Override
            public void onItemClick(DoctorDonnee doctor) {
                // Handle the click event
                LoremFragment loremFragment = LoremFragment.newInstance(doctor.getIdd());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, loremFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerViewmed.setAdapter(adaptermedlist);
    }




}

