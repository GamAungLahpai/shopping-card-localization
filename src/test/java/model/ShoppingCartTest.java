package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    // --- calculateItemCost tests ---

    @Test
    void testItemCostNormal() {
        // 5.0 price x 4 quantity = 20.0
        assertEquals(20.0, ShoppingCart.calculateItemCost(5.0, 4));
    }

    @Test
    void testItemCostZeroQuantity() {
        // 0 quantity should always give 0
        assertEquals(0.0, ShoppingCart.calculateItemCost(10.0, 0));
    }

    @Test
    void testItemCostZeroPrice() {
        // 0 price should always give 0
        assertEquals(0.0, ShoppingCart.calculateItemCost(0.0, 5));
    }

    @Test
    void testItemCostOneItem() {
        // quantity 1 should return just the price
        assertEquals(9.99, ShoppingCart.calculateItemCost(9.99, 1));
    }

    // --- calculateTotalCost tests ---

    @Test
    void testTotalCostMultipleItems() {
        double[] costs = {10.0, 20.0, 30.0};
        assertEquals(60.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testTotalCostSingleItem() {
        double[] costs = {15.5};
        assertEquals(15.5, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testTotalCostEmptyCart() {
        double[] costs = {};
        assertEquals(0.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testTotalCostWithDecimals() {
        double[] costs = {1.99, 2.49, 3.52};
        assertEquals(8.0, ShoppingCart.calculateTotalCost(costs), 0.001);
    }
    // test negative price
    @Test
    void testItemCostNegativePrice() {
        assertEquals(-10.0, ShoppingCart.calculateItemCost(-2.0, 5));
    }

    // test large quantity
    @Test
    void testItemCostLargeQuantity() {
        assertEquals(1000.0, ShoppingCart.calculateItemCost(2.0, 500));
    }

    // test total with all zeros
    @Test
    void testTotalCostAllZeros() {
        double[] costs = {0.0, 0.0, 0.0};
        assertEquals(0.0, ShoppingCart.calculateTotalCost(costs));
    }

    // test many items
    @Test
    void testTotalCostManyItems() {
        double[] costs = {5.0, 5.0, 5.0, 5.0, 5.0};
        assertEquals(25.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testItemCostNegativeQuantity() {
        assertEquals(-10.0, ShoppingCart.calculateItemCost(2.0, -5));
    }

    @Test
    void testItemCostNegativeBoth() {
        assertEquals(10.0, ShoppingCart.calculateItemCost(-2.0, -5));
    }

    @Test
    void testItemCostLargeNumbers() {
        assertEquals(1_000_000.0, ShoppingCart.calculateItemCost(1000.0, 1000));
    }


    @Test
    void testTotalCostWithNegativeValues() {
        double[] costs = {10.0, -5.0, 15.0};
        assertEquals(20.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testTotalCostMixedDecimals() {
        double[] costs = {1.1, 2.2, 3.3};
        assertEquals(6.6, ShoppingCart.calculateTotalCost(costs), 0.0001);
    }

    @Test
    void testTotalCostSingleNegative() {
        double[] costs = {-10.0};
        assertEquals(-10.0, ShoppingCart.calculateTotalCost(costs));
    }

    @Test
    void testTotalCostLargeArray() {
        double[] costs = new double[100];
        for (int i = 0; i < 100; i++) {
            costs[i] = 1.0;
        }
        assertEquals(100.0, ShoppingCart.calculateTotalCost(costs));
    }
}