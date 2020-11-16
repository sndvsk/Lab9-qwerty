package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final List<SoldItem> soldItemList;
    private final List<Purchase> purchases;

    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        items.add(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        items.add(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        items.add(new StockItem(3L, "Frankfurters", "Beer sauseges", 15.0, 12));
        items.add(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
        this.stockItemList = items;
        this.soldItemList = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    @Override
    public Long lastStockItem() {
        StockItem last = stockItemList.get(stockItemList.size() - 1);
        return last.getId();
    }

    @Override
    public ArrayList<StockItem> getAllStockItems() {
        return new ArrayList<>(stockItemList);
    }

    @Override
    public void saveSoldItem(SoldItem item) {
        soldItemList.add(item);
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    @Override
    public void updateStockItem(StockItem item) {
        stockItemList.set(Math.toIntExact(item.getId() - 1), item);
    }

    @Override
    public void deleteStockItem(StockItem stockItem){

        // Remove item from list without Exceptions
        Iterator<StockItem> iter = stockItemList.iterator();
        while (iter.hasNext()) {
            StockItem item = iter.next();
            if (item.getId() == stockItem.getId()) {
                iter.remove();
            }
        }
    }

    // Search the warehouse for a StockItem with the bar code entered
    // to the barCode textfield.
    public StockItem getStockItemByBarcode(String barCode) {
        try {
            long code = Long.parseLong(barCode);
            return findStockItem(code);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void beginTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void commitTransaction() {
    }

    @Override
    public void savePurchase(Purchase p) {
        purchases.add(p);
    }

    @Override
    public ArrayList<Purchase> getAllPurchases() {
        return new ArrayList<>(purchases);
    }

    @Override
    public ArrayList<Purchase> getLast10Purchases() {
        ArrayList<Purchase> last10 = new ArrayList<>();
        for (int i = purchases.size() - 10; i < purchases.size(); i++) {
            last10.add(purchases.get(i));
        }
        return last10;
    }

    @Override
    public Purchase findPurchaseById(long id) {
        for (Purchase p : purchases) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    @Override
    public Long lastPurchase() {
        return purchases.get(purchases.size() - 1).getId();
    }
}
