package ui;

public class ChessGameBuilder {

//     This will make string representation of board
//     class that encapsulates logic for how chess board look

    private static final String[][] board = new String[8][8];

    public static String printBoard(String color) {

        boolean isWhiteView = color.equalsIgnoreCase("white");

        StringBuilder boardString = new StringBuilder(EscapeSequences.ERASE_SCREEN);

        // Initialize the board with empty squares
        initializeBoard();

        // Place the pieces on the board
        placePieces(isWhiteView);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String squareColor;
                if (isWhiteView) {
                    squareColor = ((row + col) % 2 == 0) ? EscapeSequences.LIGHT_SQUARE_COLOR : EscapeSequences.DARK_SQUARE_COLOR;
                }
                else {
                    squareColor = ((row + col) % 2 == 0) ? EscapeSequences.DARK_SQUARE_COLOR : EscapeSequences.LIGHT_SQUARE_COLOR;
                }
                boardString.append(squareColor)
                        .append(board[row][col])
                        .append(EscapeSequences.RESET_COLOR);
            }
            boardString.append("\n"); // Add a newline character after each row
        }

        return boardString.toString();
    }

    private static void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = EscapeSequences.EMPTY;
            }
        }
    }

    private static void placePieces(boolean isWhiteView) {
        if (isWhiteView) {
            placePiecesFromWhiteView();
        } else {
            placePiecesFromBlackView();
        }
    }

    private static void placePiecesFromWhiteView() {
        board[0][0] = EscapeSequences.WHITE_ROOK;
        board[0][1] = EscapeSequences.WHITE_KNIGHT;
        board[0][2] = EscapeSequences.WHITE_BISHOP;
        board[0][3] = EscapeSequences.WHITE_QUEEN;
        board[0][4] = EscapeSequences.WHITE_KING;
        board[0][5] = EscapeSequences.WHITE_BISHOP;
        board[0][6] = EscapeSequences.WHITE_KNIGHT;
        board[0][7] = EscapeSequences.WHITE_ROOK;

        board[7][0] = EscapeSequences.BLACK_ROOK;
        board[7][1] = EscapeSequences.BLACK_KNIGHT;
        board[7][2] = EscapeSequences.BLACK_BISHOP;
        board[7][3] = EscapeSequences.BLACK_QUEEN;
        board[7][4] = EscapeSequences.BLACK_KING;
        board[7][5] = EscapeSequences.BLACK_BISHOP;
        board[7][6] = EscapeSequences.BLACK_KNIGHT;
        board[7][7] = EscapeSequences.BLACK_ROOK;

        for (int col = 0; col < 8; col++) {
            board[1][col] = EscapeSequences.WHITE_PAWN;
            board[6][col] = EscapeSequences.BLACK_PAWN;
        }
    }

    private static void placePiecesFromBlackView() {
        board[7][0] = EscapeSequences.WHITE_ROOK;
        board[7][1] = EscapeSequences.WHITE_KNIGHT;
        board[7][2] = EscapeSequences.WHITE_BISHOP;
        board[7][3] = EscapeSequences.WHITE_QUEEN;
        board[7][4] = EscapeSequences.WHITE_KING;
        board[7][5] = EscapeSequences.WHITE_BISHOP;
        board[7][6] = EscapeSequences.WHITE_KNIGHT;
        board[7][7] = EscapeSequences.WHITE_ROOK;

        board[0][0] = EscapeSequences.BLACK_ROOK;
        board[0][1] = EscapeSequences.BLACK_KNIGHT;
        board[0][2] = EscapeSequences.BLACK_BISHOP;
        board[0][3] = EscapeSequences.BLACK_QUEEN;
        board[0][4] = EscapeSequences.BLACK_KING;
        board[0][5] = EscapeSequences.BLACK_BISHOP;
        board[0][6] = EscapeSequences.BLACK_KNIGHT;
        board[0][7] = EscapeSequences.BLACK_ROOK;

        for (int col = 0; col < 8; col++) {
            board[6][col] = EscapeSequences.WHITE_PAWN;
            board[1][col] = EscapeSequences.BLACK_PAWN;
        }
    }






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


}
