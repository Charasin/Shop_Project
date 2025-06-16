// Exception for insufficient quantity of product
public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String name, int req, int avail) {
        super("Insufficient quantity of " + name + ": requested " + req + ", available " + avail);
    }
}
