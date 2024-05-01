package cinema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"state"})
public class Seat {
    private final int row;
    private final int column;
    private final int price;
    private PurchaseState state;

    public Seat(int row, int column, int price, PurchaseState state) {
        this.row = row;
        this.column = column;
        this.price = price;
        this.state = state;
    }

    public Seat(int row, int column, int price) {
        this(row, column, price, PurchaseState.Free);
    }

    public Seat(int row, int column) {
        this(row, column, (row <= 4) ? 10 : 8);
    }

    public Seat() {
        this(0, 0);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }

    public void setState(PurchaseState state) {
        this.state = state;
    }

    public PurchaseState getState() {
        return state;
    }
}
