public class Runner {
    public static void main(String[] args) {
        Board board = new Board();

        // Place a battleship starting at position A1, going down
        try {
            Ship battleship = Ship.createShip("Battleship");
            board.placeShip(battleship, "A1", "down");
        } catch (InvalidPositionException | InvalidPlacementException | InvalidShipTypeException e) {
            e.printStackTrace();
        }

        // Place a destroyer starting at position D4, going across
        try {
            Ship destroyer = Ship.createShip("Destroyer");
            board.placeShip(destroyer, "G3", "across");
        } catch (InvalidPositionException | InvalidPlacementException | InvalidShipTypeException e) {
            e.printStackTrace();
        }

        // Attack positions A1 and A2
        try {
            board.attack("A1");
            board.attack("A2");               
            board.attack("B1");
            board.attack("G3");
            board.attack("G4"); 
            board.attack("G5");   
            board.attack("G6");                                   
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        // Display the current state of the board
        System.out.println(board.toString());
    }
}