package utli;

import dao.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class LocalizationServiceTest {

    private final LocalizationService localizationService = new LocalizationService();

    @Test
    void getStrings_shouldReturnNonNullMap_forEnglishWithoutDatabase() {
        Map<String, String> strings;
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            strings = localizationService.getStrings("en_US");
        }

        assertNotNull(strings);
        assertTrue(strings.isEmpty());
    }

    @Test
    void getStrings_shouldReturnNonNullMap_forNonEnglishWithoutDatabase() {
        Map<String, String> strings;
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            strings = localizationService.getStrings("ar_AR");
        }

        assertNotNull(strings);
        assertTrue(strings.isEmpty());
    }

    @Test
    void getStrings_shouldReturnIndependentMapsOnMultipleCalls() {
        Map<String, String> firstCall;
        Map<String, String> secondCall;
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenThrow(new SQLException("No DB for unit test"));
            firstCall = localizationService.getStrings("ja_JP");
            secondCall = localizationService.getStrings("sv_SE");
        }

        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertNotSame(firstCall, secondCall);
    }
}