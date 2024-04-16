package server.websocket;

import static server.websocket.ChessMoveBuilder.*;
import chess.*;
import com.google.gson.Gson;
import model.customSerializers.JsonRegistrar;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Collection;


@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    private Service service;

    public void setService(Service newService) {
        this.service = newService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        switch (action.getCommandType()) {

            case JOIN_PLAYER:
                JoinPlayerCommand player = new Gson().fromJson(message, JoinPlayerCommand.class);
                JoinPlayer(player, session);
                break;

            case JOIN_OBSERVER:
                JoinObserverCommand observer = new Gson().fromJson(message, JoinObserverCommand.class);
                JoinObserver(observer, session);
                break;

            case MAKE_MOVE:
                MakeMoveCommand move = JsonRegistrar.getChessGameGson().fromJson(message, MakeMoveCommand.class);
                MakeMove(move, session);
                break;

            case LEAVE:
                LeaveCommand leave = new Gson().fromJson(message, LeaveCommand.class);
                Leave(leave, session);
                break;

            case RESIGN:
                ResignCommand resign = new Gson().fromJson(message, ResignCommand.class);
                Resign(resign, session);
                break;

        }
    }


    private void JoinPlayer(JoinPlayerCommand player, Session session) throws IOException {

        try {
            int gameId = player.getGameId();
            String authToken = player.getAuthString();
            var color = player.getPlayerColor();

            String userName = service.getUsername(authToken);
            boolean valid = service.verifyUsername(gameId, userName, color);

            if (valid) {
                connectionManager.addGame(gameId);
                connectionManager.addUser(gameId, authToken, userName, color, session);

                ChessGame game = service.getChessGame(gameId);
                LoadGameMessage loadGame = new LoadGameMessage(game);
                String loadGameString = JsonRegistrar.getChessGameGson().toJson(loadGame);
//                connectionManager.sendMessage(gameId, authToken, loadGameString);
                connectionManager.sendMessageToConnection(session, loadGameString);

                String prettyColor = (color == ChessGame.TeamColor.WHITE ? "white" : "black");
                NotificationMessage notification = new NotificationMessage(String.format("%s has joined the game as %s", userName, prettyColor));
                String notificationString = new Gson().toJson(notification);
                connectionManager.broadcastMessage(gameId, authToken, notificationString);

            }

            else {
                throw new Exception("Cannot steal another user's spot");
            }

        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }


    private void JoinObserver(JoinObserverCommand observer, Session session) throws IOException {

        try {
            int gameId = observer.getGameId();
            String authToken = observer.getAuthString();

            String userName = service.getUsername(authToken);

            connectionManager.addGame(gameId);
            connectionManager.addUser(gameId, authToken, userName, null, session);


            ChessGame game = service.getChessGame(gameId);
            LoadGameMessage loadGame = new LoadGameMessage(game);
            String loadGameString = JsonRegistrar.getChessGameGson().toJson(loadGame);
//            connectionManager.sendMessage(gameId, authToken, loadGameString);
            connectionManager.sendMessageToConnection(session, loadGameString);

            NotificationMessage notification = new NotificationMessage(String.format("%s has joined the game as an observer", userName));
            String notificationString = new Gson().toJson(notification);
            connectionManager.broadcastMessage(gameId, authToken, notificationString);

        }
        catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }


    private void MakeMove(MakeMoveCommand move, Session session) throws IOException {

        try {
            int gameId = move.getGameId();
            String authToken = move.getAuthString();
            ChessMove chessMove = move.getMove();

            UserConnection user = connectionManager.getUser(gameId, authToken);
            ChessGame game = service.getChessGame(gameId);

            //1. verify if correct color + game not over + valid move

            if (user.color != game.getTeamTurn()) {
               throw new Exception("It is not your turn to move");
            }
            if (game.isGameOver()) {
                throw new Exception("The game is over. No more moves can be made");
            }


            //2. verify that there is a white and black user in DB -maybe?




            ChessPosition startPosition = chessMove.getStartPosition();
            ChessPosition endPosition = chessMove.getEndPosition();
            ChessBoard currBoard = game.getBoard();
            ChessPiece currPiece = currBoard.getPiece(startPosition);

            if (currPiece.pieceColor != game.getTeamTurn()) {
                throw new Exception("You cannot move this piece, it is of the wrong color");
            }

            Collection<ChessMove> moves = game.validMoves(startPosition);
            if (!moves.contains(chessMove)) {
                throw new Exception("Not a valid move");
            }


            game.makeMove(chessMove);

            //1. Board sends Load_Game to all clients

            LoadGameMessage loadGame = new LoadGameMessage(game);
            String loadGameString = JsonRegistrar.getChessGameGson().toJson(loadGame);
            connectionManager.sendMessageToConnection(session, loadGameString);
            connectionManager.broadcastMessage(gameId, authToken, loadGameString);


            //2. server sends Notification to all other clients notifying them of what move has been made

            String prettyColor = (user.color == ChessGame.TeamColor.WHITE ? "white" : "black");
            String oppositeColor = (prettyColor.equals("white") ? "Black" : "White");

            String pieceTypeString = getPieceTypeString(currPiece.getPieceType());
            String startPositionString = getPositionString(startPosition);
            String endPositionString = getPositionString(endPosition);

            String moveString = String.format("%s moved %s piece from %s-%s as %s", user.userName, pieceTypeString, startPositionString, endPositionString, prettyColor);

            NotificationMessage notification = new NotificationMessage(moveString);
            String notificationString = new Gson().toJson(notification);
            connectionManager.broadcastMessage(gameId, authToken, notificationString);

            //3. If in checkmate or stalemate, send Notification to all clients

            String eventString = "";
            if (game.isInCheckmate(game.getTeamTurn())) {
                eventString = "checkmate";
            }

            else if (game.isInStalemate(game.getTeamTurn())) {
                eventString = "stalemate";
            }

            if (!eventString.isEmpty()) {

                game.setGameOver(true);
                String endString = String.format("%s is in %s. The game is over", oppositeColor, eventString);
                NotificationMessage notificationEnd = new NotificationMessage(endString);
                String notificationEndString = new Gson().toJson(notificationEnd);
                connectionManager.sendMessageToConnection(session, notificationEndString);
                connectionManager.broadcastMessage(gameId, authToken, notificationEndString);
            }

            String stringGame = JsonRegistrar.getChessGameGson().toJson(game);
            service.updateChessGame(gameId, stringGame);


        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }


    private void Leave(LeaveCommand leaveCommand, Session session) throws IOException {

        try {
            int gameId = leaveCommand.getGameId();
            String authToken = leaveCommand.getAuthString();
            String userName = service.getUsername(authToken);

            UserConnection user = connectionManager.getUser(gameId, authToken);

            if (user.color != null) {
                connectionManager.removeUser(gameId, authToken);
                service.removePlayer(gameId, user.color);

                String prettyColor = (user.color == ChessGame.TeamColor.WHITE ? "white" : "black");
                NotificationMessage notification = new NotificationMessage(String.format("%s has left the game as color %s", userName, prettyColor));
                String notificationString = new Gson().toJson(notification);
                connectionManager.broadcastMessage(gameId, authToken, notificationString);

            }
            else {
                connectionManager.removeUser(gameId, authToken);
                NotificationMessage notification = new NotificationMessage(String.format("%s has left the game as an observer", userName));
                String notificationString = new Gson().toJson(notification);
                connectionManager.broadcastMessage(gameId, authToken, notificationString);
            }

        }
        catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }


    private void Resign(ResignCommand resignCommand, Session session) throws IOException {

        try {
            int gameId = resignCommand.getGameId();
            String authToken = resignCommand.getAuthString();
            String userName = service.getUsername(authToken);

            UserConnection user = connectionManager.getUser(gameId, authToken);

            if (user.color != null) {

                ChessGame game = service.getChessGame(gameId);
                game.setGameOver(true);

                String gameString = JsonRegistrar.getChessGameGson().toJson(game);
                service.updateChessGame(gameId, gameString);

                String prettyColor = (user.color == ChessGame.TeamColor.WHITE ? "white" : "black");
                String oppositeColor = (prettyColor.equals("white") ? "Black" : "White");

                NotificationMessage notification = new NotificationMessage(String.format("%s has resigned from the game as color %s.\n %s has won. The game is over", userName, prettyColor, oppositeColor));
                String notificationString = new Gson().toJson(notification);

                connectionManager.sendMessageToConnection(session, notificationString);
                connectionManager.broadcastMessage(gameId, authToken, notificationString);

            }

            else {
                throw new Exception("Cannot resign as observer");
            }

        }
        catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }



}

