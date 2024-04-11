package ui;

public class ChessGameBuilder {
    private static final String[] COLUMNS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] ROWS = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private static final String[][] BOARD = new String[8][8];


    public static String printBoard(String color) {
        boolean isWhiteView = color.equalsIgnoreCase("white");
        initializeBoard();
        placePieces(isWhiteView);
        return printBoardString(isWhiteView);
    }

    private static void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                BOARD[row][col] = EscapeSequences.EMPTY;
            }
        }
    }

    private static void placePieces(boolean isWhiteView) {
        placePiecesInStandardPosition();
        if (!isWhiteView) {
            flipPiecesForBlackView();
        }
    }

    private static void placePiecesInStandardPosition() {
        BOARD[0][0] = EscapeSequences.WHITE_ROOK;
        BOARD[0][1] = EscapeSequences.WHITE_KNIGHT;
        BOARD[0][2] = EscapeSequences.WHITE_BISHOP;
        BOARD[0][3] = EscapeSequences.WHITE_QUEEN;
        BOARD[0][4] = EscapeSequences.WHITE_KING;
        BOARD[0][5] = EscapeSequences.WHITE_BISHOP;
        BOARD[0][6] = EscapeSequences.WHITE_KNIGHT;
        BOARD[0][7] = EscapeSequences.WHITE_ROOK;

        BOARD[7][0] = EscapeSequences.BLACK_ROOK;
        BOARD[7][1] = EscapeSequences.BLACK_KNIGHT;
        BOARD[7][2] = EscapeSequences.BLACK_BISHOP;
        BOARD[7][3] = EscapeSequences.BLACK_QUEEN;
        BOARD[7][4] = EscapeSequences.BLACK_KING;
        BOARD[7][5] = EscapeSequences.BLACK_BISHOP;
        BOARD[7][6] = EscapeSequences.BLACK_KNIGHT;
        BOARD[7][7] = EscapeSequences.BLACK_ROOK;

        for (int col = 0; col < 8; col++) {
            BOARD[1][col] = EscapeSequences.WHITE_PAWN;
            BOARD[6][col] = EscapeSequences.BLACK_PAWN;
        }
    }

    private static void flipPiecesForBlackView() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 8; col++) {
                BOARD[row][col] = getBlackPieceEquivalent(BOARD[7 - row][col]);
            }
        }

        swapKingAndQueen();
    }

    private static String getBlackPieceEquivalent(String piece) {
        return switch (piece) {
            case EscapeSequences.WHITE_ROOK -> EscapeSequences.BLACK_ROOK;
            case EscapeSequences.WHITE_KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case EscapeSequences.WHITE_BISHOP -> EscapeSequences.BLACK_BISHOP;
            case EscapeSequences.WHITE_QUEEN -> EscapeSequences.BLACK_QUEEN;
            case EscapeSequences.WHITE_KING -> EscapeSequences.BLACK_KING;
            case EscapeSequences.WHITE_PAWN -> EscapeSequences.BLACK_PAWN;
            case EscapeSequences.BLACK_ROOK -> EscapeSequences.WHITE_ROOK;
            case EscapeSequences.BLACK_KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case EscapeSequences.BLACK_BISHOP -> EscapeSequences.WHITE_BISHOP;
            case EscapeSequences.BLACK_QUEEN -> EscapeSequences.WHITE_QUEEN;
            case EscapeSequences.BLACK_KING -> EscapeSequences.WHITE_KING;
            case EscapeSequences.BLACK_PAWN -> EscapeSequences.WHITE_PAWN;
            default -> piece;
        };
    }

    private static void swapKingAndQueen() {
        String temp = BOARD[0][4];
        BOARD[0][4] = BOARD[0][3];
        BOARD[0][3] = temp;

        temp = BOARD[7][4];
        BOARD[7][4] = BOARD[7][3];
        BOARD[7][3] = temp;
    }

    private static String printBoardString(boolean isWhiteView) {
        StringBuilder boardString = new StringBuilder(EscapeSequences.ERASE_SCREEN);
        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        for (int row = 0; row < 8; row++) {
            int rowIndex = isWhiteView ? row : 7- row;
            printRowLabelRight(boardString, ROWS[rowIndex]);

            for (int col = 0; col < 8; col++) {
                String squareColor = getSquareColor(row, col);
                String piece = BOARD[rowIndex][col];
                boardString.append(squareColor)
                        .append(piece)
                        .append(EscapeSequences.RESET_COLOR);
            }

            printRowLabelLeft(boardString, ROWS[rowIndex]);
            boardString.append("\n");
        }

        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        return boardString.toString();
    }

    private static void printColumnLabels(StringBuilder boardString, boolean isWhiteView) {
        boardString.append(EscapeSequences.ROWLABELPADDING).append(EscapeSequences.ROWLABELSPACING);
        for (int i = 0; i < COLUMNS.length; i++) {
            int columnIndex = isWhiteView ? i : COLUMNS.length - 1 - i;
            boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                    .append(COLUMNS[columnIndex])
                    .append(EscapeSequences.ROWLABELSPACING)
                    .append(EscapeSequences.RESET_TEXT_COLOR);
        }
        boardString.append(EscapeSequences.ROWLABELSPACING);
    }


    private static void printRowLabelRight(StringBuilder boardString, String rowLabel) {
        boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                .append(rowLabel)
                .append(" ");
    }

    private static void printRowLabelLeft(StringBuilder boardString, String rowLabel) {
        boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                .append(" ")
                .append(rowLabel);
    }

    private static String getSquareColor(int row, int col) {
        boolean isLightSquare = (row + col) % 2 == 0;
        return isLightSquare
                ? EscapeSequences.LIGHT_SQUARE_COLOR
                : EscapeSequences.DARK_SQUARE_COLOR;
    }




    public static final String exitString =
        """
        Leaving the Chess Game.
        You are in the main menu.
        """;

    public static final String helpString =
            """
            Commands:
            < exit -- to leave the Chess Game
            < help -- to get a list of possible commands
            """;
}