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

    public void addItem(StockItem item) {
        // TODO In case such stockItem already exists increase the quantity of the existing stock
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception

        dao.saveStockItem(item);
        //log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public List<StockItem> getAll() {
        return stockItems;
    }


}
