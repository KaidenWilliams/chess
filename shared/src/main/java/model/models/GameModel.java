package model.models;

import chess.ChessGame;

public record GameModel(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) {}
