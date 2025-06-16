import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

// Class for receipt
public class Receipt implements Serializable {
    private int number;
    private Cashier cashier;
    private LocalDate date;
    private List<ReceiptItem> items;
    private double total = 0.0;

    public Receipt(int n, Cashier c, List<ReceiptItem> i) {
        number = n;
        cashier = c;
        date = LocalDate.now();
        items = i;
        for (ReceiptItem it : i) {
            total += it.totalPrice;
        }
    }

    // Saves the receipt to a file
    public void printAndSave() throws IOException {
        PrintWriter w = new PrintWriter(new FileWriter("receipt_" + number + ".txt"));
        w.println("Receipt No." + number);
        w.println("Cashier: " + cashier.getName());
        w.println("Date: " + date);
        for (ReceiptItem item : items) {
            w.println(item);
        }
        w.println("Total: " + String.format("%.2f", total) + "BGN");
        w.close();
    }

    public double getTotal() {
        return total;
    }

    public int getNumber() {
        return number;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }
}
