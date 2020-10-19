package ee.ut.math.tvt.salessystem;

public class NegativePriceException extends Exception {
    int Price;

    public NegativePriceException(int price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "Price can't be negative" + Price;
    }
}
