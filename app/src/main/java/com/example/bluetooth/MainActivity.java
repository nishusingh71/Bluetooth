package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonSear;
    TextView textView;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> bluetoothAddress = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSear = findViewById(R.id.button_search);
        textView = findViewById(R.id.textView2);
        listView = findViewById(R.id.list_bluetooth);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        listView.setAdapter(arrayAdapter);


        buttonSear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textView.setText("Searching...");
                buttonSear.setEnabled(false);
                bluetoothDevices.clear();
                bluetoothAddress.clear();
                bluetoothAdapter.startDiscovery();

            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(broadcastReceiver, intentFilter);


    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            Log.i("Action", action);
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                textView.setText("Finished");
                buttonSear.setEnabled(true);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = bluetoothDevice.getName();
                String address = bluetoothDevice.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                if (!bluetoothAddress.contains(address)) {
                    bluetoothAddress.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")) {
                        deviceString = address + " -RSSI" + rssi + "dBm";
                    } else {
                        deviceString = name + " -RSSI" + rssi + "dBm";
                    }

                    bluetoothDevices.add(deviceString);

                    arrayAdapter.notifyDataSetChanged();
                }

            }

        }
    };
}