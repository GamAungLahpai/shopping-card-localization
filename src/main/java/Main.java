import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        // Language selection
        System.out.println("Select language / Valitse kieli / Välj språk / 言語を選択:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");
        System.out.print("Choice: ");

        int langChoice = scanner.nextInt();

        Locale locale;
        switch (langChoice) {
            case 2: locale = new Locale("fi", "FI"); break;
            case 3: locale = new Locale("sv", "SE"); break;
            case 4: locale = new Locale("ja", "JP"); break;
            default: locale = new Locale("en", "US"); break;
        }

        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

        System.out.print(messages.getString("enter.number.of.items") + " ");
        int numItems = scanner.nextInt();

        double[] costs = new double[numItems];

        for (int i = 0; i < numItems; i++) {
            System.out.print(messages.getString("enter.price") + " " + (i + 1) + ": ");
            double price = scanner.nextDouble();

            System.out.print(messages.getString("enter.quantity") + " " + (i + 1) + ": ");
            int quantity = scanner.nextInt();

            costs[i] = ShoppingCart.calculateItemCost(price, quantity);
        }

        double total = ShoppingCart.calculateTotalCost(costs);
        System.out.println(messages.getString("total.cost") + " " + total);

        scanner.close();
    }
}