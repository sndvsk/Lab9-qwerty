package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private static final Logger log = LogManager.getLogger(StockController.class);

    private final SalesSystemDAO dao;

    @FXML
    private Button addProduct;
    @FXML
    private Button saveChanges;
    @FXML
    private Button deleteProduct;
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
    @FXML
    private InMemorySalesSystemDAO stock;

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveChanges.setDisable(true);
        deleteProduct.setDisable(true);
        refreshStockItems();
        // TODO refresh view after adding new items
    }

    // Event handler for adding new product to warehouse
    @FXML
    public void addProductButtonClicked() {
        log.info("Add new product");
        refreshStockItems();
        try {
            // ei tea kas see töötab, sest saan neid errori ja praegu ei saa aru mida nendega teha:

            // Caused by: java.lang.reflect.InvocationTargetException

            // Caused by: java.lang.NumberFormatException:
            // For input string: "TextField[id=barCodeField, styleClass=text-input text-field]"

            StockItem newItem = new StockItem(Long.parseLong(barCodeField.getText()), nameField.toString(), "description", Double.parseDouble(String.valueOf(priceField)), Integer.parseInt(String.valueOf(quantityField)));
            stock.saveStockItem(newItem);
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
        refreshStockItems();
        // TODO
    }

    // Event handler for updating existing product data
    @FXML
    public void saveChangesButtonClicked() {
        log.info("Update product info");
        enableInputs();
        refreshStockItems();
        // TODO
    }

    // Event handler for deleting a product from the system
    @FXML
    public void deleteProductButtonClicked() {
        log.info("Deleting a product");
        refreshStockItems();
    }

    // Event handler to refresh warehouse table view
    @FXML
    public void refreshButtonClicked() {
        refreshStockItems();
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(FXCollections.observableList(dao.findStockItems()));
        warehouseTableView.refresh();
    }

    // Enable buttons like this
    private void enableInputs() {
        saveChanges.setDisable(false);
        deleteProduct.setDisable(false);
    }

    //
    private void disableInputs() {
        resetProductField();
        addProduct.setDisable(true);
        saveChanges.setDisable(true);
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
