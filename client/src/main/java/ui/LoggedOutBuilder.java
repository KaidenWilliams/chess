package ui;

public class LoggedOutBuilder {

    public static String loginSuccessString =
            """
            Username: 
            """;

    public static String quitString =
            """
            quit
            """;


    public static String helpString =
            """
            register <USERNAME> <PASSWORD> <EMAIL> - to create an account
            login <USERNAME> <PASSWORD> - to get on the chess server
            quit - to exit the application
            help - to get a list of possible commands
            """;


    public static String errorString =
            """
            Error, incorrect arguments provided. Try again, or use command "help" to look at the possible commands.
            """;
}
