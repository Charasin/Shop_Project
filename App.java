import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Основен клас, демонстриращ използването на системата
public class App {
    public static void main(String[] args) {
        Store store = new Store();
        Cashier john = new Cashier(1, "John Ivanov", 1200);
        store.addCashier(john);

        store.addProduct(new FoodProduct(1, "Milk", 1.2, LocalDate.now().plusDays(3), 100));
        store.addProduct(new NonFoodProduct(2, "Television", 300, LocalDate.now().plusYears(2), 10));

        Map<Integer, Integer> basket = new HashMap<>();
        basket.put(1, 2);
        basket.put(2, 1);

        try {
            Receipt r = store.sellProducts(basket, john);
            System.out.println("Purchase successful. Total amount: " + r.getTotal() + "BGN");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Turnover: " + store.getTotalIncome() + "BGN");
        System.out.println("Expenses: " + store.getTotalExpenses() + "BGN");
        System.out.println("Profit: " + store.getProfit() + "BGN");
    }
}
