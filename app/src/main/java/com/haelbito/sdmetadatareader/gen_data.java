package com.haelbito.sdmetadatareader;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.CardAdder;
import helpers.SharedViewModel;
import imgMetaData.ImageMetaData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gen_data#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gen_data extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedViewModel sharedViewModel;

    public gen_data() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gen_data.
     */
    // TODO: Rename and change types and number of parameters
    public static gen_data newInstance(String param1, String param2) {
        gen_data fragment = new gen_data();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_data, container, false);

        LinearLayout  frame = view.findViewById(R.id.gen_data_layout);

        ArrayList<String> params = new ArrayList<>();
        params.add(getString(R.string.pos_prompt));
        params.add(getString(R.string.neg_prompt));
        params.add(getString(R.string.steps));
        params.add(getString(R.string.sampler));
        params.add(getString(R.string.cfg));
        params.add(getString(R.string.seed));
        params.add(getString(R.string.size));



        // Example: Observe data changes
        sharedViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ImageMetaData imageMetaData = ImageMetaData.getInstance();

            //frame.removeAllViews();


            for (String param : params) {
                String contentText = imageMetaData.get(requireContext(), param);

                CardAdder.addCard(param, contentText, frame, inflater, view);
            }


        });
        return view;
    }


}