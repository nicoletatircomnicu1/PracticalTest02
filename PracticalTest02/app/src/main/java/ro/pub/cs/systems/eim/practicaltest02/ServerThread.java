package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    HashMap data = new HashMap<String, BitcoinInfo>();

    private int port;

    Runnable taskRunnable = new Runnable() {
        public void run() {
            Log.v("ceva", "ceva");

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/"
                        + "EUR" + ".json");

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String EURPage = httpClient.execute(httpGet, responseHandler);
                httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/"
                        + "USD" + ".json");
                responseHandler = new BasicResponseHandler();
                String USDPage = httpClient.execute(httpGet, responseHandler);
                Log.v("ceva", USDPage);
                Log.v("ceva", EURPage);

                JSONObject pageContentusd = new JSONObject(USDPage);
                String updated = pageContentusd.getJSONObject("time").getString("updated");
                String USDvalue = pageContentusd.getJSONObject("bpi").getJSONObject("USD").getString("rate_float");

                JSONObject pageContentEUR = new JSONObject(EURPage);
                String EURvalue = pageContentEUR.getJSONObject("bpi").getJSONObject("EUR").getString("rate_float");

                BitcoinInfo info = new BitcoinInfo(updated, USDvalue, EURvalue);
                setData("EUR", info);
                setData("USD", info);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public ServerThread(EditText port) {
        this.port = Integer.parseInt(port.getText().toString());
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v("p2", "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e("p2", "An exception has occurred: " + ioException.getMessage());

        }
        Log.v("p2", "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(this, socket);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e("p2", "An exception has occurred: " + ioException.getMessage());

        }
    }

    public synchronized void setData(String city, BitcoinInfo bitcoinInfo) {
        this.data.put(city, bitcoinInfo);
    }

    public synchronized HashMap<String, BitcoinInfo> getData() {
        return data;
    }
}

