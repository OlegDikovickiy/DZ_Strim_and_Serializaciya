import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Basket {

  protected String[] products;
  protected int[] prices;
  protected int[] quantityProducts;
  protected int totalPay = 0;

  public Basket(String[] products, int[] prices) {
    this.products = products;
    this.prices = prices;
    this.quantityProducts = new int[prices.length];
  }


  private Basket(String[] products, int[] prices, int[] quantityProducts) {
    this.products = products;
    this.prices = prices;
    this.quantityProducts = quantityProducts;
  }

  public void addToCart(int productNum, int amount) {
    quantityProducts[productNum] += amount;
    totalPay += (amount * prices[productNum]);

  }

  public void printCart() {
    System.out.println("Ваша корзина: ");
    for (int i = 0; i < quantityProducts.length; i++) {
      if (quantityProducts[i] != 0) {
        System.out.println(products[i] + " " + quantityProducts[i] + "шт " + prices[i] + " руб/шт | "
            + (quantityProducts[i] * prices[i]) + "руб. в сумме");
      }
    }
    System.out.println("Итого к оплате: " + totalPay + "руб.");
  }

  public void saveTxt(File textFile) throws FileNotFoundException {
    try (PrintWriter out = new PrintWriter(textFile)) {
      for (String article : products) {
        out.print(article + " ");
      }
      out.println();

      for (int price : prices) {
        out.print(price + " ");
      }
      out.println();
      for (int quantity : quantityProducts) {
        out.print(quantity + " ");
      }
      out.println();
    }
  }

  public static Basket loadFromTxtFile(File textFile) throws FileNotFoundException {
    try (Scanner scanner = new Scanner(new FileInputStream(textFile))) {
      String[] products = scanner.nextLine().split(" ");
      int[] prices = Arrays.stream(scanner.nextLine().split(" "))
          .mapToInt(Integer::parseInt).toArray();
      int[] quantityProducts = Arrays.stream(scanner.nextLine().trim().split(" "))
          .mapToInt(Integer::parseInt).toArray();

      return new Basket(products, prices, quantityProducts);
    }
  }
}
