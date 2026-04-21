package controller;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utli.CartService;
import utli.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {

    private MainController controller;
    private LocalizationService localizationService;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        new JFXPanel();
        controller = spy(new MainController());

        // Mock services
        localizationService = mock(LocalizationService.class);
        cartService = mock(CartService.class);

        controller.setLocalizationService(localizationService);
        controller.setCartService(cartService);

        doNothing().when(controller).showAlert(anyString());

        // Mock UI elements
        controller.root = new VBox();
        controller.languageLabel = new Label();
        controller.languageBox = new ComboBox<>();
        controller.numItemsField = new TextField();
        controller.createItemsButton = new Button();
        controller.calculateButton = new Button();
        controller.itemsBox = new VBox();
        controller.resultLabel = new Label();

        // Mock localization strings
        Map<String, String> strings = new HashMap<>();
        strings.put("select.language", "Select Language");
        strings.put("number.of.items", "Enter items");
        strings.put("create.items", "Create");
        strings.put("calculate", "Calculate");
        strings.put("total.cost", "Total");
        strings.put("item.label", "Item");
        strings.put("enter.price", "Price");
        strings.put("enter.quantity", "Qty");
        strings.put("item.total", "Item Total");
        strings.put("error.invalid.number", "Invalid number");

        when(localizationService.getStrings(anyString())).thenReturn(strings);
    }

    @Test
    void initialize_shouldLoadUI() {
        assertDoesNotThrow(() -> controller.initialize());

        assertEquals("Select Language", controller.languageLabel.getText());
        assertEquals("Create", controller.createItemsButton.getText());
    }

    @Test
    void handleCreateItems_validInput_shouldCreateCards() {
        controller.initialize();

        controller.numItemsField.setText("3");
        controller.handleCreateItems();

        assertEquals(3, controller.itemsBox.getChildren().size());
    }

    @Test
    void handleCreateItems_invalidInput_shouldNotCrash() {
        controller.initialize();

        controller.numItemsField.setText("abc");

        assertDoesNotThrow(() -> controller.handleCreateItems());
    }

    @Test
    void handleCalculate_validInput_shouldCalculateTotal() {
        controller.initialize();

        // Create 1 item manually
        controller.numItemsField.setText("1");
        controller.handleCreateItems();

        VBox card = (VBox) controller.itemsBox.getChildren().get(0);

        ((TextField) card.getChildren().get(1)).setText("10");
        ((TextField) card.getChildren().get(2)).setText("2");

        assertDoesNotThrow(() -> controller.handleCalculate());

        assertTrue(controller.resultLabel.getText().contains("20.00"));

        verify(cartService, atLeastOnce())
                .saveCart(anyInt(), anyDouble(), anyString(), anyList());
    }

    @Test
    void handleCalculate_invalidInput_shouldNotCrash() {
        controller.initialize();

        controller.numItemsField.setText("1");
        controller.handleCreateItems();

        VBox card = (VBox) controller.itemsBox.getChildren().get(0);

        ((TextField) card.getChildren().get(1)).setText("abc");
        ((TextField) card.getChildren().get(2)).setText("xyz");

        assertDoesNotThrow(() -> controller.handleCalculate());
    }

    @Test
    void changeLanguage_shouldUpdateStrings() {
        controller.initialize();

        controller.languageBox.setValue("Arabic");
        controller.languageBox.getOnAction().handle(null);

        verify(localizationService, atLeastOnce())
                .getStrings(anyString());
    }
}