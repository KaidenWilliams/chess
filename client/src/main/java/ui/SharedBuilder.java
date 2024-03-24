package ui;

public class SharedBuilder {

    public static String errorStringSyntax =
            """
            Syntax Error, incorrect syntax provided for the command: \"%s\". Try again, or type "help" for a syntax refresher.
            """;

    public static String errorStringRequest =
            """
            %s. Request error for the command: \"%s\". Please try again.
            """;


    public static String getErrorStringSyntax(String command) {
        return String.format(errorStringSyntax, command);
    }

    public static String getErrorStringRequest(String errorMessage, String command) {
        return String.format(errorStringRequest, command, errorMessage);
    }
}
