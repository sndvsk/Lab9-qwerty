package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {

    private static final Logger log = LogManager.getLogger(PurchaseController.class);
    private final SalesSystemDAO dao;
    private final ShoppingCart shoppingCart;

    @FXML
    private Button showBetween;
    @FXML
    private Button showLast10;
    @FXML
    private Button showAll;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Label totalSum;
    @FXML
    private TableView<Purchase> historyTableView;
    @FXML
    private TableView<SoldItem> purchaseTableView;

    public HistoryController() {
        dao = new HibernateSalesSystemDAO();
        shoppingCart = new ShoppingCart(dao);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: implement
        log.info("Initialize HistoryController");
    }
}
