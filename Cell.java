public class Cell {
    private Segment segment;
    private boolean hit;
    private boolean sunkenShipSegment;

    public Cell() {
        this.segment = null;
        this.hit = false;
        this.sunkenShipSegment = false;
    }

    public boolean hasBeenHit() {
        return this.hit;
    }

    public void attack() {
        if (this.segment != null) {
            this.segment.attack();
        }
        this.hit = true;
    }

    public boolean isOccupied() {
        return this.segment != null;
    }

    public void placeSegment(Segment segment) {
        if (!this.isOccupied()) {
            this.segment = segment;
        }
    }

    public boolean isSunkenShipSegment() {
        return sunkenShipSegment;
    }

    public void setSunkenShipSegment(boolean sunkenShipSegment) {
        this.sunkenShipSegment = sunkenShipSegment;
    }

@Override
public String toString() {
    if (!this.hit) {
        return ".";
    } else {
        if (!this.isOccupied()) {
            return "O";
        } else if (this.segment.hit()) {
            return "X"; // Display 'X' for a hit ship segment
        } else {
            return ".";
        }
    }
}





    public String displaySetup() {
        return this.isOccupied() ? this.segment.toString() : ".";
    }

    public Ship getShip() {
        return this.segment != null ? this.segment.getShip() : null;
    }

    public void markAsHit() {
        this.hit = true;
    }
}