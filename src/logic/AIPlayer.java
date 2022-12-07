package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.io.*;
import java.util.*;

import static logic.Util.log_debug;

public class AIPlayer extends NetworkPlayer {

    private final Set<Coordinate> previousShots = new HashSet<>();

    public AIPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
    }

    public AIPlayer(int semester) {
        super(new PlayerConfig("ai player"));
        try {
            HashMap<Integer, String> semesterToName = new HashMap<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/logic/AI.txt"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] split = line.split("::");
                String name = split[0];
                int readSemester = Integer.parseInt(split[1]);
                semesterToName.put(readSemester, name);
                line = bufferedReader.readLine();
            }

            setUsername(semesterToName.get(semester));
            setMaxSemester(semester);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setShips() {
        Random coord = new Random();
        ArrayList<Ship> ships = getArrayListShips();

        for (Ship ship : ships) {
            boolean placed = true;
            do {
                int x = coord.nextInt(myMap.getMapSize() - 1);
                int y = coord.nextInt(myMap.getMapSize() - 1);
                Coordinate coordinate = new Coordinate(x, y);
                Alignment alignment = coord.nextBoolean() ? Alignment.VERT_DOWN : Alignment.HOR_RIGHT;
                try {
                    placed = addShip(ship, coordinate, alignment);
                } catch (IllegalArgumentException e) {
                    placed = false;
                }
                if (placed) {
                    log_debug("successfully placed ship at " + coordinate + " aligned " + alignment);
                }
            } while (!placed);

        }
    }

    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        System.out.println("our turn! state of the game:");
        printBothMaps();
        Random random = new Random();

        Coordinate shot = new Coordinate(random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())));

        while (previousShots.contains(shot)) {
            shot = new Coordinate(random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())));
        }

        previousShots.add(shot);

        return shot;
    }
}
