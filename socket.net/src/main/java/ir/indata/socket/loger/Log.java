package ir.indata.socket.loger;

/**
 * Save Logs
 */
public class Log {

    private static String log = "";

    public static void add(String s) {
        android.util.Log.e("SocketHelper :", s);
        log += "\n" + s;
    }

    public static String get() {
        return log;
    }
}