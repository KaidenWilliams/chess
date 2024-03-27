import clientlogic.InputHandler;
import ui.ChessGameBuilder;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }


        String bothBoards = ChessGameBuilder.printBoard("white") + "\n\n" + ChessGameBuilder.printBoard("black");
        System.out.println(bothBoards);

       new InputHandler(serverUrl).ReadInput();

    }
}