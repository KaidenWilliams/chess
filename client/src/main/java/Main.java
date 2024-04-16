import clientlogic.InputHandler;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        try {
            new InputHandler(serverUrl).readInput();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}