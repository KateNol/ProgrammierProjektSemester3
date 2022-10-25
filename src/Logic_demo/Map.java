package Logic_demo;

public class Map {
    public MapState[][] map = null;
    private int N = 14;

    /**
     * the map should initialise with WATER entirely
     */
    public Map() {
        map = new MapState[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                map[row][col] = MapState.WATER;
            }
        }
    }

    /**
     *
     * @param c Coordinate
     * @return value of the map on this coordinate
     */
    public MapState getState(Coordinate c) {return map[c.getRow()][c.getCol()];}

    /**
     *
     * @param c coordinate the value should change
     * @param s new value
     */
    public void setState(Coordinate c, MapState s) {
        if(c.getRow() < N && c.getRow() >= 0 && c.getCol() < N && c.getCol() >= 0) {
            map[c.getRow()][c.getCol()] = s;
        } else {
            throw new IllegalArgumentException("Coordinate not in range!");
        }
    }

    public int getMapSize() {return N;}
}
