package clientlogic;
import ui.EscapeSequences;

import java.util.Scanner;

public class InputHandler {

    private final clientController client;

    public InputHandler(String serverUrl) {
        client = new clientController(serverUrl);
    }

    public void readInput() {

        printIntro();

        Scanner scanner = new Scanner(System.in);
        var output = "";

        while (true) {
            String line = scanner.nextLine();

            output = client.routeInput(line);

            if (!output.isEmpty()) {

                String[] words = output.trim().split("\\s+");
                if (words.length == 1 && words[0].contains("quit")) {
                    break;
                }

                System.out.println(output);
            }
        }

        printOutro();
    }

    private void printIntro() {
        System.out.println("Welcome to chess. Login or register to play the game. For a list of potential commands, type \"help\"");
        System.out.println(EscapeSequences.RESET_TEXT_COLOR);
    }

    private void printOutro() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.println("Thank you for playing. Come again soon ;)");
    }

}
