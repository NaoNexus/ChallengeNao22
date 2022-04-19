package com.example.naocontroller.socket;

import android.os.AsyncTask;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends AsyncTask<String, Void, Void> {
    private final static String TAG = MessageSender.class.getSimpleName();

    @Override
    protected Void doInBackground(String[] strings) {
        String ip = strings[1];
        int port = Integer.parseInt(strings[2]);

        try {
            Socket socket = new Socket(ip, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.print(strings[0]);
            writer.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
