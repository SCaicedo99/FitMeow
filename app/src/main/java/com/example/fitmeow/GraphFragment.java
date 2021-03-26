package com.example.fitmeow;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;


public class GraphFragment extends Fragment {

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    Button goToReportButton;
    Button refreshButton;
    float totalCalBurnt;
    TextView status;

    Set<String> dataSet = new HashSet<String>();
    Object[] plotData = new Object[24];
    LinkedList<String> data = new LinkedList<String>();
    int dataCollect;
    FileInputStream fis;

    public GraphFragment() {
        // Required empty public constructor
    }

    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, null);

        status = (TextView) view.findViewById(R.id.status);

        barChart = (BarChart) view.findViewById(R.id.bargraph);

        goToReportButton = (Button) view.findViewById(R.id.viewGraph);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        goToReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_graphFragment_to_reportFragment);
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_graphFragment_to_blueToothFragment);
            }
        });

        createRandomBarGraph("0:00", "23:59");// This order needs to be considered...

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createRandomBarGraph(String Date1, String Date2){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();

            SharedPreferences getter = getContext().getSharedPreferences("GraphData", 0);
            String filename = getter.getString("Filename", " ");
//            String filename = "data.txt";

            try {
                fis = getActivity().openFileInput(filename);
//                fis = getActivity().openFileInput("data.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader;
            try  {
                reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    data.add(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


//            float max = 0f;
            float value = 0f;
            totalCalBurnt = 0f;
            random = new Random();
            for(int j = 0; j< dates.size();j++){
//                max = 30f;
//                value = random.nextFloat()*max;
                value = Math.abs(Float.valueOf(data.get(j)));
                while(value > 100){
                    value -= 100;
                }
                totalCalBurnt += value;
                barEntries.add(new BarEntry(value,j));
            }

            SharedPreferences settings = getContext().getSharedPreferences("totalCalBurnt", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("totalCalBurnt", (int)totalCalBurnt);

            editor.apply();

        }catch(ParseException e){
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Hourly Calories Burnt");
        BarData barData = new BarData(dates, barDataSet);
        barChart.setData(barData);
        barChart.setDescription("Activity Graph");
        status.setText("end");
    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.HOUR_OF_DAY,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.HOUR_OF_DAY) + ":" + (cld.get(Calendar.MINUTE));
        try{
            Date date = new SimpleDateFormat("HH:mm").parse(curDate);
            curDate =  new SimpleDateFormat("HH:mm").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }

}