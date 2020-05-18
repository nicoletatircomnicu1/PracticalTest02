package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText serverPort;
    private TextView resultTextView;
    private Button startServer, connectServerClient;
    private Spinner methodSpin;
    private ServerThread serverThread;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.srv_button:

                    serverThread = new ServerThread(serverPort);
                    serverThread.startServer();

                    break;
                case R.id.client_button:
                    String operation = methodSpin.getSelectedItem().toString(); //taking text
                    ClientAsyncTask clientAsyncTask = new ClientAsyncTask(resultTextView);
                    clientAsyncTask.execute(methodSpin.getSelectedItem().toString());
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practic_test02_main);

        serverPort = (EditText)findViewById(R.id.srv_port_edit_text);
        startServer = (Button)findViewById(R.id.srv_button);
        startServer.setOnClickListener(buttonClickListener);

        connectServerClient = (Button)findViewById(R.id.client_button);
        connectServerClient.setOnClickListener(buttonClickListener);

        resultTextView = (TextView)findViewById(R.id.result_text_view);
        methodSpin = (Spinner)findViewById(R.id.operation_spinner);
    }
}
