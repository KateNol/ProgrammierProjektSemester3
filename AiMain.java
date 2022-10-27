import java.util.*;
import java.awt.Point;
public class AiMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Array Größe wählen: ");
        int size = scanner.nextInt();
        int[][] board = new int[size][size];
        AI ai = new AI(board);
        while(true) {
            System.out.print("Drücke 1 zum schießen: ");
            int control = scanner.nextInt();
            if (control == 1) {
                Point test = ai.shoot();
                System.out.println("X-Koordinate: " + test.x);
                System.out.println("Y-Koordinate: " + test.y);

            } else {
                break;
            }
        }

    }
}
