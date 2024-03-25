package ui;

public class ChessGameBuilder {

//     This will make string representation of board
//     class that encapsulates logic for how chess board look


    public static String boardWhite =
            """
            """;


    public static String boardBlack =
            """
            """;


    public static String exitString =
            """
            Leaving the Chess Game.
            You are in the main menu.
            """;

    public static String helpString =
            """
            Commands:
            < exit -- to leave the Chess Game
            < help -- to get a list of possible commands
            """;



    public static String getBothBoards() {
        String combinedBoards = boardWhite + "\n" + boardBlack;
        return combinedBoards;
    }

}
