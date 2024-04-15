package clientlogic.websocket;

import ui.SharedBuilder;
import ui.EscapeSequences;

public class WebSocketPrinter {

    // TODO make this another color, escape characters, all sorts of nice stuff
    public static void printNotification(String input) {
        System.out.println(SharedBuilder.setStringColor(EscapeSequences.SET_TEXT_COLOR_YELLOW, input + "\n"));
    }

    public static void printError(String input) {
        System.out.println(SharedBuilder.setStringColor(EscapeSequences.SET_TEXT_COLOR_RED, input + "\n"));
    }

    public static void printGame(String input) {
        System.out.println(input);
    }


}
