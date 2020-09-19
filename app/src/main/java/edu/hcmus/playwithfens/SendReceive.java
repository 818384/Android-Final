package edu.hcmus.playwithfens;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SendReceive extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    static final int MESSAGE_READ = 1;
    private Handler handler;
    private boolean keepSend = false;

    public SendReceive(Socket skt) {
        socket = skt;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public SendReceive(Socket skt, Handler handler) {
        socket = skt;
        this.handler = handler;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null) {
            try {
                bytes = inputStream.read(buffer);
                //System.out.println("Day la byte: " + bytes);
                if (bytes > 0 && keepSend) {
                    handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    keepSend = false;
                }
                else if (bytes <= 0){
                    keepSend = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(final byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    System.out.println("Do dai byte: " + new String(bytes, StandardCharsets.UTF_8));
//                    String checkLength = new String(bytes, StandardCharsets.UTF_8);
                    if (keepSend == false){
                        outputStream.write(bytes);
                        keepSend = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
