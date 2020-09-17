package edu.hcmus.playwithfens;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Handler;

public class ServerClass extends Thread {
    private Socket socket;
    private ServerSocket serverSocket;
    private SendReceive sendReceive;
    private boolean isConnectedToClient = false;
    private android.os.Handler handler;

    public void CloseSocket() {
        try {
            if (socket.isConnected())
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerClass(android.os.Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            sendReceive = new SendReceive(socket, handler);
            sendReceive.start();
            if (socket.isConnected()){
                isConnectedToClient = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendReceive getSendReceive(){
        return sendReceive;
    }

    public boolean getIsConnectedToClient(){
        return isConnectedToClient;
    }

    public void setIsConnectedToClient(boolean value){
        this.isConnectedToClient = value;
    }
}
