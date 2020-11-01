package ee.ut.math.tvt.salessystem.ui;

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
import org.junit.*;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class WarehouseTests {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);

    InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
    WarehouseStock warehouseStock = new WarehouseStock(dao);

    @Mock
    SalesSystemDAO daoMock;
    WarehouseStock warehouseMockDao;
    ShoppingCart shoppingCartMock;

    StockItem item1 = new StockItem(99L, "Chips", "Chips", 11.0, 1);
    StockItem item2 = new StockItem(54L, "Chips", "Chips", 11.0, 1);
    StockItem item3 = new StockItem(33L, "Test", "Test", 12.0, 2);
    StockItem item_neg = new StockItem(34L, "Oxygen", "Pure", 12.0, -2);
    StockItem item_neg2 = new StockItem(36L, "Grass", "Green", -1.0, 2);


    @Before
    public void onbeforeTests() {
        openMocks(this);

        this.warehouseMockDao = new WarehouseStock(daoMock);
        this.shoppingCartMock = new ShoppingCart(daoMock);
    }

    @Test
    public void testingAddingNewItemToWarehouse() {
        assertNull(dao.findStockItem(99));
        dao.saveStockItem(item1);
        assertEquals(item1, dao.findStockItem(99));
        log.info("testingAddingNewItemToWarehouse - Test passed");
    }

    @Test (expected = NegativePriceException.class)
    public void testAddingItemWithNegativePrice() throws NegativePriceException, NegativeQuantityException {
        /*try {*/
        warehouseStock.addItem(String.valueOf(item_neg2.getName()),
                String.valueOf(item_neg2.getPrice()),
                String.valueOf(item_neg2.getQuantity()),
                String.valueOf(item_neg2.getId())
        );
        /*
        } catch (NegativeQuantityException | NegativePriceException e) {
            //log.error(e.getMessage());
            log.error("Error: Quantity can't be negative:" + item_neg.getQuantity());
        }*/
        log.info("testAddingItemWithNegativeQuantity - Test passed");

    }


    // check that methods beginTransaction and commitTransaction are both called exactly once and that order
    @Test
    public void testAddingItemBeginsAndCommitsTransaction() {
        //assertEquals();
        SoldItem soldItem = new SoldItem(item1, 5);
        shoppingCartMock.addItem(soldItem);

        shoppingCartMock.submitCurrentPurchase();

        InOrder inOrder = inOrder(daoMock);
        inOrder.verify(daoMock).beginTransaction();
        inOrder.verify(daoMock).commitTransaction();
        inOrder.verifyNoMoreInteractions();
        log.info("testAddingItemBeginsAndCommitsTransaction - Test passed");
    }

    // check that a new item is saved through the DAO
    @Test
    public void testAddingNewItem() throws NegativeQuantityException, NegativePriceException, NullPointerException {
        warehouseStock.addItem(String.valueOf(item3.getName()), String.valueOf(item3.getPrice()), String.valueOf(item3.getQuantity()), String.valueOf(item3.getId()));
        var stockItem = dao.findStockItem(item3.getId());
        assertEquals(item3.getId(), stockItem.getId());
        assertEquals(item3.getName(), stockItem.getName());
        assertEquals(item3.getPrice(), stockItem.getPrice(), 0.001);
        log.info("testAddingNewItem - Test passed");
    }

    // check that adding a new item increases the quantity and the saveStockItem method of the DAO is not called
    @Test
    public void testAddingExistingItem() throws NegativeQuantityException, NegativePriceException {
        var itemT1 = dao.findStockItem(1L);
        int before = itemT1.getQuantity();

        // set up mock invocations and interactions
        Mockito.when(daoMock.findStockItem(1))
                .thenReturn(itemT1);
        Mockito.when(daoMock.getStockItemByBarcode("1"))
                .thenReturn(itemT1);

        doAnswer(invocation -> {
            var stockItem = (StockItem)invocation.getArgument(0);
            // assumption is we update only quantity during 'warehouseMockDao.addItem'
            itemT1.setQuantity(stockItem.getQuantity());
            return null;
        }).when(daoMock).updateStockItem(any());



        warehouseMockDao.addItem("Lays chips", "12", "6", "1");

        int after = dao.findStockItem(1L).getQuantity();
        assertEquals(before + 6, after);

        verify(daoMock, times(0)).saveStockItem(any());
        verify(daoMock, times(1)).updateStockItem(any());
        log.info("testAddingExistingItem - Test passed");

        //https://www.baeldung.com/mockito-series
    }

    // check that adding an item with negative quantity results in an exception
    @Test(expected = NegativeQuantityException.class)
    public void testAddingItemWithNegativeQuantity() throws NegativeQuantityException, NegativePriceException {
        /*try {*/
            warehouseStock.addItem(String.valueOf(item_neg.getName()),
                    String.valueOf(item_neg.getPrice()),
                    String.valueOf(item_neg.getQuantity()),
                    String.valueOf(item_neg.getId())
            );
        /*
        } catch (NegativeQuantityException | NegativePriceException e) {
            //log.error(e.getMessage());
            log.error("Error: Quantity can't be negative:" + item_neg.getQuantity());
        }*/
        log.info("testAddingItemWithNegativeQuantity - Test passed");
    }

}
