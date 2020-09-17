package edu.hcmus.playwithfens;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Handler;

public class ClientClass extends Thread{
    private Socket socket;
    private String hostAdd;
    private SendReceive sendReceive;
    private android.os.Handler handler;

    public ClientClass(InetAddress hostAddress) {
        this.hostAdd = hostAddress.getHostAddress();
        this.socket = new Socket();
    }

    public ClientClass(InetAddress hostAddress, android.os.Handler handler) {
        this.hostAdd = hostAddress.getHostAddress();
        this.socket = new Socket();
        this.handler = handler;
    }

    public void CloseSocket() {
        try {
            if (socket.isConnected())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(hostAdd, 8888), 2000);
            sendReceive = new SendReceive(socket, handler);
            sendReceive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendReceive getSendReceive(){
        return sendReceive;
    }
}
