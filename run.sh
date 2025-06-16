#!/bin/bash
# Compile the Java sources and run a small demo that exercises the Store
# functionality by creating a Store instance and calling its methods.
set -e

# compile sources
javac *.java

# create a temporary Demo class that uses the Store
cat <<'JAVA' > Demo.java
public class Demo {
    public static void main(String[] args) throws Exception {
        Store store = new Store();

        // add a cashier
        Cashier cashier = new Cashier(1, "Йоан Иванов", 1200);
        store.addCashier(cashier);

        // add products
        store.addProduct(new FoodProduct(1, "Мляко", 1.2,
                java.time.LocalDate.now().plusDays(3), 100));
        store.addProduct(new NonFoodProduct(2, "Телевизор", 300,
                java.time.LocalDate.now().plusYears(2), 10));

        // create a basket and sell products
        java.util.Map<Integer, Integer> basket = new java.util.HashMap<>();
        basket.put(1, 2);
        basket.put(2, 1);

        try {
            Receipt receipt = store.sellProducts(basket, cashier);
            System.out.println("Покупката е успешна. Обща сума: " + receipt.getTotal() + " лв");
        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }

        // print store statistics
        System.out.println("Оборот: " + store.getTotalIncome() + " лв");
        System.out.println("Разходи: " + store.getTotalExpenses() + " лв");
        System.out.println("Печалба: " + store.getProfit() + " лв");
        System.out.println("Брой касови бележки: " + store.getNumberOfReceipts());
    }
}
JAVA

# compile and run the demo
javac Demo.java
java Demo

# cleanup generated files
rm -f Demo.java Demo.class
