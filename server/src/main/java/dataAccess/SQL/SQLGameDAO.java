package dataAccess.SQL;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.IGameDAO;
import model.GameModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends GeneralSQLDAO implements IGameDAO {


    //1. Get all games
    public Collection<GameModel> listAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var result = new ArrayList<GameModel>();
            var statement = "SELECT * FROM game";
            ResultSet rs = executeQuery(conn, statement);
            if (rs != null) {
                while (rs.next()) {
                    result.add(makeGame(rs));
                }
                return result;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving row by authToken", 500);
        }
    }


    //2. Insert row
    public GameModel create(GameModel providedGameModel) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game (whiteusername, blackusername, gamename, game) VALUES (?, ?, ?, ?)";
            String chessGame = JsonRegistrar.getChessGameGson().toJson(providedGameModel.chessGame());

            var id = executeUpdateWithKeys(conn, statement, providedGameModel.whiteUsername(), providedGameModel.blackUsername(), providedGameModel.blackUsername(), chessGame);
            if (id != 0) {
                return new GameModel(id, providedGameModel.whiteUsername(), providedGameModel.blackUsername(), providedGameModel.blackUsername(), providedGameModel.chessGame());
            } else {
                throw new DataAccessException("Insert into game failed", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert row into Game", 500);
        }
    }

    // TODO Serialization
    private GameModel makeGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String whiteUsername = rs.getString("whiteusername");
        String blackUsername = rs.getString("blackusername");
        String gameName = rs.getString("gamename");
        String chessGame = rs.getString("game");
        ChessGame gameDeserialized = JsonRegistrar.getChessGameGson().fromJson(chessGame, ChessGame.class);
        return new GameModel(id, whiteUsername, blackUsername, gameName, gameDeserialized);
    }

    //TODO update ChessGame
//    public updateChessGame();



    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
        var statement = "SELECT FROM game WHERE id = ?";
        ResultSet rs = executeQuery(conn, statement, gameID);
        if (rs != null) {
            return makeGame(rs);
        } else {
            return null;
        }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving row by authToken", 500);
        }
    }


    //4. Update username for correct color with id TODO needs work
    public GameModel updateUsername(GameModel oldGame, String usernameNew, String color) throws DataAccessException {

        if (color == null) {
            //Add Spectator here
            return oldGame;
        }

        String usernameToUpdate = "";
        if (color.equalsIgnoreCase("WHITE") ) {
            if (oldGame.whiteUsername() == null) {
                usernameToUpdate = "whiteusername";
            }
            else {
                return null;
            }
        }
        else if (color.equalsIgnoreCase("BLACK") ) {
            if (oldGame.blackUsername() == null) {
                usernameToUpdate = "blackusername";
            }
            else {
                return null;
            }
        }

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game WHERE ? = ?";
            int rows = executeUpdateWithNumberRows(conn, statement, usernameToUpdate, usernameNew);
            if (rows >= 1) {
                return new GameModel(oldGame.gameID(), oldGame.whiteUsername(), usernameNew, oldGame.gameName(), oldGame.chessGame());
            } else {
                throw new DataAccessException("Error while updating game with new username", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating game with new username", 500);
        }

    }

    //5. Delete all

    public void deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE game";
            executeUpdateWithNumberRows(conn, statement);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete all from Game", 500);
        }
    }


    //6. Add Spectator - don't have to yet





}
