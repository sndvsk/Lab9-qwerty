package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import org.junit.*;
import static org.junit.Assert.*;

public class WarehouseTests {
    InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
    StockItem item1 = new StockItem(99L, "Chips", "Chips", 11, 1);
    StockItem item2 = new StockItem(54L, "Chips", "Chips", 11, 1);

    @Test
    public void testingAddingNewItemToWarehouse() {
        assertEquals(null, dao.findStockItem(99));
        dao.saveStockItem(item1);
        assertEquals(item1, dao.findStockItem(99));
    }

/*    @Test (expected = NegativePriceException)
    public void testAddingItemWithNegativePrice() throws NegativePriceException{

    }*/
}
