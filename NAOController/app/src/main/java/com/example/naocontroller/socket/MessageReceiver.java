package com.example.naocontroller.socket;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiver extends AsyncTask<String, Void, String> {
    private final static String TAG = MessageReceiver.class.getSimpleName();

    String messageReceived = "";

    public interface AsyncResponse {
        void processFinish(String messageReceived);
    }

    public AsyncResponse response;

    public MessageReceiver(AsyncResponse response) {
        this.response = response;
    }

    @Override
    protected String doInBackground(String[] strings) {
        int port = Integer.parseInt(strings[0]);

        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Waiting for client");
            Socket s = ss.accept();
            System.out.println("Connected");
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while (!this.messageReceived.contains("stop")) {
                String line = reader.readLine();

                if (line != null) {
                    this.messageReceived = line;
                }
            }

            s.close();
            ss.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageReceived;
    }

    @Override
    protected void onPostExecute(String messageReceived) {
        response.processFinish(messageReceived);
    }
}
