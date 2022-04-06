package com.example.naocontroller;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Message_receiver extends AsyncTask <String, Void, String> {
    final int SERVER_PORT = 5050;
    String message_received = "";

    public interface AsyncResponse {
        void processFinish(String message_received);
    }

    public AsyncResponse response;

    public Message_receiver(AsyncResponse response) {
        this.response = response;
    }

    @Override
    protected String doInBackground(String[] strings) {
        try {
            ServerSocket ss = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for client");
            Socket s = ss.accept();
            System.out.println("Connected");

            while (!this.message_received.equals("stop")) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                this.message_received = reader.readLine();
                s.close();
                ss.close();
                reader.close();
            }

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

