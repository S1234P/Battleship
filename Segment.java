public class Segment {
    private final Ship ship;
    private boolean hit;
    private int row;
    private int column;

    public Segment(Ship ship) {
        this.ship = ship;
        this.hit = false;
        this.row = -1; // Default value indicating unset row
        this.column = -1; // Default value indicating unset column
    }

    public boolean hit() {
        return this.hit;
    }

    public void attack() {
        this.hit = true;
    }

    public Ship getShip() {
        return this.ship;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return this.ship == null ? "?" : this.getShip().toString();
    }
}
