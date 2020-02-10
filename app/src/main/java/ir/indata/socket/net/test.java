package ir.indata.socket.net;

import ir.indata.socket.DeliveryError;
import ir.indata.socket.Pusher;

public class test extends Pusher {


    @Override
    protected void doInBackground(String... params) {

    }


    @Override
    protected void onError(DeliveryError err) {
        super.onError(err);
    }
}
