package cinema;

import java.util.ArrayList;
import java.util.List;

public class Hall {
    private final int rows;
    private final int columns;
    private final List<Seat> seats = new ArrayList<>();

    public Hall() {
        this(9, 9);
    }

    public Hall(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Seat getSeat(int row, int col) {
        return seats.stream().filter(
                s -> s.getRow() == row && s.getColumn() == col
        ).findFirst().orElse(null);
    }
}
