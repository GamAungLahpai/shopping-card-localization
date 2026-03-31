package controller;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.ShoppingCart;

import java.util.*;

public class MainController {

    @FXML private VBox root;
    @FXML private Label languageLabel;
    @FXML private ComboBox<String> languageBox;
    @FXML private TextField numItemsField;
    @FXML private Button createItemsButton;
    @FXML private Button calculateButton;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox itemsBox;
    @FXML private Label resultLabel;

    private ResourceBundle messages;
    private Double lastCalculatedTotal;

    @FXML
    public void initialize() {
        languageBox.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        languageBox.setValue("English");

        // ← CHANGED: added new UTF8Control()
        messages = ResourceBundle.getBundle("MessagesBundle", new Locale("en", "US"), new UTF8Control());

        languageBox.setOnAction(e -> changeLanguage());
        updateTexts();
    }

    private void changeLanguage() {
        NodeOrientation orientation = NodeOrientation.LEFT_TO_RIGHT;

        switch (languageBox.getValue()) {
            case "Finnish":
                // ← CHANGED: added new UTF8Control()
                messages = ResourceBundle.getBundle("MessagesBundle", new Locale("fi", "FI"), new UTF8Control());
                break;
            case "Swedish":
                // ← CHANGED: added new UTF8Control()
                messages = ResourceBundle.getBundle("MessagesBundle", new Locale("sv", "SE"), new UTF8Control());
                break;
            case "Japanese":
                // ← CHANGED: added new UTF8Control()
                messages = ResourceBundle.getBundle("MessagesBundle", new Locale("ja", "JP"), new UTF8Control());
                break;
            case "Arabic":
                // ← CHANGED: added new UTF8Control()
                messages = ResourceBundle.getBundle("MessagesBundle", new Locale("ar", "AR"), new UTF8Control());
                orientation = NodeOrientation.RIGHT_TO_LEFT;
                break;
            default:
                // ← CHANGED: added new UTF8Control()
                messages = ResourceBundle.getBundle("MessagesBundle", new Locale("en", "US"), new UTF8Control());
        }

        root.setNodeOrientation(orientation);
        scrollPane.setNodeOrientation(orientation);
        itemsBox.setNodeOrientation(orientation);

        for (Node node : itemsBox.getChildren()) {
            node.setNodeOrientation(orientation);
        }

        updateTexts();
    }

    private void updateTexts() {
        languageLabel.setText(messages.getString("select.language"));
        numItemsField.setPromptText(messages.getString("number.of.items"));
        createItemsButton.setText(messages.getString("create.items"));
        calculateButton.setText(messages.getString("calculate"));
        updateItemPrompts();

        if (lastCalculatedTotal == null) {
            resultLabel.setText("");
        } else {
            resultLabel.setText(messages.getString("total.cost") + " " + lastCalculatedTotal);
        }
    }

    // Each item card is a VBox with children:
    //   [0] Label     - item header ("Item 1")
    //   [1] TextField - price
    //   [2] TextField - quantity
    //   [3] Label     - per-item total (filled after calculate)
    private void updateItemPrompts() {
        for (int i = 0; i < itemsBox.getChildren().size(); i++) {
            VBox card = (VBox) itemsBox.getChildren().get(i);
            int num = i + 1;
            ((Label) card.getChildren().get(0)).setText(messages.getString("item.label") + " " + num);
            ((TextField) card.getChildren().get(1)).setPromptText(messages.getString("enter.price") + " " + num);
            ((TextField) card.getChildren().get(2)).setPromptText(messages.getString("enter.quantity") + " " + num);

            Label itemTotalLabel = (Label) card.getChildren().get(3);
            if (!itemTotalLabel.getText().isEmpty()) {
                String value = itemTotalLabel.getText().replaceAll(".*\\s", "");
                itemTotalLabel.setText(messages.getString("item.total") + " " + value);
            }
        }
    }

    private VBox createItemCard(int index) {
        VBox card = new VBox(6);
        card.setStyle("-fx-padding: 8; -fx-border-color: #ccc; -fx-border-width: 1;");

        // Inherit current orientation
        card.setNodeOrientation(root.getEffectiveNodeOrientation());

        Label header = new Label(messages.getString("item.label") + " " + index);
        header.setStyle("-fx-font-weight: bold;");

        TextField priceField = new TextField();
        priceField.setPromptText(messages.getString("enter.price") + " " + index);
        priceField.setStyle("-fx-padding: 7;");

        TextField quantityField = new TextField();
        quantityField.setPromptText(messages.getString("enter.quantity") + " " + index);
        quantityField.setStyle("-fx-padding: 7;");

        Label itemTotalLabel = new Label();

        card.getChildren().addAll(header, priceField, quantityField, itemTotalLabel);
        return card;
    }

    @FXML
    private void handleCreateItems() {
        itemsBox.getChildren().clear();
        lastCalculatedTotal = null;
        resultLabel.setText("");

        int numItems = Integer.parseInt(numItemsField.getText().trim());
        for (int i = 1; i <= numItems; i++) {
            itemsBox.getChildren().add(createItemCard(i));
        }
    }

    @FXML
    private void handleCalculate() {
        List<Double> costs = new ArrayList<>();

        for (Node node : itemsBox.getChildren()) {
            VBox card = (VBox) node;
            double price = Double.parseDouble(((TextField) card.getChildren().get(1)).getText().trim());
            int quantity = Integer.parseInt(((TextField) card.getChildren().get(2)).getText().trim());

            double itemCost = ShoppingCart.calculateItemCost(price, quantity);
            costs.add(itemCost);

            Label itemTotalLabel = (Label) card.getChildren().get(3);
            itemTotalLabel.setText(messages.getString("item.total") + " " + itemCost);
        }

        double total = ShoppingCart.calculateTotalCost(
                costs.stream().mapToDouble(Double::doubleValue).toArray()
        );

        lastCalculatedTotal = total;
        resultLabel.setText(messages.getString("total.cost") + " " + total);
    }
}
