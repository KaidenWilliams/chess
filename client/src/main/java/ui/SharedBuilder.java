package ui;

public class SharedBuilder {

    public static String setStringColor(String escape, String content){
        return escape + content;
    }

    public static String errorStringSyntax =
            """
            Syntax Error for the following command: "%s". Try again, or type "help" for a syntax refresher.
            """;

    public static String errorStringRequest =
            """
            Error for the following command: "%s". Error: "%s". Please try again.
            """;


    public static String getErrorStringSyntax(String command) {
        return setStringColor(EscapeSequences.SET_TEXT_COLOR_RED, String.format(errorStringSyntax, command));
    }

    public static String getErrorStringRequest(String errorMessage, String command) {
        return setStringColor(EscapeSequences.SET_TEXT_COLOR_RED, String.format(errorStringRequest, command, errorMessage));
    }



}
