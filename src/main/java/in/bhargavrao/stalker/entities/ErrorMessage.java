package in.bhargavrao.stalker.entities;


public class ErrorMessage extends Message {

    String error;

    public ErrorMessage(String error) {
        this.setMessage("error");
        this.error = error;
    }
}
