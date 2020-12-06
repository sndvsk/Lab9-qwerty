package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.MaxQuantityExceededException;
import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;

import java.util.ArrayList;
import java.util.List;

import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShoppingCart {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);
    private final SalesSystemDAO dao;
    private final List<SoldItem> items = new ArrayList<>();

    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(SoldItem item) throws MaxQuantityExceededException, NegativeQuantityException, NegativePriceException {
        double price = item.getPrice();
        if (price <= 0) {
            throw new NegativePriceException(price);
        }
        checkAndSetItemQuantity(item);

        boolean itemAdded = false;
        if (items.size() != 0) {
            for (SoldItem listItem : items) {
                if (listItem.getId().equals(item.getId())) {
                    item.setQuantity(listItem.getQuantity() + item.getQuantity());
                    if (item.getQuantity() < 0) {
                        throw new NegativeQuantityException(item.getQuantity());
                    }
                    items.remove(listItem);
                    items.add(item);
                    log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
                    itemAdded = true;
                    break;
                }
            }
            if (!itemAdded) {
                items.add(item);
                log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
            }
        } else {
            items.add(item);
        }
        log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public void checkAndSetItemQuantity(SoldItem item) throws MaxQuantityExceededException, NegativeQuantityException {
        StockItem stockItem = dao.findStockItem(item.getId());
        int quantity = item.getQuantity();
        if (quantity > stockItem.getQuantity()) {
            throw new MaxQuantityExceededException(quantity);
        } else if (quantity <= 0) {
            throw new NegativeQuantityException(quantity);
        }
        stockItem.setQuantity(stockItem.getQuantity() - quantity);
    }

    public List<SoldItem> getAll() {
        return items;
    }

    // For testing
    public int getItemQuantity(SoldItem item) {
        return item.getQuantity();
    }

    public double getTotalSum() {
        List<Double> prices = new ArrayList<>();
        double totalSum = 0.0;
        for (int i = 0; i < items.size(); i++) {
            prices.add(items.get(i).getPrice() * items.get(i).getQuantity());
        }
        for (Double sum : prices) {
            totalSum += sum;
        }
        return totalSum;
    }

    public void cancelCurrentPurchase() {
        //Siin peame tagastama stocki koik asjad, mis olid sisestatud, kui hakkame updatetima reaalajas stocki
//        for (SoldItem item : items)
//
//            dao.updateStockItem(item.getStockItem());
//            log.info("Returned " + item.getName() + " to shopping cart.");
//        }
        items.clear();
        log.info("Current purchase cancelled");
    }

    public void submitCurrentPurchase() {
        // TODO decrease quantities of the warehouse stock

        // note the use of transactions. InMemorySalesSystemDAO ignores transactions
        // but when you start using hibernate in lab5, then it will become relevant.
        // what is a transaction? https://stackoverflow.com/q/974596
        dao.beginTransaction();
        log.info("Begin of transaction");
        try {
            for (SoldItem item : items) {
                dao.saveSoldItem(item);
                log.info("Added " + item.getName() + " to shopping cart.");
            }
            dao.commitTransaction();
            items.clear();
        } catch (Exception e) {
            dao.rollbackTransaction();
            throw e;
        }
    }
}
