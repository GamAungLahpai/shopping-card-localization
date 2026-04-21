package utli;

import dao.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    private final LocalizationService service = new LocalizationService();

    @Test
    void getStrings_shouldReturnEmpty_whenDBFails() {
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException("fail"));

            Map<String, String> result = service.getStrings("en_US");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void getStrings_shouldFallbackToEnglish() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        // simulate empty result
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            Map<String, String> result = service.getStrings("ar_AR");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void getStrings_shouldReturnMap_whenDataExists() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("key")).thenReturn("hello");
        when(rs.getString("value")).thenReturn("Hello");

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            Map<String, String> result = service.getStrings("en_US");

            assertEquals("Hello", result.get("hello"));
        }
    }
}