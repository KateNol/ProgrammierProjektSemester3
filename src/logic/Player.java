package logic;
public class Player {
    int maxLevel = 1;
    String name = null;
    Ship[] ships = null;

    public Player(String name) {
        this.name = name;
        //Initialize shipsizes for Level 1 fixed
        int[] shipSizes = {2,2,2,2,4,6};
        for (int i = 0; i < shipSizes.length; i++) {
            ships[i] = new Ship(shipSizes[i]);
        }
    }

    public void setShip(int shipNumber, Coordinate c, Alignment a) {
        if(shipNumber < 6 && shipNumber >= 0) {
            ships[shipNumber].setPos(c,a);
        }
    }
}
