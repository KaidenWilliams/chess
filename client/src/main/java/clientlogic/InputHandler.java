package clientlogic;
import exceptionclient.ClientException;

import java.util.Scanner;

public class InputHandler {

    // Has scanner, handles input
    // Constantly taking input, passes it to client
    // Initialized Client
    // Kills/stops looping upon recieving Quit message

    // Calls client
//
//

    private final ClientController client;

    public InputHandler(String serverUrl) {
        client = new ClientController(serverUrl);
    }

    public void ReadInput() {

        printIntro();

        Scanner scanner = new Scanner(System.in);
        var output = "";

        while (true) {
            String line = scanner.nextLine();

            output = client.routeInput(line);
            if (output.equals("quit\n")) {
                break;
            }

            System.out.println(output);
        }

        printOutro();
    }

    private void printIntro() {
        System.out.println("Welcome to chess. Login or register to play the game. For a list of potential commands, type \"help\"");
        System.out.println();
    }

    private void printOutro() {
        System.out.println();
        System.out.println("Thank you for playing. Come again soon ;)");
    }




//
//    public void run() {
//        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
//        System.out.print(client.help());
//
//        Scanner scanner = new Scanner(System.in);
//        var result = "";
//        while (!result.equals("quit")) {
//            printPrompt();
//            String line = scanner.nextLine();
//
//            try {
//                result = client.eval(line);
//                System.out.print(BLUE + result);
//            } catch (Throwable e) {
//                var msg = e.toString();
//                System.out.print(msg);
//            }
//        }
//        System.out.println();
//    }

}
