package com.example.fitmeow;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    Button startButton;
    Button editButton;
    Button viewGraphButton;
    View fteView;
    View norView;
    int curWeight;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;

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

//        try {
//            appendLog("klklklklklk");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        fteView = (View) view.findViewById(R.id.FTESetup);
        norView = (View) view.findViewById(R.id.normalSetup);
        SharedPreferences settings = getContext().getSharedPreferences("CatProfile", 0);
        curWeight = settings.getInt("CurrentWeight", 0);
        if(curWeight !=0){
            fteView.setVisibility(View.GONE);
        }else{//first-time
            norView.setVisibility(View.GONE);
        }

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }

        startButton = (Button) view.findViewById(R.id.start_button);
        editButton = (Button) view.findViewById(R.id.editProfileButton);
        viewGraphButton = (Button) view.findViewById(R.id.viewGraphButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("As a first-time user, please setup the profile of your cat!");
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});
                alert.create().show();
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment);

            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
        viewGraphButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_blueToothFragment);
            }
        });

        return view;
    }

//    public void appendLog(String text) throws IOException {
//        File file = new File(Environment.DIRECTORY_DOWNLOADS + File.separator + "test.txt");
//        try{
//            file.createNewFile();
//        } catch (IOException e) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//            alert.setTitle("You Suck!");
//        }
//
//        byte[] data1={1,1,0,0};
//        //write the bytes in file
//        if(file.exists())
//        {
//            OutputStream fo = new FileOutputStream(file);
//            fo.write(data1);
//            fo.close();
//            System.out.println("file created: "+file);
//        }
////        File logFile = new File("Download/log.file");
////        if (!logFile.exists())
////        {
////            try {
////                logFile.createNewFile();
////            } catch (IOException e) {
////                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
////                alert.setTitle("You Suck!");
////            }
////
////        }
////        try
////        {
////            //BufferedWriter for performance, true to set append to file flag
////            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
////            buf.append(text);
////            buf.newLine();
////            buf.close();
////        }
////        catch (IOException e)
////        {
////            e.printStackTrace();
////        }
//    }

}