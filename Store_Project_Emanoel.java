// Университетски проект по Java – Моделиране на магазин

// === ProductCategory.java ===
// Категории на стоки: Хранителни и Нехранителни
public enum ProductCategory { FOOD, NON_FOOD }

// === Product.java ===
// Абстрактен клас, който описва общите свойства на стоките
import java.time.LocalDate;

public abstract class Product {
    int id, quantity; // Идентификатор и количество на продукта
    String name; // Име на продукта
    double deliveryPrice; // Доставна цена
    ProductCategory category; // Категория
    LocalDate expirationDate; // Срок на годност

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

// === FoodProduct.java ===
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

// === NonFoodProduct.java ===
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

// === Cashier.java ===
// Клас, представящ касиер в магазина
public class Cashier {
    int id; // ИД на касиера
    String name; // Име
    double salary; // Заплата

    public Cashier(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() { return salary; }
    public String getName() { return name; }
}

// === ReceiptItem.java ===
// Ред от касова бележка: продукт, количество, обща цена
public class ReceiptItem {
    Product product;
    int quantity;
    double totalPrice;

    public ReceiptItem(Product p, int q, double u) {
        product = p;
        quantity = q;
        totalPrice = u * q;
    }

    // Представя реда като текст
    public String toString() {
        return product.getName() + " x" + quantity + " - " + String.format("%.2f", totalPrice) + " лв";
    }
}

// === Receipt.java ===
// Клас за касова бележка
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt implements Serializable {
    int number; // Номер на бележката
    Cashier cashier; // Касиер, който е извършил продажбата
    LocalDateTime dateTime; // Дата и час
    List<ReceiptItem> items; // Продукти
    double total; // Обща стойност

    public Receipt(int n, Cashier c, List<ReceiptItem> i) {
        number = n;
        cashier = c;
        dateTime = LocalDateTime.now();
        items = i;
        total = 0;
        for (ReceiptItem it : i) {
            total += it.totalPrice;
        }
    }

    // Записва бележката във файл
    public void printAndSave() throws IOException {
        PrintWriter w = new PrintWriter(new FileWriter("receipt_" + number + ".txt"));
        w.println("Касова бележка №" + number);
        w.println("Касиер: " + cashier.getName());
        w.println("Дата: " + dateTime);
        for (ReceiptItem item : items) {
            w.println(item);
        }
        w.println("Общо: " + String.format("%.2f", total) + " лв");
        w.close();
    }

    public double getTotal() {
        return total;
    }
}

// === InsufficientQuantityException.java ===
// Изключение при недостатъчно количество от продукт
public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String name, int req, int avail) {
        super("Недостатъчно количество от " + name + ": заявено " + req + ", налични " + avail);
    }
}

// === Store.java ===
// Основен клас, който описва магазина
import java.util.*;

public class Store {
    List<Product> inventory = new ArrayList<>(); // Списък с налични продукти
    List<Cashier> cashiers = new ArrayList<>(); // Списък с касиери
    List<Receipt> receipts = new ArrayList<>(); // Списък с касови бележки
    int receiptCounter = 1; // Брояч за бележките

    // Параметри на магазина
    public double foodMarkup = 0.3;
    public double nonFoodMarkup = 0.5;
    public double foodDiscountPercent = 0.2;
    public int foodDiscountDays = 5;

    // Добавя касиер
    public void addCashier(Cashier c) {
        cashiers.add(c);
    }

    // Добавя продукт
    public void addProduct(Product p) {
        inventory.add(p);
    }

    // Извършва продажба и връща касова бележка
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
            if (p == null || p.isExpired()) continue;
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
            System.out.println("Грешка при запис: " + ex.getMessage());
        }
        return r;
    }

    // Общо приходи от продажби
    public double getTotalIncome() {
        double total = 0;
        for (Receipt r : receipts) {
            total += r.getTotal();
        }
        return total;
    }

    // Общи разходи (заплати и доставки)
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

    // Печалба = приходи - разходи
    public double getProfit() {
        return getTotalIncome() - getTotalExpenses();
    }

    // Брой издадени бележки
    public int getNumberOfReceipts() {
        return receipts.size();
    }
}

// === App.java ===
// Основен клас, демонстриращ използването на системата
import java.time.LocalDate;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Store store = new Store();

        // Създаване на касиер
        Cashier john = new Cashier(1, "Йоан Иванов", 1200);
        store.addCashier(john);

        // Добавяне на продукти
        store.addProduct(new FoodProduct(1, "Мляко", 1.2, LocalDate.now().plusDays(3), 100));
        store.addProduct(new NonFoodProduct(2, "Телевизор", 300, LocalDate.now().plusYears(2), 10));

        // Симулация на кошница с покупки
        Map<Integer, Integer> basket = new HashMap<>();
        basket.put(1, 2);
        basket.put(2, 1);

        // Опит за продажба
        try {
            Receipt r = store.sellProducts(basket, john);
            System.out.println("Покупката е успешна. Обща сума: " + r.getTotal() + " лв");
        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }

        // Извеждане на финансов отчет
        System.out.println("Оборот: " + store.getTotalIncome() + " лв");
        System.out.println("Разходи: " + store.getTotalExpenses() + " лв");
        System.out.println("Печалба: " + store.getProfit() + " лв");
    }
}
