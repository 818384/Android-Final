package edu.hcmus.playwithfens;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
    private GameView gameView;
    private boolean checkSend = false;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, MainActivity mActivity, GameView gameView) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mActivity = mActivity;
        this.gameView = gameView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {

                if (ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "okie", Toast.LENGTH_SHORT).show();
                    checkSend = true;
                    mManager.requestPeers(mChannel, gameView.peerListListener);
                } else {
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
                    ActivityCompat.requestPermissions((Activity) mActivity.getApplicationContext(), new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE}, 1);
                    mManager.requestPeers(mChannel, gameView.peerListListener);
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                mManager.requestConnectionInfo(mChannel, gameView.connectionInfoListener);
            } else {
                //mActivity.getConnectionStatus().setText("Device Disconnected");
                Toast.makeText(context, "Device Disconnected", Toast.LENGTH_SHORT).show();
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (gameView.getGameState() == 2 && gameView.getStepGame() == 2 && wifiManager.isWifiEnabled()){
                    if (!gameView.isWiner() && !gameView.isLoser()){
                        gameView.setWiner(true);
                        gameView.setLoser(false);
                    }
                }
                else if (gameView.getGameState() == 2 && gameView.getStepGame() == 2 && !wifiManager.isWifiEnabled()){
                    if (!gameView.isWiner() && !gameView.isLoser()){
                        gameView.setWiner(false);
                        gameView.setLoser(true);
                    }
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // do something.
            Toast.makeText(context, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION" + action, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isCheckSend() {
        return checkSend;
    }
}
