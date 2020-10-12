package ee.ut.math.tvt.salessystem.ui.controllers;

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
        StockItem newItem = new StockItem(new Long("7"), nameField.toString(), "description", Double.parseDouble(String.valueOf(priceField)), Integer.parseInt(String.valueOf(quantityField)));
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
}
