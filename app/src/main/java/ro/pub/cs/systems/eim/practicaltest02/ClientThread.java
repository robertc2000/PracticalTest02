package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private Information info;
    String client_id;

    private Socket socket;

    public ClientThread(String address, int port, Information information, String client_id) {
        this.address = address;
        this.port = port;
        this.info = information;
        this.client_id = client_id;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(client_id);
            printWriter.flush();
            switch(info.action_type) {
                case Constants.SET_ACTION:
                    printWriter.println("set," + Integer.toString(info.hour) + "," + Integer.toString(info.minute));
                    printWriter.flush();
                    break;
                case Constants.POLL_ACTION:
                    printWriter.println("poll");
                    printWriter.flush();
                    break;
                case Constants.RESET_ACTION:
                    printWriter.println("reset");
                    printWriter.flush();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}

