package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.adapter.LinearHistoryItemAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView records;
    public View snackbar_bottom;
    private TextView text1, text2;
    private ImageView icon1;
    LinearHistoryItemAdapter adapter;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        records = getView().findViewById(R.id.history_items);
        snackbar_bottom = getView().findViewById(R.id.snackbar_bottom);
        records.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LinearHistoryItemAdapter(getActivity(),  this);
        records.setAdapter(adapter);
        text1 = getView().findViewById(R.id.text1);
        text2 = getView().findViewById(R.id.text2);
        icon1 = getView().findViewById(R.id.icon1);
        text1.setOnClickListener(this::reverseSort);
        icon1.setOnClickListener(this::reverseSort);
        refreshHistoryAmount();
    }

    public RecyclerView getRecords() {
        return records;
    }

    public void notifyHistoryAdded(File resultDir) {

        if (!adapter.sortFromBefore) {adapter.insertItem(0, resultDir);}
        else {
            adapter.insertItem(adapter.getItemCount(), resultDir);
        }
    }

    private void reverseSort(View v) {
        adapter.reverseItems();
        if (adapter.sortFromBefore) {
            text1.setText("日期顺序:升序");
        }
        else {
            text1.setText("日期顺序:降序");
        }
    }

    public void refreshHistoryAmount() {
        String s = "共" + adapter.getItemCount() + "条历史记录";
        text2.setText(s);
    }
}