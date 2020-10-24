package ee.ut.math.tvt.salessystem;

public class NegativePriceException extends Exception {
    double Price;

    public NegativePriceException(double price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "Price can't be negative" + Price;
    }
}
