package logic;

public class Map {
    public MapState[][] map = null;
    private int boardSize = 14;

    /**
     * the map should initialise with WATER entirely
     */
    public Map(int boardSize) {
        map = new MapState[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                map[row][col] = MapState.WATER;
            }
        }
    }

    /**
     *
     * @param c Coordinate
     * @return value of the map on this coordinate
     */
    public MapState getState(Coordinate c) {return map[c.row()][c.col()];}
    /**
     *
     * @param c coordinate the value should change
     * @param s new value
     */
    public void setState(Coordinate c, MapState s) {
        if(c.row() < boardSize && c.row() >= 0 && c.col() < boardSize && c.col() >= 0) {
            map[c.row()][c.col()] = s;
        } else {
            throw new IllegalArgumentException("Coordinate not in range!");
        }
    }

    public int getMapSize() {return boardSize;}
}
