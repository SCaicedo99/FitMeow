package com.example.fitmeow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
//import com.example.fitmeow.ProfileFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    private PageViewModel pageViewModel;
    TextView result;
//    private ProfileFragment mProfileFragment;


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
        pageViewModel = new ViewModelProvider(this, new PageFactory("",0,0,2)).get(PageViewModel.class);
//        pageViewModel = new ViewModelProvider(this, new PageFactory()).get(PageViewModel.class);
//        mProfileFragment = new ProfileFragment();
//        pageViewModel = mProfileFragment.pageViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null);
        result = (TextView) view.findViewById(R.id.result_statement);

        pageViewModel.getGender().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                result.setText(Integer.toString(s));
            }
        });
        return view;
    }
}