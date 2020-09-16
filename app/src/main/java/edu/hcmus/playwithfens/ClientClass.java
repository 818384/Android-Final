package edu.hcmus.playwithfens;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientClass extends Thread{
    private Socket socket;
    private String hostAdd;
    private SendReceive sendReceive;

    public ClientClass(InetAddress hostAddress) {
        this.hostAdd = hostAddress.getHostAddress();
        this.socket = new Socket();
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
            socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
            sendReceive = new SendReceive(socket);
            sendReceive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
