package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.NegativeQuantityException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;

import java.util.ArrayList;
import java.util.List;

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
    public void addItem(SoldItem item) throws NegativeQuantityException {
        // TODO In case such stockItem already exists increase the quantity of the existing stock
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception
        boolean itemAdded = false;
        if (items.size() != 0) {
            try {
                for (SoldItem listItem : items) {
                    if (listItem.getId().equals(item.getId())) {
                        item.setQuantity(listItem.getQuantity() + item.getQuantity());
                        if (item.getQuantity() < 0) {
                            log.error("Negative quantity - can't buy more than exists in the warehouse");
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
            } catch (NegativeQuantityException e) {
                System.out.println(e);
            }
        } else {
            items.add(item);
        }
        log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public List<SoldItem> getAll() {
        return items;
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
