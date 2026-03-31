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
    @FXML private VBox itemsBox;
    @FXML private Label resultLabel;

    private ResourceBundle bundle;
    private final Map<String, Locale> localeMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        localeMap.put("English", new Locale("en", "US"));
        localeMap.put("Finnish", new Locale("fi", "FI"));
        localeMap.put("Swedish", new Locale("sv", "SE"));
        localeMap.put("Japanese", new Locale("ja", "JP"));
        localeMap.put("Arabic", new Locale("ar", "AR"));

        languageBox.getItems().addAll(localeMap.keySet());
        languageBox.setValue("English");

        loadBundle(localeMap.get("English"));
        applyTexts();

        languageBox.setOnAction(e -> changeLanguage());
    }

    private void changeLanguage() {
        Locale locale = localeMap.get(languageBox.getValue());
        loadBundle(locale);
        applyTexts();

        if (languageBox.getValue().equals("Arabic")) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);
    }

    private void applyTexts() {
        languageLabel.setText(bundle.getString("select.language"));
        numItemsField.setPromptText(bundle.getString("number.of.items"));
        createItemsButton.setText(bundle.getString("create.items"));
        calculateButton.setText(bundle.getString("calculate"));
        resultLabel.setText(bundle.getString("total.cost") + " -");

        updateItemTexts();
    }

    private void updateItemTexts() {
        for (int i = 0; i < itemsBox.getChildren().size(); i++) {
            VBox card = (VBox) itemsBox.getChildren().get(i);

            ((Label) card.getChildren().get(0))
                    .setText(bundle.getString("item.label") + " " + (i + 1));

            ((TextField) card.getChildren().get(1))
                    .setPromptText(bundle.getString("enter.price"));

            ((TextField) card.getChildren().get(2))
                    .setPromptText(bundle.getString("enter.quantity"));
        }
    }

    private VBox createItemCard(int index) {
        VBox card = new VBox(5);

        Label label = new Label(bundle.getString("item.label") + " " + index);

        TextField price = new TextField();
        price.setPromptText(bundle.getString("enter.price"));

        TextField qty = new TextField();
        qty.setPromptText(bundle.getString("enter.quantity"));

        Label total = new Label();

        card.getChildren().addAll(label, price, qty, total);
        return card;
    }

    @FXML
    private void handleCreateItems() {
        itemsBox.getChildren().clear();
        resultLabel.setText("");

        int n = Integer.parseInt(numItemsField.getText());

        for (int i = 1; i <= n; i++) {
            itemsBox.getChildren().add(createItemCard(i));
        }
    }

    @FXML
    private void handleCalculate() {
        List<Double> costs = new ArrayList<>();

        for (Node node : itemsBox.getChildren()) {
            VBox card = (VBox) node;

            double price = Double.parseDouble(
                    ((TextField) card.getChildren().get(1)).getText()
            );

            int qty = Integer.parseInt(
                    ((TextField) card.getChildren().get(2)).getText()
            );

            double itemTotal = ShoppingCart.calculateItemCost(price, qty);
            costs.add(itemTotal);

            ((Label) card.getChildren().get(3))
                    .setText(bundle.getString("item.total") + " " + itemTotal);
        }

        double total = ShoppingCart.calculateTotalCost(
                costs.stream().mapToDouble(Double::doubleValue).toArray()
        );

        resultLabel.setText(bundle.getString("total.cost") + " " + total);
    }
}