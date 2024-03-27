package ui;

public class ChessGameBuilder {

//     This will make string representation of board
//     class that encapsulates logic for how chess board look

    private static final String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[][] board = new String[8][8];

    public static String printBoard(String color) {

        boolean isWhiteView = color.equalsIgnoreCase("white");

        initializeBoard();
        placePieces(isWhiteView);

        return printBoardString(isWhiteView);
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


    private static String printBoardString(boolean isWhiteView) {

        StringBuilder boardString = new StringBuilder(EscapeSequences.ERASE_SCREEN);
        // Print column labels
        boardString.append(EscapeSequences.RESET_BG_COLOR).append("\n").append(" ");;
        if (isWhiteView) {
            for (String column : columns) {
                boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE).append(" ").append(column).append("  ");
            }
        } else {
            for (int i = columns.length - 1; i >= 0; i--) {
                boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE).append(" ").append(columns[i]).append("  ");
            }
        }
        boardString.append(EscapeSequences.RESET_BG_COLOR).append("\n");

        for (int row = 0; row < 8; row++) {
            // Print row label
            int rowNumber = isWhiteView ? 8 - row : row + 1;
            boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE).append(rowNumber).append(" "); // add row label

            for (int col = 0; col < 8; col++) {
                String squareColor;
                if (isWhiteView) {
                    squareColor = ((row + col) % 2 == 0) ? EscapeSequences.LIGHT_SQUARE_COLOR : EscapeSequences.DARK_SQUARE_COLOR;
                } else {
                    squareColor = ((row + col) % 2 == 0) ? EscapeSequences.DARK_SQUARE_COLOR : EscapeSequences.LIGHT_SQUARE_COLOR;
                }
                boardString.append(squareColor)
                        .append(board[row][col])
                        .append(EscapeSequences.RESET_COLOR);
            }
            boardString.append(" ").append(rowNumber).append("\n"); // add row label and newline
        }

        boardString.append(" ");
        // Print column labels again
        if (isWhiteView) {
            for (String column : columns) {
                boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE).append(" ").append(column).append("  ");
            }
        } else {
            for (int i = columns.length - 1; i >= 0; i--) {
                boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE).append(" ").append(columns[i]).append("  ");
            }
        }
        boardString.append("\n").append(EscapeSequences.RESET_BG_COLOR).append(EscapeSequences.RESET_TEXT_COLOR);

        return boardString.toString();
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
