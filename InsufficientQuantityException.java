// Изключение при недостатъчно количество от продукт
public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String name, int req, int avail) {
        super("Недостатъчно количество от " + name + ": заявено " + req + ", налични " + avail);
    }
}
