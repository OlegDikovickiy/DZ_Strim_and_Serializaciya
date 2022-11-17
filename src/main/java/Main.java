import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Scanner;

public class Main {


  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    Scanner scanner = new Scanner(System.in);

    String[] products = {"Хлеб", "Молоко", "Яблоки"};
    int[] prices = {100, 200, 300};
    Basket basket;

    ClientLog clientLog = new ClientLog(products, prices);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(new File("shop.xml"));

    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();

    //нужно ли загружать корзину? Откуда?
    boolean loadFileEnabled = Boolean.parseBoolean(xPath.compile("config/load/enabled").evaluate(document));
    String loadFileName = xPath.compile("/config/load/fileName").evaluate(document);
    String loadFileFormat = xPath.compile("/config/load/format").evaluate(document);
    //нужно ли сохранять? Куда? в каком формате?
    boolean saveFileEnabled = Boolean.parseBoolean(xPath.compile("config/save/enabled").evaluate(document));
    String saveFileName = xPath.compile("config/save/fileName").evaluate(document);
    String saveFileFormat = xPath.compile("config/save/format").evaluate(document);
    // Нужно ли сохранять лог? и Куда?
    boolean logFileEnabled = Boolean.parseBoolean(xPath.compile("config/log/enabled").evaluate(document));
    String logFileName = xPath.compile("config/log/fileName").evaluate(document);

    File txtFile = new File("basket.txt");
    File jsonBasket = new File("basket.json");

    // Загрузка файла: файл есть, файла нет (нет папки для сохранения)
    if (loadFileEnabled) {
      if (loadFileFormat.equals("json") && loadFileName.equals("basket.json")) {
        if (jsonBasket.exists()) {
          basket = Basket.loadJson(jsonBasket);
        } else {
          basket = new Basket(products, prices);
          System.out.println("Старая корзина не обнаружена, создана новая корзина (.json)");
        }
      } else if (loadFileFormat.equals("text") && loadFileName.equals("basket.txt")) {
        if (txtFile.exists()) {
          basket = Basket.loadTxtFile(txtFile);
        } else {
          basket = new Basket(products, prices);
          System.out.println("Старая корзина не обнаружена, создана новая корзина (.txt)");
        }
      } else {
        System.out.println("Выбраный формат файла не имеет методов для загрузки, создана новая корзина");
        basket = new Basket(products, prices);
      }
    } else {
      basket = new Basket(products, prices);
    }

    //основной цикл
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

      //нужно ли сохранять данные, если да, то как
      if (saveFileEnabled) {
        if (saveFileName.equals("basket.json") && saveFileFormat.equals("json")) {
          basket.saveJson(jsonBasket);
        }
        if (saveFileName.equals("basket.txt") && saveFileFormat.equals("text")) {
          basket.saveTxt(txtFile);
        }
      } else {
        System.out.println("basket don`t save!");
        break;
      }
    }

    // нужно сохранять лог?
    if (logFileEnabled) {
      clientLog.exportAsCSV(new File(logFileName));
    } else {
      System.out.println("log-файл не сохранен!");
    }

    scanner.close();
    basket.printCart();
  }
}