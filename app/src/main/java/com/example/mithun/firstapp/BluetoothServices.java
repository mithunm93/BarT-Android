package com.example.mithun.firstapp;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;


/**
 * Created by Mithun on 3/11/15.
 */
public class BluetoothServices extends Service
{
    private final static String TAG = "Bluetooth Service";

    public static final UUID RBL_SERVICE = UUID
            .fromString("713D0000-503E-4C75-BA94-3148F18D941E");

    public static final UUID RBL_DEVICE_RX_UUID = UUID
            .fromString("713D0002-503E-4C75-BA94-3148F18D941E");

    public static final UUID RBL_DEVICE_TX_UUID = UUID
            .fromString("713D0003-503E-4C75-BA94-3148F18D941E");

    public static final UUID CCC = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final UUID SERIAL_NUMBER_STRING = UUID
            .fromString("00002A25-0000-1000-8000-00805f9b34fb");

    public static final String BART_MAC = "DC:A4:F0:F5:87:D5";

    private Handler mHandler = new Handler();
    private BluetoothGattCharacteristic txChar = null;
    private BluetoothGattCharacteristic rxChar = null;
    private boolean mConnected = false;
    private BluetoothGatt mGatt;
    private BluetoothScannerActivity scanner = new BluetoothScannerActivity();

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {

            if (gatt.getDevice().getAddress().equals(BART_MAC))
            {
                if (newState == BluetoothProfile.STATE_CONNECTED && !mConnected)
                {
                    mHandler.post(new ToastRunnable(MainActivity.activity.getString(R.string.bart_connected)));
                    mConnected = true;
                    mGatt.discoverServices();
                    //invalidation for redrawing the BT icon
                    MainActivity.activity.invalidateOptionsMenu();
                }else if (newState == BluetoothProfile.STATE_DISCONNECTED && mConnected) {
                    mHandler.post(new ToastRunnable(MainActivity.activity.getString(R.string.bart_disconnected)));
                    mConnected = false;
                    mGatt.close();
                    mGatt = null;
                    MainActivity.activity.invalidateOptionsMenu();

                    // if the user didn't simply turn wifi off and we are disconnected,
                    //  try connecting again.
                    tryConnect();
                }
            }

            // Log new connection state
            Log.i(TAG, "onConnectionStateChange for device: " + gatt.getDevice().getAddress() + " in state: "
                    + newState + " with status: " + status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            super.onServicesDiscovered(gatt, status);
            BluetoothGattService rblService = gatt.getService(RBL_SERVICE);

            if (rblService == null)
            {
                Log.e(TAG, "RBL service not found!");
                return;
            }

            txChar = rblService.getCharacteristic(RBL_DEVICE_TX_UUID);
            rxChar = rblService.getCharacteristic(RBL_DEVICE_RX_UUID);

            enableNotification(true, rxChar);
            Log.i(TAG, "Service discovered on device: " + gatt.getDevice().getAddress() + " with status: " + status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            if (gatt.getDevice().getAddress().equals(BART_MAC) && characteristic.getValue().length == 1)
                MainActivity.activity.drinkQueue.setStatus( (char) characteristic.getValue()[0]);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }
    };

    public boolean enableNotification(boolean enable,
                                      BluetoothGattCharacteristic characteristic) {
        if (mGatt == null) {
            return false;
        }
        if (!mGatt.setCharacteristicNotification(characteristic,
                enable)) {
            return false;
        }

        BluetoothGattDescriptor clientConfig = characteristic
                .getDescriptor(CCC);
        if (clientConfig == null) {
            return false;
        }

        if (enable) {
            Log.i(TAG, "enable notification");
            clientConfig
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            Log.i(TAG, "disable notification");
            clientConfig
                    .setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }

        return mGatt.writeDescriptor(clientConfig);
    }

    public void tryConnect()
    {
        if (scanner.checkBLEnabled())
            scanner.setupBT();
    }

    public void keepConnected()
    {
        if (mConnected)
        {
            char[] ping = {'P', 'I', 'N', 'G'};
            sendData(ping);
        }
    }

    public void sendData(char[] data)
    {
        this.setupBT();

        //send data
        txChar.setValue(new String(data));

        if(mGatt != null)
        {
            mGatt.writeCharacteristic(txChar);

            Log.i(TAG, "Data: " + new String(data) + " sent!");
        }
    }

    public boolean ismConnected()
    {
        return mConnected;
    }

    public void setupBT()
    {
        if (mGatt == null)
            scanner.setupBT();
    }

    @Override
    public void onDestroy()
    {
        if (mGatt == null)
            return;

        mGatt.close();
        mGatt = null;

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    private class ToastRunnable implements Runnable
    {
        String mText;

        public ToastRunnable(String text) {
            mText = text;
        }

        @Override
        public void run(){
            Toast.makeText(MainActivity.activity, mText, Toast.LENGTH_SHORT).show();
        }
    }

    private class BluetoothScannerActivity extends Activity
    {
        private final int REQUEST_ENABLE_BT = 1;
        private BluetoothAdapter mBluetoothAdapter;
        private static final long SCAN_PERIOD = 10000;
        private BluetoothLeScanner mBLEScanner;
        private BluetoothManager mBluetoothManager;

        public ScanCallback mScanCallback = new ScanCallback()
        {
            @Override
            public void onScanResult (int callbackType, final ScanResult result)
            {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // if BarT found on network
                        if (result.getDevice().getAddress().equals(BART_MAC))
                            mGatt = result.getDevice().connectGatt(MainActivity.activity, false, mGattCallback);
                    }
                });
            }
        };

        public boolean checkBLEnabled()
        {
            // Initializes Bluetooth adapter.
            if (mBluetoothManager == null)
                mBluetoothManager = (BluetoothManager) MainActivity.activity.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothAdapter == null)
                mBluetoothAdapter = mBluetoothManager.getAdapter();

            return mBluetoothAdapter.isEnabled();
        }

        private void setupBT()
        {
            // Checks that Bluetooth is enabled
            if (!checkBLEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                MainActivity.activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else
                bluetoothScan();

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if (requestCode == REQUEST_ENABLE_BT && resultCode != RESULT_CANCELED)
                bluetoothScan();
        }


        private void bluetoothScan()
        {
            mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();

            //scans for devices
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBLEScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            mBLEScanner.startScan(mScanCallback);
        }
    }

}
