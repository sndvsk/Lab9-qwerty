package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class HibernateSalesSystemDAO implements SalesSystemDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public HibernateSalesSystemDAO() {
        // if you get ConnectException/JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory("pos");
        em = emf.createEntityManager();


        //Lisa tooteid andmebaasi, kui andmebaas käivitatakse
        //Praegu persistence.xml faili on seadistuseks "drop-and-create", ehk
        //enne igat uut andmebaasi käima panekut dropitakse vana andmebaasi sisu
        //ära. Käivitudes lisatakse siis need siin uuesti andmebaasi. Kodutöös lõpuks
        //kasutada siis seadistust "create", et andmebaasi sisu ka säiliks.

        //Rakenduse kasutamise ajal käivitatud käske näeb ka pos.log all.

        //StockItemi ID ei genereerita andmebaasi poolt praegu. Seda oleks ilus lasta andmebaasi teha, aga ei ole ilmtingimata
        //vajalik ja võib ka ise ID genereerimise loogika implementeerida.

        saveStockItem(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 50));
        saveStockItem(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        saveStockItem(new StockItem(3L, "Frankfurters", "Beer sauseges", 15.0, 0));
        saveStockItem(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
        saveStockItem(new StockItem(5L, "Pancakes", "sweets", 3, 57));
        saveStockItem(new StockItem(6L, "Tank", "toy", 9999.99, 1));
        saveStockItem(new StockItem(7L, "Christmas calendar", "sweets", 5, 20));
    }

    // TODO implement missing methods

    public void close() {
        em.close();
        emf.close();
    }

    @Override
    public List<StockItem> findStockItems() {
        return em.createQuery("from StockItem", StockItem.class).getResultList();
    }

    @Override
    public StockItem findStockItem(long id) {
        return null;
    }

    @Override
    public Long lastStockItem() {
        return null;
    }

    @Override
    public ArrayList<StockItem> getAllStockItems() {
        return null;
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        //TODO lisada funktsionaalsust.
        beginTransaction();
        em.persist(stockItem);
        commitTransaction();
    }

    @Override
    public void saveSoldItem(SoldItem item) {
    }

    @Override
    public void updateStockItem(StockItem stockItem) {

    }

    @Override
    public void deleteStockItem(StockItem stockItem) {

    }

    @Override
    public StockItem getStockItemByBarcode(String barCode) {
        return null;
    }

    @Override
    public void beginTransaction() {
        em.getTransaction().begin();
    }

    @Override
    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }
}
