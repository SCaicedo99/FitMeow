package com.example.fitmeow;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    EditText nameInput;
    Spinner genderInput;
    Spinner deviceInput;
    BluetoothDevice[] btArray;
    EditText cweightInput;
    EditText iweightInput;
    EditText brandInput;
    EditText foodInfoInput;
    Button calculateButton;
    String name;
    String brand;
    float foodInfo;
    int gender;
    int device;
    int cweight;
    int iweight;
    BluetoothAdapter bluetoothAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        nameInput = (EditText) view.findViewById(R.id.input_name);
        brandInput = (EditText) view.findViewById(R.id.food_brand);
        foodInfoInput = (EditText) view.findViewById(R.id.food_info);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        deviceInput = (Spinner) view.findViewById(R.id.spinner2);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
        String[] strings=new String[bt.size()];
        btArray=new BluetoothDevice[bt.size()];
        int index=0;

        if( bt.size()>0)
        {
            for(BluetoothDevice device : bt)
            {
                btArray[index]= device;
                strings[index]=device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,strings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deviceInput.setAdapter(arrayAdapter);
            deviceInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    device = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }//else, an alert prompt telling users to go to the Bluetooth setting of the phone to discover and pair devices

        calculateButton = (Button) view.findViewById(R.id.calculate_button);


        final SharedPreferences testings = getContext().getSharedPreferences("CatProfile", 0);
        final SharedPreferences.Editor editor = testings.edit();
        int curWeight = testings.getInt("CurrentWeight", 0);
        if (curWeight != 0) {
            editor.putBoolean("NotFirstTime", true);
            name = testings.getString("CatName", " ");
            cweight = testings.getInt("CurrentWeight", 0);
            iweight = testings.getInt("IdealWeight", 0);
            brand = testings.getString("FoodBrand", " ");
            foodInfo = testings.getFloat("FoodInfo", 0);

            nameInput.setText(name);
            cweightInput.setText(Integer.toString(cweight));
            iweightInput.setText(Integer.toString(iweight));
            brandInput.setText(brand);
            foodInfoInput.setText(Float.toString(foodInfo));
            calculateButton.setText("UPDATE");

        }
//            try {
//                fileOutputStream = getContext().openFileOutput("Profile File.txt", Context.MODE_APPEND);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cweight = Integer.valueOf(cweightInput.getText().toString());
                iweight = Integer.valueOf(iweightInput.getText().toString());
                name = nameInput.getText().toString();
                brand = brandInput.getText().toString();
                foodInfo = Float.valueOf(foodInfoInput.getText().toString());

                editor.putString("CatName", name);
                editor.putInt("CurrentWeight", cweight);
                editor.putInt("IdealWeight",iweight);
                editor.putString("FoodBrand", brand);
                editor.putFloat("FoodInfo", foodInfo);
                editor.putInt("Device", device);
                editor.apply();

//                    try {
//                        fileOutputStream.write(name.getBytes());
//                        fileOutputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                if(testings.getBoolean("NotFirstTime", false)){
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_blueToothFragment);
                }else {
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_finishFragment);
                }
            }
        });

        return view;
    }
}