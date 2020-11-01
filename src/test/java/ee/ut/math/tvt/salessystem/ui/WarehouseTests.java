package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import ee.ut.math.tvt.salessystem.logic.WarehouseStock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import static org.junit.Assert.*;

public class WarehouseTests {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);
    InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
    WarehouseStock warehouseStock = new WarehouseStock(dao);

    StockItem item1 = new StockItem(99L, "Chips", "Chips", 11, 1);
    StockItem item2 = new StockItem(54L, "Chips", "Chips", 11, 1);

    @Test
    public void testingAddingNewItemToWarehouse() {
        assertEquals(null, dao.findStockItem(99L));
        try {
            warehouseStock.addItem("Chips", "11", "1", "99L");
        } catch (Exception e){
            fail("Test failed");
        }
        assertEquals(item1, dao.findStockItem(99L));
        log.info("Test");
    }

    @Test
    public void testAddingItemWithNegativePrice() throws NegativePriceException {
        assertEquals(item1, dao.findStockItem(99L));
        log.info("Test");
    }


    // check that methods beginTransaction and commitTransaction are both called exactly once and that order
    @Test
    public void testAddingItemBeginsAndCommitsTransaction() {

    }

    // check that a new item is saved through the DAO
    @Test
    public void testAddingNewItem() {

    }

    // check that adding a new item increases the quantity and the saveStockItem method of the DAO is not called
    @Test
    public void testAddingExistingItem() {

    }

    // check that adding an item with negative quantity results in an exception
    @Test
    public void testAddingItemWithNegativeQuantity() {

    }
}
