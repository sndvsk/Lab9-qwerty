package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

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

        double price = Double.parseDouble(priceS);
        int quantity = Integer.parseInt(quantityS);


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

        if (Strings.isEmpty(barCodeS)) {
            StockItem newItem = new StockItem(generateBarcode(), name, "description", price, quantity);
            dao.saveStockItem(newItem);
            log.debug("Added " + newItem.getName() + " quantity of " + newItem.getQuantity());
        } else {
            long barCode = Long.parseLong(barCodeS);
            var existingItem = dao.getStockItemByBarcode(barCodeS);

            if (existingItem != null) {
                StockItem newItem = new StockItem(barCode, name,
                        existingItem.getDescription(), price, quantity + existingItem.getQuantity());
                updateItem(newItem);
                log.debug("Updated {} quantity from {} to {}", newItem.getName(),
                        existingItem.getQuantity(), newItem.getQuantity());
            } else {
                StockItem newItemWitBarcode = new StockItem(barCode, name, "description", price, quantity);
                dao.saveStockItem(newItemWitBarcode);
                log.debug("Added item with existing barcode {} quantity {}",  barCodeS, quantityS);
            }
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

    public List<StockItem> getAll() {
        stockItems.addAll(dao.findStockItems());
        return stockItems;
    }

    // SE-15 barCodeField automatically generated
    private Long generateBarcode() {
        log.info("Generating barcode");
        Long last = dao.lastStockItem();
        return last + 1;
    }


}
