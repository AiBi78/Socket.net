package ir.indata.socket.net;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ir.indata.socket.VolleyError;


class Pusher1<Input, Output> extends Thread {

    private static Socket client;
    private static OutputStream dataOut;
    private static InputStreamReader dataIn;
    private static BufferedReader input;
    private static PrintWriter output;
    Pusher<Input, Output> listener;
    private ArrayList<Input> toSend = new ArrayList<>();
    private String receiverAddress;
    private int receiverPort;


    public Pusher1(String receiverAddress, int receiverPort, Pusher<Input, Output> listener) {
        this.receiverAddress = receiverAddress;
        this.receiverPort = receiverPort;
        this.listener = listener;

        try {

            // Create connection to the receiver
            createConnection();

            listenResponse();

        } catch (IOException e) {
            listener.onError(new VolleyError("Connection Failed 2 " + e.getMessage() + "  \n " + e.getLocalizedMessage()));
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {
            listener.onError(new VolleyError("Connection Failed 1 " + e.getMessage() + "  \n " + e.getLocalizedMessage()));
            e.printStackTrace();
        }

    }

    /**
     * Start Socket and connect to the receiver address
     *
     * @throws IOException
     */
    private void createConnection() throws IOException {
        // Create connection
        client = new Socket(receiverAddress, receiverPort);

        //create object streams with the server
        dataIn = new InputStreamReader(client.getInputStream());
        dataOut = client.getOutputStream();

        output = new PrintWriter(dataOut);
        input = new BufferedReader(dataIn);
    }

    /**
     * add item to list for send
     *
     * @param message item for send
     */
    public void addToSendList(Input message) {
        toSend.add(message);
    }

    @Override
    public void run() {
        super.run();

        for (; ; ) {
            if (toSend.isEmpty())
                continue;

            try {

                // Send data to the server
                sendData(toSend.get(0));


            } catch (IOException e) {
                listener.onError(new VolleyError("Connection Failed 4 " + e.getMessage() + "  \n " + e.getLocalizedMessage()));
                e.printStackTrace();
                //removeConnection();
                return;
            }
        }
    }


    /**
     * Send the data
     *
     * @param message
     * @throws IOException
     */
    private void sendData(Input message) throws IOException, NullPointerException {

        if (client != null) {

            if (client.isConnected()) {
                if (message != null) {
                    dataOut.write((message.toString() + "\r\n").getBytes());
                    dataOut.flush();
                    toSend.remove(0);
                }

            } else {
                listener.onError(new VolleyError("Connection Failed server disconnection" ));
                // Removing connection
                //removeConnection();
                listener.onError(new VolleyError("Connection Failed"));
                System.gc();
            }
        }
    }

    /**
     * Listen for a response form the server
     */
    private void listenResponse() {

        new Thread() {
            @Override
            public void run() {

                byte[] buffer = new byte[1024];

                StringBuilder stringBuilder = new StringBuilder();

                try {

                    if (client != null) {
                        // if(socket.isConnected()) {
                        while (client.isConnected()) {
                            String message = input.readLine();

                            int readSize = message.length();

                            Log.d("TAG", "readSize:" + readSize);

                            //If Server is stopping
                            if (readSize == -1) {

                                dataIn.close();
                                dataOut.close();
                                client.close();
                                createConnection();
                            }
                            if (readSize == 0) continue;

                            //Update the receive editText
                            if (readSize > 10) {

                                int index = message.indexOf("}{");

                                //MULTIPLE MESSAGES DETECTED IN BUFFER
                                if (index > 0) {

                                    String[] arrayMsg = message.split("\\}\\{");
                                    for (int i = 0; i < arrayMsg.length; i++) {

                                        if (i == 0) {
                                            arrayMsg[i] = arrayMsg[i] + "}";
                                        } else {
                                            arrayMsg[i] = "{" + arrayMsg[i];
                                        }

                                        listener.onMessageReceived((Output) arrayMsg[i]);
                                    }
                                }
                                else {
                                    listener.onMessageReceived((Output) message);
                                }
                            }
                            //
                        }


                    }


                } catch (UnknownHostException e) {
                    Log.e("ERROR R", e.getMessage());
                    listener.onError(new VolleyError("Connection Failed  11 "+ e.getMessage() +"  \n "+ e.getLocalizedMessage()));
                } catch (IOException e) {
                    Log.e("ERROR R", e.getMessage());
                    listener.onError(new VolleyError("Connection Failed  22 "+ e.getMessage() +"  \n "+ e.getLocalizedMessage()));

                } catch (NullPointerException e) {
                    Log.e("ERROR R", e.getMessage());
                    listener.onError(new VolleyError("Connection Failed 33 "+ e.getMessage() +"  \n "+ e.getLocalizedMessage()));
                }

            }
        }.start();

    }


    public void removeConnection() {

        try {
            Log.e("removeing ", "Connection");

            if (client != null)
            {
                client.shutdownOutput();
                client.shutdownInput();
                client.close();

                client = null;

                dataIn.close();
                dataOut.close();
                input.close();
                output.close();

                Log.e("removing", "done");
            }

        } catch (IOException e) {
            e.printStackTrace();
            //if (client.isConnected() || !client.isOutputShutdown() || !client.isInputShutdown())
               // removeConnection();
        }
    }


    public boolean isConnected() {
        if (client == null)
            return false;

        return client.isConnected();
    }
}
