package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import java.util.ArrayList;
import java.util.List;

public class WarehouseStock {

    private final SalesSystemDAO dao;
    private final List<StockItem> stockItems = new ArrayList<>();

    public WarehouseStock(SalesSystemDAO dao) {
        this.dao = dao;
    }

    // Add completely new StockItem
    public void addItem(StockItem item) {
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception

        Long id = item.getId();
        dao.saveStockItem(item);
        //log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    // If stockItem already exists don't save it as a new item,
    // Currently updates all fields
    public void updateItem(StockItem item) {
        Long id = item.getId();
        if (dao.findStockItem(id).getId().equals(item.getId())) {
            dao.updateStockItem(item);
        }
    }

    public void deleteItem(StockItem item){
        dao.deleteStockItem(item);
    }

    public List<StockItem> getAll() {
        return stockItems;
    }


}
