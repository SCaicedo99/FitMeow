package com.example.fitmeow;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    TextView resultStatement;
    TextView resultTitle;
    String name;
    Button backButton;
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
        backButton = (Button) view.findViewById(R.id.backButton);
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_reportFragment_to_blueToothFragment);
            }
        });
        return view;
    }
}