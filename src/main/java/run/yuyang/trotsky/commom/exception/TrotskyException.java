package run.yuyang.trotsky.commom.exception;

public class TrotskyException extends Exception {

    public TrotskyException(String message) {
        super("Trotsky is quit ." + message);
    }

}
