package ee.ut.math.tvt.salessystem.dataobjects;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
@Table(name = "SOLDITEM")
public class SoldItem extends Item{

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    //@JoinColumn(name = "id")
    @JoinColumn(name = "stock_item_id")
    private StockItem stockItem;

    public SoldItem() {
    }

    public SoldItem(StockItem stockItem, int quantity) {
        this.id = stockItem.getId();
        this.stockItem = stockItem;
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return price * ((double) quantity);
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    @Override
    public String toString() {
        return String.format("SoldItem{id=%d, name='%s'}", id, name);
    }
}
