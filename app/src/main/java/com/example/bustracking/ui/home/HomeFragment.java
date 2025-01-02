package com.example.bustracking.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bustracking.AITRC_Vita;
import com.example.bustracking.MapActivity;
import com.example.bustracking.Model;
import com.example.bustracking.R;
import com.example.bustracking.RecycleViewAdapter;
import com.example.bustracking.Vita_Bhalavni;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecycleViewAdapter.OnBusStopClickListener {
    private RecycleViewAdapter adapter;
    private ArrayList<Model> modelArrayList;
    private RecyclerView recyclerView;



    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        modelArrayList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        modelArrayList.add(new Model("AITRC to Vita", "MH 10 BT 9999"));
        modelArrayList.add(new Model("Kadegaon to Vita", "MH 10 DT 1952"));
        modelArrayList.add(new Model("Bhalavani to Vita", "MH 10 DT 4437"));
        modelArrayList.add(new Model("Kurli to Vita", "MH 10 AW 5074"));
        modelArrayList.add(new Model("Dahivadi to Vita", "MH 10 DT 1801"));
        modelArrayList.add(new Model("Tasgaon to Vita", "MH 10 DT 3836"));
        modelArrayList.add(new Model("Chitali to Vita", "MH 10 DT 4432"));
        modelArrayList.add(new Model("Khanapur to Vita", "MH 10 DT 6720"));
        modelArrayList.add(new Model("Mayni to Vita", "MH 10 DT 3833"));
        modelArrayList.add(new Model("Kundal to Vita", "MH 10 DT 3834"));
        modelArrayList.add(new Model("Dudhondi to Vita", "MH 10 DT 5075"));
        modelArrayList.add(new Model("Mumbai to Vita", "MH 10 DT 0000  "));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecycleViewAdapter(modelArrayList, getContext(), this);
        recyclerView.setAdapter(adapter);

        return root;
    }
    @Override
    public void onBusStopClick(Model model) {
        Intent intent;
        switch (model.getName()) {  // Customize based on the unique attribute (e.g., name or ID)
            case "Bhalavani to Vita":
                intent = new Intent(getActivity(), Vita_Bhalavni.class);
                break;
            case "Kadegaon to Vita":
                intent = new Intent(getActivity(), MapActivity.class); // MapActivity For Kadegaon to vita
                break;
            case "AITRC to Vita":
                intent = new Intent(getActivity(), AITRC_Vita.class); // AIT College to vita
                break;
            // Add more cases for each bus stop
            default:
                intent = new Intent(getActivity(), MapActivity.class);  // Default activity
                break;
        }
        startActivity(intent);
    }
}
