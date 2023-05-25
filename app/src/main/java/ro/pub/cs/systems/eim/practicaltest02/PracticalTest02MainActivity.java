package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText setData = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private getInfoButtonClickListener getInfoButtonClickListener = new getInfoButtonClickListener();
    private class getInfoButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            //String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            int action_type = 0;
            switch (view.getId()) {
                case R.id.set_button:
                    action_type = Constants.SET_ACTION;
                    break;
                case R.id.reset_button:
                    action_type = Constants.RESET_ACTION;
                    break;
                case R.id.poll_button:
                    action_type = Constants.POLL_ACTION;
            }

            EditText setHourData = (EditText)findViewById(R.id.set_hour_edit_text);
            int hour = Integer.parseInt(setHourData.getText().toString());

            EditText setMinData = (EditText)findViewById(R.id.set_minute_edit_text);
            int minute = Integer.parseInt(setMinData.getText().toString());

            EditText clientId = (EditText)findViewById(R.id.client_id_edit_text);
            String id = clientId.getText().toString();

            Information info = new Information(action_type, hour, minute);

            clientThread = new ClientThread(
                    "127.0.0.1", Integer.parseInt(clientPort), info, id
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        Button setButton = (Button)findViewById(R.id.set_button);
        setButton.setOnClickListener(getInfoButtonClickListener);

        Button resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(getInfoButtonClickListener);

        Button pollButton = (Button)findViewById(R.id.poll_button);
        pollButton.setOnClickListener(getInfoButtonClickListener);

        //clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
