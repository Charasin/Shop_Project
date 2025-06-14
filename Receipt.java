import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// Клас за касова бележка
public class Receipt implements Serializable {
    int number;
    Cashier cashier;
    LocalDateTime dateTime;
    List<ReceiptItem> items;
    double total;

    public Receipt(int n, Cashier c, List<ReceiptItem> i) {
        number = n;
        cashier = c;
        dateTime = LocalDateTime.now();
        items = i;
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
