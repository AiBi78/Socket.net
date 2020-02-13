package ir.indata.socket.net;

import android.util.Log;

import ir.indata.socket.VolleyError;

/**
 * Created by Ali Behrozi on 09/02/2020
 * Send / Receive messaging platform
 * send messaging easy and fast all over the net

 * Communicate with server with open connection
 * connection will be open and messages will send and receive on that
 *
 * @param <Input>  Accepted / Expected input type
 * @param <Output> Accepted / Expected output type
 */
public abstract class Pusher<Input, Output> {

    /**
     * Create connection to the server for communication
     *
     * @param serverUrl  Server Address
     * @param serverPort Server listening port
     */
    protected synchronized void ceateConnection(String serverUrl, int serverPort) {


        onConnectionCreated(serverUrl, serverPort);
    }

    /**
     * Accord when connection created
     *
     * @param serverUrl  Server Address
     * @param serverPort Server listening port
     */
    protected void onConnectionCreated(String serverUrl, int serverPort) {
        Log.i("Socket.net Info",String.format("Connection to %s:%d created",serverUrl.toString(), serverPort));
    }


    /**
     * Disconnect from the server
     */
    protected synchronized void closeConnection() {

    }

    /**
     * Accord when connection closed
     * Notify communication is interrupted
     */
    protected void onConnectionClosed() {}

    /**
     * <p>Restart connection to the server .
     * try to disconnect and connect again .</p>
     */
    protected void restartConnection() {}

    /**
     * Accord when connection restart
     */
    protected void onConnectionRestart() {}

    protected abstract void doInBackground(String... params);








    /**
     * <p>Called if your send message receive by receiver and
     * read it.</p>
     *
     * <p>This method won't be invoked if receiver don't call {@link #imReadMessage()}</p>
     * @param message The message that read

    protected void onMessageReaded(Output message) { }
     */


    //protected void onMessageDeliverd(Input message) { }

    /**
     * <p>invoked if new message receive form sever .</p>
     * <p><b>Working on new thread</b></p>
     *
     * @param message The input message
     */
    protected void onMessageReceived(Output message) {
        Log.i("Socket.net InputMessage", message.toString());
    }


    /**
     * <p>After creating connection with calling this method you can send your message to him </p>
     *
     * @param message The message to send .
     */
    public final void sendMessaeg(Output message)
    {

    }

    public final void imReadMessage() {}


















    /**
     * Shows errors when happened
     * @param err The error
     */
    protected void onError(VolleyError err) {
        // print error message
        Log.e("Message Delivery Error " ,err.toString());
    }


    /**
     * Indicates the current status of the connection. Each status will be set only once
     * during the lifetime of a task.
     */
    public enum Status {
        /**
         * Indicates that the connection has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the connection is on.
         */
        RUNNING,
        /**
         * Indicates that connection has closed.
         */
        FINISHED,
    }

}



