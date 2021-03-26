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
 * Use the {@link FinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishFragment extends Fragment {

    TextView resultStatement;
    TextView resultTitle;
    String name;
    Button continueButton;

    public FinishFragment() {
        // Required empty public constructor
    }

    public static FinishFragment newInstance(String param1, String param2) {
        FinishFragment fragment = new FinishFragment();
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
        View view = inflater.inflate(R.layout.fragment_finish, null);
        resultTitle = (TextView) view.findViewById(R.id.result_title);
        resultStatement = (TextView) view.findViewById(R.id.result_statement);
        continueButton = (Button) view.findViewById(R.id.continueButton);

        SharedPreferences profile = getContext().getSharedPreferences("CatProfile", 0);

        name = profile.getString("CatName", "null");

        resultTitle.setText("You are all set!");
        resultStatement.setText("Now please put the FitMeow collar on " + name + " to help the Meow get Fit");
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_finishFragment_to_blueToothFragment);
            }
        });
        return view;
    }
}