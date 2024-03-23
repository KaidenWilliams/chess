package ClientLogic;

import java.util.Scanner;
import model.DataAccessException;

public class InputHandler {

    // Has scanner, handles input
    // Constantly taking input, passes it to client
    // Initialized Client
    // Kills/stops looping upon recieving Quit message

    // Calls client
//
//

    private final ChessClient client;

    public InputHandler(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void ReadInput() {

        Scanner scanner = new Scanner(System.in);
        var output = "";

        while (!output.equals("quit")) {
            String line = scanner.nextLine();

            try {
                output = client.takeInput(line);
                System.out.println(output);
            } catch (DataAccessException e) {
                var msg = e.toString();
                System.out.println(msg);
            }
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
