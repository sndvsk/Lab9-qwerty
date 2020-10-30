package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WarehouseStock {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);
    private final SalesSystemDAO dao;
    private final List<StockItem> stockItems = new ArrayList<>();

    public WarehouseStock(SalesSystemDAO dao) {
        this.dao = dao;
    }

    // Add completely new StockItem
    public void addItem(String name, String priceS, String quantityS, String barCodeS) throws NumberFormatException, NegativePriceException, NegativeQuantityException {
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception
        double price = 0;
        int quantity = 0;

        price = Double.parseDouble(priceS);
        quantity = Integer.parseInt(quantityS);


        // Check if quantity or price is negative
        if (price < 0) {
            throw new NegativePriceException(price);
        }
        if (quantity < 0) {
             throw new NegativeQuantityException(quantity);
        }
        // TODO check exceptions by find
        // TODO check if bar code and name
        // Try to add or update the product

        if (barCodeS.equals("") || barCodeS == null) {
            StockItem newItem = new StockItem(generateBarcode(), name, "description", price, quantity);
            dao.saveStockItem(newItem);
            log.debug("Added " + newItem.getName() + " quantity of " + newItem.getQuantity());
        } else {
            long barCode = Long.parseLong(barCodeS);
            StockItem newItem = new StockItem(barCode, name, dao.getStockItemByBarcode(barCodeS).getDescription(), price, quantity);
            updateItem(newItem);
            log.debug("Updated " + newItem.getName() + " quantity of " + newItem.getQuantity());
        }
    }

    // If stockItem already exists don't save it as a new item,
    // Currently updates all fields
    public void updateItem(StockItem item) {
        Long id = item.getId();
        if (dao.findStockItem(id).getId().equals(item.getId())) {
            dao.updateStockItem(item);
            log.info("Updated item " + item.getName() + " with id " + item.getId());
        }
    }

    public void deleteItem(StockItem item){
        dao.deleteStockItem(item);
        log.info("Deleted product: " + item.getName());
    }
    public void deleteProduct(){

    }

    public List<StockItem> getAll() {
        return stockItems;
    }

    // SE-15 barCodeField automatically generated
    private Long generateBarcode() {
        log.info("Generating barcode");
        Long last = dao.lastStockItem();
        return last + 1;
    }


}
