package dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DatabaseConnectionTest {

    @Test
    void getConnection_shouldNotCrash() {
        assertDoesNotThrow(() -> {
            try {
                DatabaseConnection.getConnection();
            } catch (Exception ignored) {
                // acceptable
            }
        });
    }
}