package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
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



// Corresponds to all endpoints from WebsocketFacade
// calls correct functionality for each endpoint

// Keeps list of connections (people in shop) to notify when something happens
// - don't know how I will implement this, because need list of connections for every gam
// - I see need for ConnectionManager, implement as necessary

//Unlike example, mine needs to call service methods to update DB, specifically for Chess moves

@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    private Service service;

    public void setService(Service newService) {
        this.service = newService;
    }

//    JOIN_PLAYER
//    JOIN_OBSERVER
//    MAKE_MOVE
//    LEAVE
//    RESIGN
//
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

//            case MAKE_MOVE:
//                MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
//                MakeMove(move, session);
//            break;

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



    //    //1.
//    //        -JOIN_PLAYER sent to server
//    //-server send LOAD_GAME message back to root client
//    //-server sends NOTIFICATION to all other clients, informs them what color root client is joining as
//    //---- needs to check what they are joining is actually valid in DB, can't steal spot they aren't in, client hacking protection

    //1. Join_Player: Integer gameID, ChessGame.TeamColor playerColor

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

        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }



    //
//    //2.
//    //        -JOIN_OBSERVER sent to server
//    //-server sends LOAD_GAME back to root client
//    //-server sends NOTIFICATION to all other clients in game
//    //---- needs to check what they are joining is actually valid in DB, can't steal spot they aren't in, client hacking protection
//
    //2. Join_Observer: Integer gameID

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





//    //3.
//    //        -MAKE_MOVE sent to server
//    //-server verifies validity of move
//    //-game is updated in DB to represent the move
//    //-server sends a LOAD_GAME message to all clients in the game with updated game
//    //-server sends NOTIFICATION to all other clients informing what move was made
//
//    //3. Make_Move: Integer gameID, ChessMove move



//
//    //4.
//    //        -LEAVE send to server
//    //-game connection updated so root client is no longer in it
//    //-NOTIFICATION sent to all other clients informing them client has left
//
//    //4. Leave: Integer gameID

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
            }

        }
        catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }





//
//    //5.
//    //        -RESIGN sent so server
//    //-server marks the game as over (can modify ChessGame class, or set nextmove to null, think that is easiest)
//    //- NOTIFICATION sent to all clients informing what client has resigned
//    //5. Resign: Integer gameID

    private void Resign(ResignCommand resignCommand, Session session) throws IOException {

        try {
            int gameId = resignCommand.getGameId();
            String authToken = resignCommand.getAuthString();
            String userName = service.getUsername(authToken);

            UserConnection user = connectionManager.getUser(gameId, authToken);

            if (user.color != null) {

                ChessGame game = service.getChessGame(gameId);
                game.setTeamTurn(null);

                String gameString = JsonRegistrar.getChessGameGson().toJson(game);
                service.updateChessGame(gameId, gameString);

                String prettyColor = (user.color == ChessGame.TeamColor.WHITE ? "white" : "black");
                String oppositeColor = (prettyColor.equals("white") ? "Black" : "White");

                NotificationMessage notification = new NotificationMessage(String.format("%s has resigned from the game as color %s.\n %s has won. The game is over", userName, prettyColor, oppositeColor));
                String notificationString = new Gson().toJson(notification);

                connectionManager.sendMessageToConnection(session, notificationString);
                connectionManager.broadcastMessage(gameId, authToken, notificationString);

            }

        }
        catch (Exception ex) {
            ErrorMessage error = new ErrorMessage(ex.getMessage());
            String errorMessageString = new Gson().toJson(error);
            connectionManager.sendMessageToConnection(session, errorMessageString);
        }

    }



}

