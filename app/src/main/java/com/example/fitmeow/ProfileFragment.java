package com.example.fitmeow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public PageViewModel pageViewModel;
    EditText nameInput;
    Spinner genderInput;
    EditText cweightInput;
    EditText iweightInput;
    Button calculateButton;
    String name;
    int gender;
    int cweight;
    int iweight;
    TextView result;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this, new PageFactory("", 0,0,1)).get(PageViewModel.class);
//        pageViewModel = new ViewModelProvider(this, new PageFactory()).get(PageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        nameInput = (EditText) view.findViewById(R.id.input_name);
        cweightInput = (EditText) view.findViewById(R.id.input_current_weight);
        iweightInput = (EditText) view.findViewById(R.id.input_ideal_weight);
        genderInput = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderInput.setAdapter(adapter);
        genderInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = position;
//                pageViewModel.setGender(gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        calculateButton = (Button) view.findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageViewModel.setGender(3);
                cweight = Integer.valueOf(cweightInput.getText().toString());
                iweight = Integer.valueOf(iweightInput.getText().toString());
                name = nameInput.getText().toString();
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_graphFragment);
            }
        });
        result = (TextView) view.findViewById(R.id.testing);

        pageViewModel.getGender().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                result.setText(Integer.toString(s));
            }
        });
        return view;
    }
}