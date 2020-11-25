package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.MaxQuantityExceededException;
import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import ee.ut.math.tvt.salessystem.logic.WarehouseStock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ShoppingCartTests {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);

    InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
    ShoppingCart cart = new ShoppingCart(dao);
    private final List<SoldItem> items = new ArrayList<>();

    @Mock
    SalesSystemDAO daoMock;
    WarehouseStock warehouseMockDao;
    ShoppingCart shoppingCartMock;

    StockItem item1 = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5);
    StockItem item2 = new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8);


    @Before
    public void setUp() throws Exception {
        openMocks(this);

        this.warehouseMockDao = new WarehouseStock(daoMock);
        this.shoppingCartMock = new ShoppingCart(daoMock);
    }

    // addItem
    @Test
    public void testAddingExistingItem() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        assertNotNull(dao.getAllStockItems());
        cart.addItem(new SoldItem(item1, 1));
        //TODO check that adding an existing item increases the quantity
    }

    @Test
    public void testAddingNewItem() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        assertNotNull(dao.getAllStockItems());
        assertEquals(new ArrayList<>(), cart.getAll());
        cart.addItem(new SoldItem(item1, 1));
        assertNotNull(cart.getAll());
    }

    @Test(expected = NegativeQuantityException.class)
    public void testAddingItemWithNegativeQuantity() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        assertNotNull(dao.getAllStockItems());
        cart.addItem(new SoldItem(item1, -1));
    }

    @Test(expected = MaxQuantityExceededException.class)
    public void testAddingItemWithQuantityTooLarge() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        cart.addItem(new SoldItem(item1, item1.getQuantity() + 1));
    }

    @Test(expected = MaxQuantityExceededException.class)
    public void testAddingItemWithQuantitySumTooLarge() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        cart.addItem(new SoldItem(item1, 3));
        cart.addItem(new SoldItem(item2, 2));
        cart.addItem(new SoldItem(item1, 1));
        cart.addItem(new SoldItem(item2, 1));
        cart.addItem(new SoldItem(item1, 2));
    }


    // submitCurrentPurchase
    @Test
    public void testSubmittingCurrentPurchaseDecreasesStockItemQuantity() throws NegativeQuantityException, MaxQuantityExceededException, NegativePriceException {
        int quantityToBeBought = 3;
        assertEquals(5, dao.findStockItem(item1.getId()).getQuantity());
        cart.addItem(new SoldItem(item1, quantityToBeBought));
        cart.submitCurrentPurchase();
        assertEquals(item1.getQuantity() - quantityToBeBought, dao.findStockItem(item1.getId()).getQuantity());
    }

    @Test
    public void testSubmittingCurrentPurchaseBeginsAndCommitsTransaction() {
        //TODO check that submitting the current purchase calls beginTransaction and endTransaction,
        // exactly once and in that order


//        Mockito.verify();
    }

    @Test
    public void testSubmittingCurrentOrderCreatesHistoryItem() {
        //TODO check that a new HistoryItem is saved and that it contains the correct SoldItems
    }

    @Test
    public void testSubmittingCurrentOrderSavesCorrectTime() {
        //TODO check that the timestamp on the created HistoryItem is set correctly (for example
        // has only a small difference to the current time)
    }

    @Test
    public void testCancellingOrder() {
        //TODO check that cancelling an order (with some items) and then submitting a new order
        // (with some different items) only saves the items from the new order (cancelled items are discarded)
    }

    @Test
    public void testCancellingOrderQuanititesUnchanged() {
        //TODO check that after cancelling an order the quantities of the related StockItems are not changed
    }

}
