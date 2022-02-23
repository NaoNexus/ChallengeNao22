package com.example.naocontroller;

import android.os.AsyncTask;
import java.io.PrintWriter;
import java.net.Socket;

public class Message_sender extends AsyncTask<String, Void, Void> {


    @Override
    protected Void doInBackground(String[] strings) {

        try {

            Socket socket = new Socket("192.168.0.00", 5050);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(strings[0]);

            writer.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
