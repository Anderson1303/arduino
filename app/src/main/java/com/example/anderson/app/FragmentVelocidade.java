package com.example.anderson.app;

/**
 * Created by anderson on 22/07/17.
 */

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import com.example.androidbtcontrol.R;

public class FragmentVelocidade extends Fragment {
    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<BluetoothDevice> pairedDeviceArrayList;

    TextView textInfo, textStatus;
    ListView listViewPairedDevice;
    LinearLayout inputPane;
    Button btnVelocidade1,btnVelocidade2;
    ImageButton btn_frente, btn_direita, btn_esqueerda, btn_traz;
    Button btn_para;
    Handler handler = new Handler();

    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        textInfo = (TextView) view.findViewById(R.id.info);
        textStatus = (TextView) view.findViewById(R.id.status);
        listViewPairedDevice = (ListView) view.findViewById(R.id.pairedlist);

        inputPane = (LinearLayout) view.findViewById(R.id.inputpane);

        btn_frente = (ImageButton) view.findViewById(R.id.btn_cima);
        btn_esqueerda = (ImageButton) view.findViewById(R.id.btn_esquerda);
        btn_direita = (ImageButton) view.findViewById(R.id.btn_direita);
        btn_traz = (ImageButton) view.findViewById(R.id.btn_baixo);
        btn_para = (Button) view.findViewById(R.id.btn_parar);
        btnVelocidade1 = (Button) view.findViewById(R.id.vel_1);
        btnVelocidade2 = (Button) view.findViewById(R.id.vel_2);

        try {

            btn_frente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "F".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

            btn_esqueerda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "E".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

            btn_direita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "D".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

            btn_traz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "T".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

            btn_para.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "P".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });
            btnVelocidade1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "100".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

            btnVelocidade2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myThreadConnected != null) {
                        byte[] bytesToSend = "200".getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }
            });

        }catch (NullPointerException null1){}
        //using the well-known SPP UUID
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        String stInfo = bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device);
            }

            pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(getContext(),
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition(position);

                    textStatus.setText("start ThreadConnectBTdevice");
                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
                    myThreadConnectBTdevice.start();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myThreadConnectBTdevice != null) {
            myThreadConnectBTdevice.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                setup();
            } else {
                Toast.makeText(getContext(),
                        "Bluetooth não está habilitado",
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket) {

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    /*
    ThreadConnectBTdevice:
    Background Thread to handle BlueTooth connecting
    */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("tentar novamente \n");
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if (success) {
                //connect successful
                final String msgconnected = "conectado:\n";
                //  + "BluetoothSocket: " + bluetoothSocket + "\n"
                //  + "BluetoothDevice: " + bluetoothDevice;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText(msgconnected);

                        listViewPairedDevice.setVisibility(View.VISIBLE);
                        inputPane.setVisibility(View.VISIBLE);
                    }
                });

                startThreadConnected(bluetoothSocket);
            } else {
                //erro
            }
        }

        public void cancel() {

            Toast.makeText(getActivity(),
                    "conexao fechada",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
    public class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = String.valueOf(bytes) +
                            " informação recebida:\n"
                            + strReceived;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textStatus.setText(msgReceived);
                        }
                    });

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Desconectado";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textStatus.setText(msgConnectionLost);
                        }
                    });
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}