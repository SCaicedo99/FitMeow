package com.example.fitmeow;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class btFragment extends Fragment {

    Button viewGraphButton;
    Button refreshButton;
    TextView status;
    TextView ready;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
//    ClientClass clientClass;
    int[] plotData = new int[24];
    Set<String> dataSet = new HashSet<String>();
    int dataCollect = 0;

//    SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    private static final String APP_NAME = "Meow";
    private static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

//    final SharedPreferences testings = getContext().getSharedPreferences("GraphData", 0);
//    final SharedPreferences.Editor editor = testings.edit();

    public btFragment() {
        // Required empty public constructor
    }

    public static btFragment newInstance() {
        btFragment fragment = new btFragment();
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
        View view = inflater.inflate(R.layout.fragment_bt, null);

        status = (TextView) view.findViewById(R.id.status);

        ready = (TextView) view.findViewById(R.id.statusText);

        viewGraphButton = (Button) view.findViewById(R.id.viewGraph);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        viewGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stop();
//                Navigation.findNavController(view).navigate(R.id.action_btFragment_to_graphFragment);
            }
        });

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

//        receivePlottingData();

        // wait until received data then run this method to create graph
        //createRandomBarGraph("0:00", "23:59");// This order needs to be considered...

        return view;
    }

/*
    private void receivePlottingData() {

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
            SharedPreferences settings = getContext().getSharedPreferences("CatProfile", 0);
            int i = settings.getInt("Device", 0);
            clientClass=new ClientClass(btArray[i]);
            clientClass.start();

            status.setText("Connecting");

        }
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
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
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);

//                    plotData[dataCollect] = Math.abs(Integer.valueOf(tempMsg));
//                    int data = plotData[dataCollect];
//                    while (data > 100){
//                        data /= 10;
//                    }
//
//                    dataSet.add(Integer.toString(data));
//
//                    dataCollect++;
//                    if(dataCollect == 23){
//                        editor.putStringSet("Data", dataSet);
//                        editor.apply();
//                        ready.setText("Graph is ready!");
//                    }
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

    private void cancelClientThread(){
        if(clientClass != null){
            clientClass.cancel();
            clientClass = null;
        }
    }

    private void cancelSendReceiveThread(){
        if(sendReceive != null){
            sendReceive.cancel();
            sendReceive = null;
        }
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null)
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run()
        {
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

//                sendReceive=new SendReceive(socket);
//                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }

            synchronized (btFragment.this){
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

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;
            //int i = 0;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                    //i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
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
    }_*/

}