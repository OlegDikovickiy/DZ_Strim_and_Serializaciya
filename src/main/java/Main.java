import java.io.*;
import java.util.Scanner;

public class Main {


  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);

    String[] products = {"Хлеб", "Молоко", "Яблоки"};
    int[] prices = {100, 200, 300};
    Basket basket;

    ClientLog clientLog = new ClientLog(products, prices);
    File txtFile = new File("basket.txt");
    File csvFile = new File("log.csv");
    File jsonBasket = new File("basket.json");

    if (jsonBasket.exists()) {
      basket = Basket.loadJson(jsonBasket);
    } else {
      basket = new Basket(products, prices);
    }

    for (int i = 0; i < products.length; i++) {
      System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
    }
    while (true) {
      System.out.println("Выберите товар и количество или введите `end`");
      String input = scanner.nextLine();
      if ("end".equals(input)) {
        break;
      }
      String[] parts = input.split(" ");
      int productNumber = Integer.parseInt(parts[0]) - 1;
      int productCount = Integer.parseInt(parts[1]);
      basket.addToCart(productNumber, productCount);
      clientLog.log(productNumber + 1, productCount);
      basket.saveJson(jsonBasket);
    }

    clientLog.exportAsCSV(csvFile);
    scanner.close();
    basket.printCart();
  }
}