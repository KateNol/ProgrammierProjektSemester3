package logic;

public class Map {
    public MapStates[][] map = null;
    private int N = 14;

    public Map() {
        map = new MapStates[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                map[row][col] = WATER;
            }
        }
    }

    /**
     *
     * @param c Coordinate where the state should be returned
     * @return
     */
    public MapStates getState(Coordinate c) {return map[c.getRow()][c.getCol()];}

    /**
     *
     * @param c
     * @param s
     */
    public void setState(Coordinate c, MapStates s) {
        if(c.getRow() < N && c.getRow() >= 0 && c.getCol() < N && c.getCol() >= 0) {
            map[c.getRow()][c.getCol()] = s;
        } else {
            throw new IllegalArgumentException("Coordinate not in range!");
        }
    }

    public int getMapSize() {return N;}
}
