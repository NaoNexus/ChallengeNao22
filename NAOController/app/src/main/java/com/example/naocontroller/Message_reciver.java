package com.example.naocontroller;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Message_reciver extends AsyncTask <String, Void, String> {

    final int SERVER_PORT = 6000;
    String message_received = "";

    public interface AsyncResponse {
        void processFinish(String message_received);
    }

    public AsyncResponse response;

    public Message_reciver(AsyncResponse response) {
        this.response = response;
    }

    @Override
    protected String doInBackground(String[] strings) {
        try {
            //CLIENT MUST SEND DATA TO PORT 5050\\
            //PORT 6000 CANNOT BE CHANGED\\
            //BEFORE RUNNING DO PORT-FORWARDING AND REDIRECT TRAFFIC FROM 5050 TO 6000 (with Telnet)\\
            ServerSocket ss = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for client");
            Socket s = ss.accept();
            System.out.println("Connected");

            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.message_received = reader.readLine();

            s.close();
            ss.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message_received;
    }

    @Override
    protected void onPostExecute(String message_received) {
        response.processFinish(message_received);
    }
}

