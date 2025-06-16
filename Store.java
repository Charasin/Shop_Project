import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Main class that describes the store
public class Store {
    List<Product> inventory = new ArrayList<>();
    List<Cashier> cashiers = new ArrayList<>();
    List<Receipt> receipts = new ArrayList<>();
    int receiptCounter = 1;

    public double foodMarkup = 0.3;
    public double nonFoodMarkup = 0.5;
    public double foodDiscountPercent = 0.2;
    public int foodDiscountDays = 5;

    public void addCashier(Cashier c) {
        cashiers.add(c);
    }

    public void addProduct(Product p) {
        inventory.add(p);
    }

    public Receipt sellProducts(Map<Integer, Integer> basket, Cashier cashier) throws InsufficientQuantityException {
        List<ReceiptItem> items = new ArrayList<>();

        for (Map.Entry<Integer, Integer> e : basket.entrySet()) {
            Product p = null;
            for (Product prod : inventory) {
                if (prod.id == e.getKey()) {
                    p = prod;
                    break;
                }
            }
            if (p == null || p.isExpired()) {
                continue;
            }
            if (p.getQuantity() < e.getValue()) {
                throw new InsufficientQuantityException(p.getName(), e.getValue(), p.getQuantity());
            }
            double m = (p.category == ProductCategory.FOOD) ? foodMarkup : nonFoodMarkup;
            double d = (p.category == ProductCategory.FOOD) ? foodDiscountPercent : 0.0;
            double price = p.getSellingPrice(foodDiscountDays, d, m);
            p.reduceQuantity(e.getValue());
            items.add(new ReceiptItem(p, e.getValue(), price));
        }

        Receipt r = new Receipt(receiptCounter++, cashier, items);
        receipts.add(r);
        try {
            r.printAndSave();
        } catch (IOException ex) {
            System.out.println("Error while saving: " + ex.getMessage());
        }
        return r;
    }

    public double getTotalIncome() {
        double total = 0;
        for (Receipt r : receipts) {
            total += r.getTotal();
        }
        return total;
    }

    public double getTotalExpenses() {
        double sal = 0;
        for (Cashier c : cashiers) {
            sal += c.getSalary();
        }
        double cost = 0;
        for (Product p : inventory) {
            cost += p.deliveryPrice * p.getQuantity();
        }
        return sal + cost;
    }

    public double getProfit() {
        return getTotalIncome() - getTotalExpenses();
    }

    public int getNumberOfReceipts() {
        return receipts.size();
    }
}
