package com.example.qrcodescanner;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrcodescanner.adapters.ViewViewPagerAdapter;
import com.example.qrcodescanner.models.Viewitems;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    ViewPager pager;
    ViewViewPagerAdapter adapter;
    List<Viewitems> viewitems;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        viewitems = new ArrayList<>();
        pager = view.findViewById(R.id.ViewPagerView);
        viewitems.add(new Viewitems("View Attendance by Date","firstview.json"));
        viewitems.add(new Viewitems("View Overall Attendance","secondview.json"));
        adapter = new ViewViewPagerAdapter(getContext(),viewitems);
        pager.setAdapter(adapter);


    // Inflate the layout for this fragment
        return view;
    }

}
