package com.example.fitmeow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    Button startButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, null);
        startButton = (Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences settings = getContext().getSharedPreferences("CatProfile", 0);
        int curWeight = settings.getInt("CurrentWeight", 0);

//        File file = getContext().getFileStreamPath("Profile File.txt");
        if (curWeight != 0) {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_graphFragment);
        }
        else {

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("As a first-time user, please setup the profile of your cat!");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}});
            alert.create().show();
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
        }
    }
}