package ir.indata.socket;

public class VolleyError {


    private String message;

    public VolleyError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
