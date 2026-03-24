public class ShoppingCart {

    public static double calculateItemCost(double price, int quantity) {
        return price * quantity;
    }

    public static double calculateTotalCost(double[] costs) {
        double total = 0;
        for (double cost : costs) {
            total += cost;
        }
        return total;
    }
}