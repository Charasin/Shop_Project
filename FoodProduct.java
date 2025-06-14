import java.time.LocalDate;

// Представлява хранителен продукт
public class FoodProduct extends Product {
    public FoodProduct(int id, String name, double price, LocalDate date, int qty) {
        super(id, name, price, ProductCategory.FOOD, date, qty);
    }

    // Изчислява продажната цена с отстъпка при наближаващ срок
    public double getSellingPrice(int d, double disc, double m) {
        double p = deliveryPrice * (1 + m);
        if (isExpiringSoon(d)) {
            p = p * (1 - disc);
        }
        return p;
    }
}
