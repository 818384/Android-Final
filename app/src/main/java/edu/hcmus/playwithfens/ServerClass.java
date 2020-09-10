package edu.hcmus.playwithfens;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClass extends Thread {
    private Socket socket;
    private ServerSocket serverSocket;

    public void CloseSocket() {
        try {
            if (socket.isConnected())
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            //sendReceive = new SendReceive(socket);
            //sendReceive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
