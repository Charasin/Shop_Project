// Ред от касова бележка: продукт, количество, обща цена
public class ReceiptItem {
    Product product;
    int quantity;
    double totalPrice;

    public ReceiptItem(Product p, int q, double unitPrice) {
        product = p;
        quantity = q;
        totalPrice = unitPrice * q;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " - " + String.format("%.2f", totalPrice) + " лв";
    }
}
