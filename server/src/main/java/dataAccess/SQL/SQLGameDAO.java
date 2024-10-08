package dataAccess.SQL;

import chess.ChessGame;
import model.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.IGameDAO;
import model.customSerializers.JsonRegistrar;
import model.models.GameModel;

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
            if (rs.next()) {
                boolean moreRows = true;
                while (moreRows) {
                    result.add(makeGame(rs));

                    if (!rs.next()){
                        moreRows = false;
                    }
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

            var id = executeUpdateWithKeys(conn, statement, providedGameModel.whiteUsername(), providedGameModel.blackUsername(), providedGameModel.gameName(), chessGame);
            if (id != 0) {
                return new GameModel(id, providedGameModel.whiteUsername(), providedGameModel.blackUsername(), providedGameModel.blackUsername(), providedGameModel.chessGame());
            } else {
                throw new DataAccessException("Insert into game failed", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert row into Game", 500);
        }
    }


    private GameModel makeGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String whiteUsername = rs.getString("whiteusername");
        String blackUsername = rs.getString("blackusername");
        String gameName = rs.getString("gamename");
        String chessGame = rs.getString("game");
        ChessGame gameDeserialized = JsonRegistrar.getChessGameGson().fromJson(chessGame, ChessGame.class);
        return new GameModel(id, whiteUsername, blackUsername, gameName, gameDeserialized);
    }


    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
        var statement = "SELECT * FROM game WHERE id = ?";
        ResultSet rs = executeQuery(conn, statement, gameID);
        if (rs.next()) {
            return makeGame(rs);
        } else {
            return null;
        }
        } catch (SQLException e) {
            throw new DataAccessException("Error: Could not find row from specified id", 500);
        }
    }



    //4. Update username for correct color with id
    public void updateChessGame(int gameId, String chessGame) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET game = ? WHERE id = ?";

            int rows = executeUpdateWithNumberRows(conn, statement, chessGame, gameId);
            if (rows < 1) {
                throw new DataAccessException("Error while updating game with new username", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating game with new username", 500);
        }

    }


    //4. Update username for correct color with id
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
            var statement = String.format("UPDATE game SET %s = ? WHERE id = ?", usernameToUpdate);

            int rows = executeUpdateWithNumberRows(conn, statement, usernameNew, oldGame.gameID());
            if (rows >= 1) {
                return new GameModel(oldGame.gameID(), oldGame.whiteUsername(), usernameNew, oldGame.gameName(), oldGame.chessGame());
            } else {
                throw new DataAccessException("Error while updating game with new username", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating game with new username", 500);
        }

    }



    public void deleteUsernameFromGame(int gameID, ChessGame.TeamColor color) throws DataAccessException {

        if (color == null) {
            return;
        }

        String usernameToUpdate = "";
        if (color == ChessGame.TeamColor.WHITE ) usernameToUpdate = "whiteusername";
        else if (color == ChessGame.TeamColor.BLACK ) usernameToUpdate = "blackusername";



        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("UPDATE game SET %s = null WHERE id = ?", usernameToUpdate);

            int rows = executeUpdateWithNumberRows(conn, statement, gameID);
            if (rows < 1) {
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

}
