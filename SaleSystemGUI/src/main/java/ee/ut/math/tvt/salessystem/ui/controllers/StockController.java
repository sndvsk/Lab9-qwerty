package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.WarehouseStock;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private static final Logger log = LogManager.getLogger(StockController.class);

    private final SalesSystemDAO dao;
    private final WarehouseStock warehouseStock;

    @FXML
    private Button addProduct;
    @FXML
    private Button deleteProduct;
    @FXML
    private Button deleteItem;
    @FXML
    private TextField barCodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TableView<StockItem> warehouseTableView;

    public StockController(SalesSystemDAO dao, WarehouseStock warehouseStock) {
        this.dao = dao;
        this.warehouseStock = warehouseStock;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        deleteProduct.setDisable(true);
//        barCodeField.setDisable(true);
//        disableProductField();
        // warehouseTableView.setItems(FXCollections.observableList(warehouseStock.getAll()));
        this.barCodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItem();
                }
            }
        });
        refreshStockItems();
        // TODO refresh view after adding new items
    }

    // Event handler for adding new product to warehouse
    // SE-14 new items
    // SE-15 barCodeField automatically generated
    // SE-16 SE-21 name field
    @FXML
    public void addProductButtonClicked() {
        log.info("Add new products");
        //refreshStockItems();

        // Try to get the data from the fields, alert the user, if anything went wrong
        try {
            warehouseStock.addItem(nameField.getText(), priceField.getText(), quantityField.getText(), barCodeField.getText());
        } catch (NumberFormatException e) {
            log.info("Error: False data inserted, wrong format or left empty");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Information in wrong format", ButtonType.OK);
            errorAlert.setHeaderText("Add product");
            errorAlert.showAndWait();
        } catch (NegativePriceException e){  // | NegativeAmountException
            log.info("Error: Negative value inserted");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "These values cannot be negative: quantity, price", ButtonType.OK);
            errorAlert.setHeaderText("Add product");
            errorAlert.showAndWait();
        } catch (NullPointerException | SalesSystemException e) {
            log.info("Error: No searched item in stock");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Check ID field", ButtonType.OK);
            errorAlert.setHeaderText("Add product");
            errorAlert.showAndWait();
            log.info("Error: Could not add or update the product");
        }
        refreshStockItems();
        // TODO
    }

    // Event handler for deleting a product from the system
    // SE-20 product deletion
    @FXML
    public void deleteProductButtonClicked(){
        log.info("Deleting a product");
        long barCode = Long.parseLong(barCodeField.getText());
        // For now quantity = 1, if user does not insert it, it throws an error, quantity doesn't matter because this method deletes all of the item the data
        StockItem item = new StockItem(barCode, nameField.getText(), dao.getStockItemByBarcode(barCodeField.getText()).getDescription(), Double.parseDouble(priceField.getText()), 1);
        warehouseStock.deleteItem(item);
        refreshStockItems();
    }

    // Event handler for deleting some quantity of items of specific product from the systems
    // SE-19 item deletion
    @FXML
    public void deleteItemButtonClicked() {
        log.info("Deleting item of a product");
        refreshStockItems();
    }

    // Event handler to refresh warehouse table view
    // SE-18 list refresh
    @FXML
    public void refreshButtonClicked() {
        refreshStockItems();
    }

    // SE-18 list refresh
    // SE-17 list of products
    private void refreshStockItems() {
        warehouseTableView.setItems(FXCollections.observableList(dao.findStockItems()));
        warehouseTableView.refresh();
    }



    private void fillInputsBySelectedStockItem() {
        StockItem stockItem = dao.getStockItemByBarcode(barCodeField.getText());
        if (stockItem != null) {
            nameField.setText(stockItem.getName());
            priceField.setText(String.valueOf(stockItem.getPrice()));
        } else {
            resetProductField();
        }
    }



    // Enable buttons like this
    private void enableInputs() {
        deleteProduct.setDisable(false);
    }

    //
    private void disableInputs() {
        resetProductField();
        addProduct.setDisable(true);
        deleteProduct.setDisable(false);
        disableProductField();
    }

    //
    private void disableProductField() {
        this.addProduct.setDisable(true);
        this.barCodeField.setDisable(true);
        this.quantityField.setDisable(true);
        this.nameField.setDisable(true);
        this.priceField.setDisable(true);
    }

    //
    private void resetProductField() {
        barCodeField.setText("");
        quantityField.setText("1");
        nameField.setText("");
        priceField.setText("");
    }
}
