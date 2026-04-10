package utli;

import dao.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class CartServiceTest {

    private final CartService cartService = new CartService();

    @Test
    void saveCart_shouldNotThrow_whenDatabaseIsUnavailable() {
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            List<double[]> items = List.of(new double[]{10.0, 2}, new double[]{5.0, 3});

            assertDoesNotThrow(() -> cartService.saveCart(2, 35.0, "en_US", items));
        }
    }

    @Test
    void saveCart_shouldNotThrow_withEmptyItems() {
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            assertDoesNotThrow(() -> cartService.saveCart(0, 0.0, "en_US", List.of()));
        }
    }

    @Test
    void saveCart_shouldNotThrow_forSingleItem() {
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            assertDoesNotThrow(() ->
                    cartService.saveCart(1, 10.0, "en_US", List.of(new double[]{10.0, 1}))
            );
        }
    }
}