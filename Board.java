import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int SIZE = 10;
    private final char[] ROWS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private final Map<String, Cell> cellMap;

    public Board() {
        cellMap = new HashMap<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String position = getPosition(row, col);
                cellMap.put(position, new Cell());
            }
        }
    }

public void placeShip(Ship ship, String startPosition, String direction)
        throws InvalidPositionException, InvalidPlacementException, InvalidShipTypeException {
    if (ship == null) {
        throw new InvalidShipTypeException("Invalid ship type.");
    }

    int row = getRowFromPosition(startPosition);
    int col = getColFromPosition(startPosition);

    if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
        throw new InvalidPositionException("Invalid position.");
    }

    if (!direction.equalsIgnoreCase("across") && !direction.equalsIgnoreCase("down")) {
        throw new InvalidPlacementException("Invalid ship placement direction.");
    }

    int length = ship.length();
    int endRow = row;
    int endCol = col;

    if (direction.equalsIgnoreCase("across")) {
        endCol += length - 1;
    } else {
        endRow += length - 1;
    }

    if (endRow >= SIZE || endCol >= SIZE) {
        throw new InvalidPlacementException("Ship placement is out of bounds.");
    }

    for (int i = row; i <= endRow; i++) {
        for (int j = col; j <= endCol; j++) {
            String position = getPosition(i, j);

            // Check if the position is valid
            if (!cellMap.containsKey(position)) {
                throw new InvalidPositionException("Non-existent position.");
            }

            Cell cell = cellMap.get(position);
            if (cell.isOccupied()) {
                throw new InvalidPlacementException("Ship overlaps with another ship.");
            }
        }
    }

    int segmentIndex = 0; // Index to access ship's segments
    for (int i = row; i <= endRow; i++) {
        for (int j = col; j <= endCol; j++) {
            String position = getPosition(i, j);
            Cell cell = cellMap.get(position);
            if (cell == null) {
                throw new InvalidPositionException("Cell does not exist.");
            }
            if (cell.isOccupied()) {
                throw new InvalidPlacementException("Ship overlaps with another ship.");
            }
            Segment segment = ship.getSegments().get(segmentIndex); // Use the ship's segments
            segment.setRow(i); // Set the row coordinate
            segment.setColumn(j); // Set the column coordinate
            cell.placeSegment(segment); // Add the ship's segment to the cell
            segmentIndex++;
        }
    }

}






    public void attack(String position) throws InvalidPositionException {
        int row = getRowFromPosition(position);
        int col = getColFromPosition(position);

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new InvalidPositionException("Invalid position.");
        }

        String cellPosition = getPosition(row, col);
        if (!cellMap.containsKey(cellPosition)) {
            throw new InvalidPositionException("Cell does not exist.");
        }

        Cell cell = cellMap.get(cellPosition);
        cell.markAsHit(); // Mark the cell as hit

        if (cell.isOccupied()) {
            cell.getShip().attack(); // Attack the ship segment

            // Check if the ship is sunken
            if (cell.getShip().sunk()) {
                // Mark all the ship's segments as sunken
                for (Segment segment : cell.getShip().getSegments()) {
                    segment.attack();
                }
                setSunkenShipSegments(cell.getShip()); // Set the sunken ship segments in the board
            }
        }
    }

    private void setSunkenShipSegments(Ship ship) {
        for (Segment segment : ship.getSegments()) {
            Cell cell = findCellWithSegment(segment);
            if (cell != null) {
                cell.setSunkenShipSegment(true);
            }
        }
    }

    private Cell findCellWithSegment(Segment segment) {
        for (Cell cell : cellMap.values()) {
            if (cell.getShip() != null && cell.getShip().equals(segment.getShip())) {
                return cell;
            }
        }
        return null;
    }

    public boolean hasBeenHit(String position) throws InvalidPositionException {
        int row = getRowFromPosition(position);
        int col = getColFromPosition(position);

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new InvalidPositionException("Invalid position.");
        }

        String cellPosition = getPosition(row, col);
        if (!cellMap.containsKey(cellPosition)) {
            throw new InvalidPositionException("Cell does not exist.");
        }

        Cell cell = cellMap.get(cellPosition);
        return cell.hasBeenHit();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  1 2 3 4 5 6 7 8 9 10\n");
        char row = 'A';
        for (int i = 0; i < SIZE; i++) {
            sb.append(row).append("");
            for (int j = 0; j < SIZE; j++) {
                String position = getPosition(i, j);
                Cell cell = cellMap.get(position);
                if (cell.hasBeenHit()) {
                    if (cell.isOccupied()) {
                        if (cell.getShip().sunk()) {
                            sb.append(" S"); // Display 'S' for a sunken ship segment
                        } else {
                            sb.append(" X"); // Display 'X' for a hit ship segment
                        }
                    } else {
                        sb.append(" O"); // Display 'O' for a miss
                    }
                } else {
                    sb.append(" ."); // Display '.' for unhit cells
                }
            }
            sb.append("\n");
            row++;
        }
        return sb.toString();
    }

    public String displaySetup() {
        StringBuilder sb = new StringBuilder();
        sb.append("  1 2 3 4 5 6 7 8 9 10\n");
        for (int row = 0; row < SIZE; row++) {
            sb.append(ROWS[row]);
            for (int col = 0; col < SIZE; col++) {
                String position = getPosition(row, col);
                Cell cell = cellMap.get(position);
                if (cell.hasBeenHit() && cell.isOccupied()) {
                    if (cell.getShip().sunk()) {
                        sb.append(" ").append(cell.getShip().toString()); // Display the ship type for a sunken ship segment
                    } else {
                        sb.append(" X"); // Display 'X' for a hit ship segment
                    }
                } else if (cell.hasBeenHit() && !cell.isOccupied()) {
                    sb.append(" O"); // Display 'O' for a miss
                } else {
                    sb.append(" ").append(cell.displaySetup());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int getRowFromPosition(String position) throws InvalidPositionException {
        if (position == null || position.isEmpty()) {
            throw new InvalidPositionException("Position is ill-formatted.");
        }

        char rowChar = position.charAt(0);
        return Character.toUpperCase(rowChar) - 'A';
    }

    private int getColFromPosition(String position) throws InvalidPositionException {
        if (position == null || position.length() < 2) {
            throw new InvalidPositionException("Position is ill-formatted.");
        }

        try {
            return Integer.parseInt(position.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidPositionException("Position is ill-formatted.");
        }
    }

    private String getPosition(int row, int col) {
        return String.valueOf(ROWS[row]) + (col + 1);
    }
}