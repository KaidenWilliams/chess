package ui;

public class LoggedOutBuilder {

    public static String registerString =
            """
            Successfully registered and logged in to your account, "%s"
            You are currently logged in
            """;

    public static String loginString =
            """
            Successfully logged in your account, "%s"
            You are currently logged in
            """;

    public static String quitString =
            """
            quit
            """;


    public static String helpString =
            """
            Commands:
            < register <USERNAME> <PASSWORD> <EMAIL> -- to create an account
            < login <USERNAME> <PASSWORD> -- to get on the chess server
            < quit -- to exit the application
            < help -- to get a list of possible commands
            """;

    public static String defaultString =
            """
            Your input was invalid. The following valid commands are shown below
            
            Commands:
            < register <USERNAME> <PASSWORD> <EMAIL> -- to create an account
            < login <USERNAME> <PASSWORD> -- to get on the chess server
            < quit -- to exit the application
            < help -- to get a list of possible commands
            """;


    public static String getRegisterString(String username) {
        return String.format(registerString, username);
    }

    public static String getLoginString(String username) {
        return String.format(loginString, username);
    }

}
