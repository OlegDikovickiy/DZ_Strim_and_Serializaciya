import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

  protected String[] products;
  protected int[] prices;
  protected int[] quantityProducts;
  protected List<String[]> historyBasket = new ArrayList<>();

  ClientLog(String[] products, int[] prices) {
    this.products = products;
    this.prices = prices;
    this.quantityProducts = new int[products.length];
  }

  public void log(int productNum, int amount) {
    historyBasket.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});

  }

  public void exportAsCSV(File csvFile) throws IOException {
    try (FileWriter writer = new FileWriter(csvFile, false)) {
      writer.write("productNum, amount \n");
      for (String[] line : historyBasket)
        writer.write(line[0] + "," + line[1] + "\n");
    }


  }
}
