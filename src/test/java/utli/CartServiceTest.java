package utli;

import dao.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private final CartService cartService = new CartService();

    @Test
    void saveCart_shouldHandleSQLExceptionGracefully() {
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException("No DB"));

            assertDoesNotThrow(() ->
                    cartService.saveCart(2, 50.0, "en_US", List.of())
            );
        }
    }

    @Test
    void saveCart_shouldExecuteFullInsertFlow() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement cartStmt = mock(PreparedStatement.class);
        PreparedStatement itemStmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(cartStmt);

        when(conn.prepareStatement(anyString()))
                .thenReturn(itemStmt);

        when(cartStmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(1);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            List<double[]> items = List.of(
                    new double[]{10.0, 2},
                    new double[]{5.0, 3}
            );

            cartService.saveCart(2, 35.0, "en_US", items);

            verify(cartStmt).executeUpdate();
            verify(itemStmt, atLeastOnce()).addBatch();
            verify(itemStmt).executeBatch();
            verify(conn).commit();
        }
    }

    @Test
    void saveCart_shouldWorkWithEmptyItems() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement cartStmt = mock(PreparedStatement.class);
        PreparedStatement itemStmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);
        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(cartStmt);

        when(conn.prepareStatement(anyString()))
                .thenReturn(itemStmt);

        when(cartStmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(1);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            assertDoesNotThrow(() ->
                    cartService.saveCart(0, 0.0, "en_US", List.of())
            );

            verify(cartStmt).executeUpdate();
            verify(conn).commit();
        }
    }
}