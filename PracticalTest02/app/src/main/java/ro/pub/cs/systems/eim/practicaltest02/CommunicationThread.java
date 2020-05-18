package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;

//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CommunicationThread extends Thread {

    private Socket socket;
    ServerThread serverThread;

//    HashMap data = new HashMap<String, BitcoinInfo>();

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.socket = socket;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

//          String city = bufferedReader.readLine();
            String informationType = bufferedReader.readLine();

            HashMap<String, BitcoinInfo> data = serverThread.getData();
            BitcoinInfo bitcoinInfo = null;

            if (data.containsKey(informationType)) {
//                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                bitcoinInfo = data.get(informationType);
            }

            if (bitcoinInfo != null) {
                //send the result to the client
                switch (informationType) {
                    case "EUR":
                        String result = bitcoinInfo.valueEUR;
                        printWriter.println(result);
                        printWriter.flush();
                        break;
                    case "USD":
                        result = bitcoinInfo.valueUSD;
                        printWriter.println(result);
                        printWriter.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }
}
