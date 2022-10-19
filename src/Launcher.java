import java.util.Scanner;


/**
 * main entry point to program
 * - asks the user what type of player he wants to be (normal (gui) or ai)
 * TODO: replace with gui
 */
public class Launcher {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String in;

        System.out.println("Would you like to play as Human or as Computer? (h/c)");
        in = scanner.nextLine().toLowerCase().strip();
        PlayerMode playerMode = in.equals("h") ? PlayerMode.Human : PlayerMode.Computer;

        System.out.println("Would you like to play via network? (y/n)");
        in = scanner.nextLine().toLowerCase().strip();
        NetworkMode networkMode;
        if (in.equals("n")) {
            System.out.println("Would you like to play vs another Human or a Computer? (h/c)");
            in = scanner.nextLine().toLowerCase().strip();
            if (in.equals("h")) {
                networkMode = NetworkMode.OfflineHuman;
            } else {
                networkMode = NetworkMode.OfflineAI;
            }

        } else {
            networkMode = NetworkMode.Online;
        }


        Player me = null;
        Player enemy = null;

        if (playerMode == PlayerMode.Human) {
            me = new ConsolePlayer();
        } else {
            System.err.println("playing as AI not supported yet");
            System.exit(1);
        }

        if (networkMode == NetworkMode.Online) {
            System.err.println("playing via network not supported yet");
            System.exit(1);
        } else if (networkMode == NetworkMode.OfflineAI) {
            System.err.println("playing vs AI not supported yet");
            System.exit(1);
        } else if (networkMode == NetworkMode.OfflineHuman) {
            enemy = new ConsolePlayer();
        }

        Logic logic = new Logic(me, enemy);
    }
}
