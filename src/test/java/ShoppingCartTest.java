import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    // --- calculateItemCost tests ---

    @Test
    public void testItemCostNormal() {
        // 5.0 price x 4 quantity = 20.0
        assertEquals(20.0, ShoppingCart.calculateItemCost(5.0, 4));
    }

    @Test
    public void testItemCostZeroQuantity() {
        // 0 quantity should always give 0
        assertEquals(0.0, ShoppingCart.calculateItemCost(10.0, 0));
    }

    @Test
    public void testItemCostZeroPrice() {
        // 0 price should always give 0
        assertEquals(0.0, ShoppingCart.calculateItemCost(0.0, 5));
    }

    @Test
    public void testItemCostOneItem() {
        // quantity 1 should return just the price
        assertEquals(9.99, ShoppingCart.calculateItemCost(9.99, 1));
    }

    // --- calculateTotalCost tests ---

    @Test
    public void testTotalCostMultipleItems() {
        double[] costs = {10.0, 20.0, 30.0};
        assertEquals(60.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    public void testTotalCostSingleItem() {
        double[] costs = {15.5};
        assertEquals(15.5, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    public void testTotalCostEmptyCart() {
        double[] costs = {};
        assertEquals(0.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    public void testTotalCostWithDecimals() {
        double[] costs = {1.99, 2.49, 3.52};
        assertEquals(8.0, ShoppingCart.calculateTotalCost(costs), 0.001);
    }
    // test negative price
    @Test
    public void testItemCostNegativePrice() {
        assertEquals(-10.0, ShoppingCart.calculateItemCost(-2.0, 5));
    }

    // test large quantity
    @Test
    public void testItemCostLargeQuantity() {
        assertEquals(1000.0, ShoppingCart.calculateItemCost(2.0, 500));
    }

    // test total with all zeros
    @Test
    public void testTotalCostAllZeros() {
        double[] costs = {0.0, 0.0, 0.0};
        assertEquals(0.0, ShoppingCart.calculateTotalCost(costs));
    }

    // test many items
    @Test
    public void testTotalCostManyItems() {
        double[] costs = {5.0, 5.0, 5.0, 5.0, 5.0};
        assertEquals(25.0, ShoppingCart.calculateTotalCost(costs));
    }
}