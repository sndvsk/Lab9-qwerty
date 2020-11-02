package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.MaxQuantityExceededException;
import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "Point-of-sale" in the menu). Consists of the purchase menu,
 * current purchase dialog and shopping cart table.
 */
public class PurchaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(PurchaseController.class);

    private final SalesSystemDAO dao;
    private final ShoppingCart shoppingCart;

    @FXML
    private Button newPurchase;
    @FXML
    private Button submitPurchase;
    @FXML
    private Button cancelPurchase;
    @FXML
    private TextField barCodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox<String> nameDropdownField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addItemButton;
    @FXML
    private Label totalSum;
    @FXML
    private TableView<SoldItem> purchaseTableView;

    public PurchaseController(SalesSystemDAO dao, ShoppingCart shoppingCart) {
        this.dao = dao;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        purchaseTableView.setItems(FXCollections.observableList(shoppingCart.getAll()));
        disableProductField(true);

        this.barCodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItem();
                }
            }
        });
        this.nameDropdownField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean
                    newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsByNameDropdownField();
                }
            }
        });
        log.info("Initialize PurchaseController");
    }

    /**
     * Event handler for the <code>new purchase</code> event.
     */
    @FXML
    protected void newPurchaseButtonClicked() {
        log.info("New sale process started");
        try {
            enableInputs();
            populateNameDropdownField();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>cancel purchase</code> event.
     */
    @FXML
    protected void cancelPurchaseButtonClicked() {
        log.info("Sale cancelled");
        try {
            shoppingCart.cancelCurrentPurchase();
            disableInputs();
            resetTotalSum();
            purchaseTableView.refresh();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>submit purchase</code> event.
     */
    @FXML
    protected void submitPurchaseButtonClicked() {
        log.info("Sale complete");
        try {
            log.debug("Contents of the current basket:\n" + shoppingCart.getAll());
            shoppingCart.submitCurrentPurchase();
            disableInputs();
            resetTotalSum();
            purchaseTableView.refresh();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    // switch UI to the state that allows to proceed with the purchase
    private void enableInputs() {
        resetProductField();
        disableProductField(false);
        cancelPurchase.setDisable(false);
        submitPurchase.setDisable(false);
        newPurchase.setDisable(true);
    }

    // switch UI to the state that allows to initiate new purchase
    private void disableInputs() {
        resetProductField();
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        newPurchase.setDisable(false);
        disableProductField(true);
    }

    // Fill other fields if item is selected by name
    private void fillInputsByNameDropdownField() {
        StockItem stockItem = new StockItem();
        try {
            long code = Long.parseLong(barCodeField.getText());
            stockItem = dao.findStockItem(code);
        } catch (NumberFormatException e) {
            System.out.println("Cannot find item" + e);
        }

        if (stockItem != null) {
            barCodeField.setText(String.valueOf(getStockItemByBarcode()));
            priceField.setText(String.valueOf(stockItem.getPrice()));
        } else {
            resetProductField();
        }
    }

    // Fill other fields if item is selected by barcode
    private void fillInputsBySelectedStockItem() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            nameDropdownField.getSelectionModel().select(stockItem.getName());
            priceField.setText(String.valueOf(stockItem.getPrice()));
        } else {
            resetProductField();
        }
    }

    // Search the warehouse for a StockItem with the bar code entered
    // to the barCode textfield.
    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeField.getText());
            return dao.findStockItem(code);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Add new item to the cart.
     */
    @FXML
    public void addItemEventHandler() {
        // add chosen item to the shopping cart.
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            int quantity;
            double price = stockItem.getPrice();
            try {
                price = Double.parseDouble(priceField.getText());
                quantity = Integer.parseInt(quantityField.getText());
                stockItem.setPrice(price);

            } catch (NumberFormatException e) {
                quantity = 1;
            }
            try {
                shoppingCart.addItem(new SoldItem(stockItem, quantity));
            } catch (MaxQuantityExceededException e) {
                log.error("Warehouse quantity exceeded. Your quantity: " + quantity + " Warehouse quantity: " + stockItem.getQuantity());
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Warehouse quantity exceeded. Your quantity: " + quantity + " Warehouse quantity: " + stockItem.getQuantity(), ButtonType.OK);
                errorAlert.setHeaderText("Add to cart");
                errorAlert.showAndWait();
                return;
            } catch (NegativeQuantityException e) {
                log.error("Quantity cannot be negative:" + price);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Quantity cannot be negative: " + quantity, ButtonType.OK);
                errorAlert.setHeaderText("Add to cart");
                errorAlert.showAndWait();
                return;
            } catch (NegativePriceException e) {
                log.error("Price cannot be negative:" + price);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Price cannot be negative: " + price, ButtonType.OK);
                errorAlert.setHeaderText("Add to cart");
                errorAlert.showAndWait();
                return;
            }
            refreshTotalSum(shoppingCart);
            purchaseTableView.refresh();
        }
    }

    // Populates stocks dropdown with item names
    private void populateNameDropdownField() {
        List<StockItem> stockItems = dao.getAllStockItems();
        Map<Long, String> names = new HashMap<>();
//        List<String> names = new ArrayList<>();
        for (StockItem item : stockItems) {
//            names.add(item.getName());
            names.put(item.getId(), item.getName());
        }
        // Add items to fxml observableList
        ObservableList<String> observableArrayList = FXCollections.observableArrayList(names.values());
//        ObservableMap<Long, String> observableArrayList = FXCollections.observableMap(names);
        nameDropdownField.setItems(observableArrayList); // Items visible in dropdown now
    }

    // Update total sum
    private void refreshTotalSum(ShoppingCart shoppingCart) {
        String totalSumString = String.valueOf(shoppingCart.getTotalSum());
        totalSum.setText(totalSumString);
    }

    // Reset total sum to 0.0
    private void resetTotalSum() {
        totalSum.setText("0.0");
    }

    /**
     * Sets whether or not the product component is enabled.
     */
    private void disableProductField(boolean disable) {
        this.addItemButton.setDisable(disable);
        this.barCodeField.setDisable(disable);
        this.quantityField.setDisable(disable);
        this.nameDropdownField.setDisable(disable);
        this.priceField.setDisable(disable);
    }

    /**
     * Reset dialog fields.
     */
    private void resetProductField() {
        barCodeField.setText("");
        quantityField.setText("1");
        nameDropdownField.getSelectionModel().select("");
        priceField.setText("");
    }
}
