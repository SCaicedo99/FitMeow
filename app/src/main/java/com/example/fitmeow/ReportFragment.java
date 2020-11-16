package com.example.fitmeow;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    TextView resultStatement;
    TextView resultTitle;
    String name;
    int totalCalBurnt;
    int amountofFood;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null);
        resultTitle = (TextView) view.findViewById(R.id.result_title);
        resultStatement = (TextView) view.findViewById(R.id.result_statement);
//        try {
//            FileInputStream fileInputStream = getContext().openFileInput("Profile File.txt");
//            Scanner scanner = new Scanner(fileInputStream);
//            scanner.useDelimiter("/");
//            while(scanner.hasNext()) {
//                name = scanner.next();
//            }
//            scanner.close();
//            SharedPreferences settings = getContext().getSharedPreferences("totalCalBurnt", 0);
//            totalCalBurnt = settings.getInt("totalCalBurnt", 0);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        SharedPreferences calories = getContext().getSharedPreferences("totalCalBurnt", 0);
        SharedPreferences profile = getContext().getSharedPreferences("CatProfile", 0);

        totalCalBurnt = calories.getInt("totalCalBurnt", 0);
        name = profile.getString("CatName", "null");
        amountofFood = (int) Math.round(totalCalBurnt/4.4);
        resultTitle.setText("Daily Report");
        resultStatement.setText(name + " has burnt a total of " + Integer.toString(totalCalBurnt) +
                " Calories today." + "\n\nWe recommend " + Integer.toString(amountofFood) + " grams of food for feeding.");
        return view;
    }
}