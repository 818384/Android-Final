package edu.hcmus.playwithfens;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button btnOnOff;
    private Button btnDiscovery;
    private Button btnSend;
    private ListView listView;
    private TextView read_msg_box;
    private TextView connectionStatus;
    private EditText writeMsg;
    private ImageView imgShip;

    private WifiManager wifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;
    private MainActivity mainActivity = this;

    static final int MESSAGE_READ = 1;

    private ServerClass serverClass;
    private ClientClass clientClass;
    private SendReceive sendReceive;

    private GameView gameView;
    private boolean checkGamePlay = false;
    private GameLoopThread gameLoopThread;

    TextView getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //initialWork();
        //exqListener();
        //if(checkGamePlay){
            gameView = new GameView(this, this);
            setContentView(gameView);
        //}


    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        //gameView.pauseMusicPlayer();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //gameView.startMusicPlayer();
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //gameView.stopMusicPlayer();
//        //gameView.releaseMusicPlayer();
//    }


//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case MESSAGE_READ:
//                    byte[] readBuff = (byte[]) msg.obj;
//                    String tempMsg = new String(readBuff, 0, msg.arg1);
//                    read_msg_box.setText(tempMsg);
//                    //gameView.setxMsg(Float.valueOf(tempMsg));
//                    break;
//            }
//            return true;
//        }
//    });

//    private void exqListener() {
//        btnOnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (serverClass.getClass().isInstance(new ServerClass()))
////                    serverClass.CloseSocket();
//                //clientClass.CloseSocket();
//                btnSend.callOnClick();
//            }
//        });
//        btnDiscovery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                    mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//                        @Override
//                        public void onSuccess() {
//                            connectionStatus.setText("Discovery Started");
//                        }
//
//                        @Override
//                        public void onFailure(int i) {
//                            connectionStatus.setText("Discovery Starting Failed");
//                        }
//                    });
//                }
//            }
//        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final WifiP2pDevice device = deviceArray[position];
//                WifiP2pConfig config = new WifiP2pConfig();
//                config.deviceAddress = device.deviceAddress;
//
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE}, 1);
//                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
//
//
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                }
//                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
//                        checkGamePlay = true;
////                        if(checkGamePlay){
////                            gameView = new GameView(getApplicationContext(), MainActivity.this);
////                            setContentView(gameView);
////                            btnSend.callOnClick();
////                            System.out.println("Qua man choi");
////
////                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(int reason) {
//                        Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = writeMsg.getText().toString();
//                byte[] ms = msg.getBytes();
//                sendReceive.write(ms);
//            }
//        });
//    }

//    private void initialWork() {
//        btnOnOff = (Button) findViewById(R.id.onOff);
//        btnDiscovery = (Button) findViewById(R.id.discover);
//        btnSend = (Button) findViewById(R.id.sendButton);
//        listView = (ListView) findViewById(R.id.peerListView);
//        read_msg_box = (TextView) findViewById(R.id.readMsg);
//        connectionStatus = (TextView) findViewById(R.id.connectionStatus);
//        writeMsg = (EditText) findViewById(R.id.writeMsg);
//        writeMsg.setText("asdasd");
//        imgShip = (ImageView) findViewById(R.id.imgShip);
//        imgShip.setX(500);
//        imgShip.setY(500);
//
//
//        btnOnOff.setEnabled(false);
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//
//        //mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//    }

//    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
//        @Override
//        public void onPeersAvailable(WifiP2pDeviceList peerList) {
//            System.out.println("Vào hàm ngoai");
//            if (!peerList.getDeviceList().equals(peers)) {
//                peers.clear();
//                peers.addAll(peerList.getDeviceList());
//                deviceNameArray = new String[peerList.getDeviceList().size()];
//                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
//                int index = 0;
//
//                for (WifiP2pDevice device : peerList.getDeviceList()) {
//                    deviceNameArray[index] = device.deviceName;
//                    deviceArray[index] = device;
//                    index++;
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                        android.R.layout.simple_list_item_1, deviceNameArray);
//                //listView.setAdapter(adapter);
//            }
//
//            if (peers.size() == 0) {
//                Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT);
//                return;
//            }
//        }
//    };

//    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener()
//    {
//        @Override
//        public void onConnectionInfoAvailable(WifiP2pInfo info) {
//            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
//
//            if (info.groupFormed && info.isGroupOwner) {
//                connectionStatus.setText("Host");
//                serverClass = new ServerClass();
//                serverClass.start();
//                btnOnOff.setEnabled(true);
//
//            } else if (info.groupFormed) {
//                connectionStatus.setText("Client");
//                clientClass = new ClientClass(groupOwnerAddress);
//                clientClass.start();
//                btnOnOff.setEnabled(true);
//            }
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        //mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        //registerReceiver(mReceiver, mIntentFilter);
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mReceiver);
        gameView.pause();
    }

//    public class ServerClass extends Thread {
//        private Socket socket;
//        private ServerSocket serverSocket;
//
//        public void CloseSocket() {
//            try {
//                if (socket.isConnected())
//                    serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                serverSocket = new ServerSocket(8888);
//                socket = serverSocket.accept();
//                sendReceive = new SendReceive(socket);
//                sendReceive.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    private class SendReceive extends Thread
//    {
//        private Socket socket;
//        private InputStream inputStream;
//        private OutputStream outputStream;
//
//        public SendReceive(Socket skt) {
//            socket = skt;
//            try {
//                inputStream = socket.getInputStream();
//                outputStream = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (socket != null) {
//                try {
//                    bytes = inputStream.read(buffer);
//                    if (bytes > 0) {
//                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void write(final byte[] bytes) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        byte[] a = "1".getBytes();
//                        System.out.println(a);
//                        outputStream.write(a);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }

//    public class ClientClass extends Thread
//    {
//        private Socket socket;
//        private String hostAdd;
//
//        public ClientClass(InetAddress hostAddress) {
//            hostAdd = hostAddress.getHostAddress();
//            socket = new Socket();
//        }
//
//        public void CloseSocket() {
//            try {
//                if (socket.isConnected())
//                    socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
//                sendReceive = new SendReceive(socket);
//                sendReceive.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}