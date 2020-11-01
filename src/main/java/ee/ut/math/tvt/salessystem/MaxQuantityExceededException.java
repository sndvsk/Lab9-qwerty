package ee.ut.math.tvt.salessystem;

public class MaxQuantityExceededException extends Exception {
    int Quantity;

    public MaxQuantityExceededException(int quantity) {
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "Quantity can't be negative: " + Quantity;
    }

}
