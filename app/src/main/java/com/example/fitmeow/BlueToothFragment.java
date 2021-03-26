package com.example.fitmeow;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class BlueToothFragment extends Fragment {

    TextView status;
    Button viewGraphButton;
    Button refreshButton;
    TextView ready;
    TextView textView47;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    ClientClass clientClass;

    Set<String> dataSet = new HashSet<String>();
    int dataCollect = 0;
    SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String APP_NAME = "Meow";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    SharedPreferences testings;
    SharedPreferences.Editor editor;
    FileOutputStream fOut;
    FileInputStream fis;
    LinkedList<String> data = new LinkedList<String>();
    Charset charset = StandardCharsets.UTF_8;
    String filename;


    public BlueToothFragment() {
        // Required empty public constructor
    }

    public static BlueToothFragment newInstance() {
        BlueToothFragment fragment = new BlueToothFragment();
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
        View view = inflater.inflate(R.layout.fragment_blue_tooth, null);

        status = (TextView) view.findViewById(R.id.status);

        ready = (TextView) view.findViewById(R.id.statusText);

        viewGraphButton = (Button) view.findViewById(R.id.viewGraph);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        viewGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
                Navigation.findNavController(view).navigate(R.id.action_blueToothFragment_to_graphFragment);
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        testings = getContext().getSharedPreferences("GraphData", 0);
        editor = testings.edit();

        receivePlottingData();

        textView47 = (TextView) view.findViewById(R.id.textView47);


        return view;

    }

    private void receivePlottingData() {

        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;

        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            SharedPreferences settings = getContext().getSharedPreferences("CatProfile", 0);
            int i = settings.getInt("Device", 0);
            clientClass = new ClientClass(btArray[i]);
            clientClass.start();

            status.setText("Connecting");

        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    String tempMsg = null;
                    String readMessage = (String) msg.obj;
                    if (readMessage!=null) tempMsg = readMessage.trim();

                    if(dataCollect == 0){
                        filename = tempMsg+ ".txt";

                        editor.putString("Filename", filename);
                        editor.apply();
                    }

                    //write data into file
                    try {
                        fOut = getActivity().openFileOutput(filename, Context.MODE_APPEND);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fOut.write(tempMsg.getBytes(charset));
                        fOut.write('\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    dataSet.add(tempMsg);
                    textView47.append(tempMsg);
                    textView47.append(", ");
                    dataCollect++;
                    if(dataCollect == 24){
                        stop();
                        try {
                            fis = getActivity().openFileInput(filename);
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
                        ready.setText(data.toString());
                    }
                    break;
            }
            return true;
        }
    });

    public synchronized void stop() {
        cancelClientThread();
        cancelSendReceiveThread();
    }

    public synchronized void connected(BluetoothSocket socket) {
        cancelClientThread();
        sendReceive = new SendReceive(socket);
        sendReceive.start();
    }

    private void cancelClientThread() {
        if (clientClass != null) {
            clientClass.cancel();
            clientClass = null;
        }
    }

    private void cancelSendReceiveThread() {
        if (sendReceive != null) {
            sendReceive.cancel();
            sendReceive = null;
        }
    }

    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        public ServerClass() {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;

            while (socket == null) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1) {
            device = device1;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

//                sendReceive=new SendReceive(socket);
//                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }

            synchronized (BlueToothFragment.this) {
                clientClass = null;
            }

            connected(socket);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket) {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            //int i = 0;

            StringBuilder readMessage = new StringBuilder();//added

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    String read = new String(buffer, 0, bytes);//added
                    readMessage.append(read);//added
                    if(read.contains("\n")) {//added
//                        handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                        handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, readMessage.toString()).sendToTarget();
                        readMessage.setLength(0);
                    }
                    //i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}