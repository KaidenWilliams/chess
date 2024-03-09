import server.Server;

public class Main {
    public static void main(String[] args) {

        // Todo start up server here
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }
            new Server().run(port);
            System.out.printf("Server started on port %d%n", port);
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

    }
}