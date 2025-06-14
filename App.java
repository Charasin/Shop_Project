import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Основен клас, демонстриращ използването на системата
public class App {
    public static void main(String[] args) {
        Store store = new Store();

        Cashier john = new Cashier(1, "Йоан Иванов", 1200);
        store.addCashier(john);

        store.addProduct(new FoodProduct(1, "Мляко", 1.2, LocalDate.now().plusDays(3), 100));
        store.addProduct(new NonFoodProduct(2, "Телевизор", 300, LocalDate.now().plusYears(2), 10));

        Map<Integer, Integer> basket = new HashMap<>();
        basket.put(1, 2);
        basket.put(2, 1);

        try {
            Receipt r = store.sellProducts(basket, john);
            System.out.println("Покупката е успешна. Обща сума: " + r.getTotal() + " лв");
        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }

        System.out.println("Оборот: " + store.getTotalIncome() + " лв");
        System.out.println("Разходи: " + store.getTotalExpenses() + " лв");
        System.out.println("Печалба: " + store.getProfit() + " лв");
    }
}
