package ee.ut.math.tvt.salessystem;

public class NegativeQuantityException {
    int Quantity;

    public NegativeQuantityException(int quantity) {
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "Quantity can't be negative: " + Quantity;
    }
}
