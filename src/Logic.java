public class Logic {

    int semester;

    int[][] playerShipMatrix;
    int[][] playerTargetMatrix;

    public Logic(Player player, Player enemy) {
        int playerSemester = player.getSemester();
        int enemySemester = enemy.getSemester();

        if (playerSemester != enemySemester) {
            System.err.println("Semesters not matching");
            System.exit(2);
        }

        semester = playerSemester;
        int size = 13 + semester;


        playerShipMatrix = new int[size][size];
        playerTargetMatrix = new int[size][size];

        player.placeShips(playerShipMatrix);


    }

}
