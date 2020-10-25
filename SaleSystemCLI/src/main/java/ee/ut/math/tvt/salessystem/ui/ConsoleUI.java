package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.NegativePriceException;
import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import ee.ut.math.tvt.salessystem.dto.Team;
import ee.ut.math.tvt.salessystem.logic.WarehouseStock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A simple CLI (limited functionality).
 */
public class ConsoleUI {
    private static final Logger log = LogManager.getLogger(ConsoleUI.class);

    private final SalesSystemDAO dao;
    private final ShoppingCart cart;
    private WarehouseStock stock;
    private Team team;

    public ConsoleUI(SalesSystemDAO dao) {
        this.dao = dao;
        cart = new ShoppingCart(dao);
        stock = new WarehouseStock(dao);
        team = new Team();
    }

    public static void main(String[] args) throws Exception {
        SalesSystemDAO dao = new InMemorySalesSystemDAO();
        ConsoleUI console = new ConsoleUI(dao);
        console.run();
        log.info("Console initialized.");
    }

    /**
     * Run the sales system CLI.
     */
    public void run() throws IOException, NegativePriceException {
        System.out.println("===========================");
        System.out.println("=       Sales System      =");
        System.out.println("===========================");
        printUsage();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            processCommand(in.readLine().trim().toLowerCase());
            System.out.println("Done. ");
        }
    }

    private void showStock() {
        List<StockItem> stockItems = dao.findStockItems();

        System.out.println("-------------------------");
        for (StockItem si : stock.getAll()) {
            System.out.println(si.getId() + " " + si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (stock.getAll().size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }

    private void addToStock(String indx, String name, String price, String amount) throws NegativePriceException {
        System.out.println("-------------------------");
        stock.addItem(name, price, amount, indx);
        System.out.println("-------------------------");
    }

    private void deleteFromStock(String id) {

        StockItem item = dao.findStockItem(Long.parseLong(id));
        stock.deleteItem(item);

        System.out.println("-------------------------");
        System.out.println("Done");
        System.out.println("-------------------------");
    }

    private void showCart() {
        System.out.println("-------------------------");
        for (SoldItem si : cart.getAll()) {
            System.out.println(si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (cart.getAll().size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }

    private void showTeam() {
        System.out.println("-------------------------");
        System.out.println(team.toString());
        System.out.println("-------------------------");
    }

    private void printUsage() {
        System.out.println("-------------------------");
        System.out.println("Usage:");
        System.out.println("h\t\tShow this help");
        System.out.println("w\t\tShow warehouse contents");
        System.out.println("aw IDX N P Q\tAdd item 'N'name,  to stock");
        System.out.println("d  IDX\tDelete item from stock by index IDX");
        System.out.println("c\t\tShow cart contents");
        System.out.println("a IDX NR \tAdd NR of stock item with index IDX to the cart");
        System.out.println("p\t\tPurchase the shopping cart");
        System.out.println("r\t\tReset the shopping cart");
        System.out.println("t\t\tShow team information");
        System.out.println("-------------------------");
    }

    private void processCommand(String command) throws NegativePriceException {
        String[] c = command.split(" ");

        if (c[0].equals("h"))
            printUsage();
        else if (c[0].equals("q"))
            System.exit(0);
        else if (c[0].equals("w"))
            showStock();
        else if (c[0].equals("aw")  && c.length == 5){
                addToStock(c[1], c[2], c[3], c[4]);
        }

        else if (c[0].equals("d") && c.length==2)
            deleteFromStock( c[1]);
        else if (c[0].equals("c"))
            showCart();
        else if (c[0].equals("p"))
            cart.submitCurrentPurchase();
        else if (c[0].equals("r"))
            cart.cancelCurrentPurchase();
        else if (c[0].equals("a") && c.length == 3) {
            try {
                long idx = Long.parseLong(c[1]);
                int amount = Integer.parseInt(c[2]);
                StockItem item = dao.findStockItem(idx);
                if (item != null) {
                    cart.addItem(new SoldItem(item, Math.min(amount, item.getQuantity())));
                } else {
                    System.out.println("no stock item with id " + idx);
                }
            } catch (SalesSystemException | NoSuchElementException e) {
                log.error(e.getMessage(), e);
            }
        }
        else if (c[0].equals("t"))
            showTeam();
        else {
            System.out.println("unknown command");
        }
    }


}
