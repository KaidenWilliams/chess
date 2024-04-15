package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.customSerializers.JsonRegistrar;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
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

            case JOIN_OBSERVER:
                JoinObserverCommand observer = new Gson().fromJson(message, JoinObserverCommand.class);
                JoinObserver(observer, session);

            case MAKE_MOVE:
                MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
                MakeMove(move, session);

            case LEAVE:
                LeaveCommand leave = new Gson().fromJson(message, LeaveCommand.class);
                Leave(leave, session);

            case RESIGN:
                ResignCommand resign = new Gson().fromJson(message, ResignCommand.class);
                Resign(resign, session);

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
                connectionManager.sendMessage(gameId, authToken, color, loadGameString);

                String prettyColor = (color == ChessGame.TeamColor.WHITE ? "white" : "black");
                NotificationMessage notification = new NotificationMessage(String.format("%s has joined the game as %s", userName, prettyColor));
                String notificationString = new Gson().toJson(notification);
                connectionManager.broadcastMessage(gameId, authToken, color, notificationString);

            }

            connectionManager.addGame(gameId);
            connectionManager.addUser(gameId, authToken, userName, color, session);

        } catch (Exception ex) {
            // send error notification
        }

//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
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

            String userName = null;

            connectionManager.addGame(gameId);
            connectionManager.addUser(gameId, authToken, userName, null, session);
        }
        catch (Exception ex) {

        }

//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
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


//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }


//
//    //5.
//    //        -RESIGN sent so server
//    //-server marks the game as over (can modify ChessGame class, or set nextmove to null, think that is easiest)
//    //- NOTIFICATION sent to all clients informing what client has resigned
//    //5. Resign: Integer gameID



}

