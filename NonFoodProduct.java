import java.time.LocalDate;

// Представлява нехранителен продукт
public class NonFoodProduct extends Product {
    public NonFoodProduct(int id, String name, double price, LocalDate date, int qty) {
        super(id, name, price, ProductCategory.NON_FOOD, date, qty);
    }

    // Няма отстъпка при наближаващ срок
    public double getSellingPrice(int d, double disc, double m) {
        return deliveryPrice * (1 + m);
    }
}
