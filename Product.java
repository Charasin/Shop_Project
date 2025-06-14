import java.time.LocalDate;

// Абстрактен клас, който описва общите свойства на стоките
public abstract class Product {
    int id;
    int quantity;
    String name;
    double deliveryPrice;
    ProductCategory category;
    LocalDate expirationDate;

    // Конструктор за инициализиране на стойности
    public Product(int id, String name, double price, ProductCategory cat, LocalDate date, int qty) {
        this.id = id;
        this.name = name;
        this.deliveryPrice = price;
        this.category = cat;
        this.expirationDate = date;
        this.quantity = qty;
    }

    // Проверява дали продуктът е с изтекъл срок
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    // Проверява дали срокът наближава (в рамките на определен брой дни)
    public boolean isExpiringSoon(int days) {
        return LocalDate.now().plusDays(days).isAfter(expirationDate);
    }

    // Абстрактен метод, който връща продажната цена
    public abstract double getSellingPrice(int days, double discount, double markup);

    // Намалява количеството след продажба
    public void reduceQuantity(int q) {
        quantity -= q;
    }

    // Връща текущото количество
    public int getQuantity() {
        return quantity;
    }

    // Връща името на продукта
    public String getName() {
        return name;
    }
}
