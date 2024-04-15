package clientlogic.websocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptionclient.ClientException;
import model.customSerializers.JsonRegistrar;
import state.AState;
import state.ChessGameState;
import state.StateNotifier;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


// Needs onMessage/addMessageHandler to dictate how it responds to messages
// needs way to connect to server, onOpen/connectToServer

// All websocket functionality in ChessGame state will call these methods, just like normal server facade


// Websocket will set gameId, probably keep track of it too

//    Interactions
//- Root/instigating client starts by sending UserGameCommand to server.
//            - Server recieves and sends appropriate ServerMessages to all clients connected to game
//- when UserGameCommand is invalid, error message sent only to Root client
//





//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    public Session session;
    Integer gameId;
    String authToken;


    public WebSocketFacade(String url, String authToken, Integer gameId, StateNotifier observer) throws ClientException {

        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.authToken = authToken;
            this.gameId = gameId;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    var type = serverMessage.getServerMessageType();
                    switch (type) {
                        case LOAD_GAME:
                            LoadGameMessage gameMessage = JsonRegistrar.getChessGameGson().fromJson(message, LoadGameMessage.class);

                            var chessGame = gameMessage.getGame();
                            ChessGameState state = observer.getChessGameState();
                            if (state != null) {
                                state.setGame(chessGame);
                                WebSocketPrinter.printGame(state.DrawBoard());
                            } else {
                                WebSocketPrinter.printError("Error: Invalid LoadGame Message sent. User is not in ChessGame State");
                            }
                            break;
                        case ERROR:
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            WebSocketPrinter.printError(errorMessage.getErrorMessage());
                            break;
                        case NOTIFICATION:
                            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                            WebSocketPrinter.printNotification(notificationMessage.getMessage());
                            break;
                        default:
                            WebSocketPrinter.printError("Error: Unknown Server Message Type encountered");
                            break;
                    }
                }
            });

        } catch (Exception ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


//
//
//    //1.
//    //        -JOIN_PLAYER sent to server
//    //-server send LOAD_GAME message back to root client
//    //-server sends NOTIFICATION to all other clients, informs them what color root client is joining as
//    //---- needs to check what they are joining is actually valid in DB, can't steal spot they aren't in, client hacking protection


    //1. Join_Player: Integer gameID, ChessGame.TeamColor playerColor
    public void JoinPlayer(String playerColor) throws ClientException {
        try {
            ChessGame.TeamColor realColor = (playerColor.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK);
            var action = new JoinPlayerCommand(authToken, gameId, realColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
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
    public void JoinObserver() throws ClientException {
        try {
            var action = new JoinObserverCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

    //
//
//    //3.
//    //        -MAKE_MOVE sent to server
//    //-server verifies validity of move
//    //-game is updated in DB to represent the move
//    //-server sends a LOAD_GAME message to all clients in the game with updated game
//    //-server sends NOTIFICATION to all other clients informing what move was made
//
//    //3. Make_Move: Integer gameID, ChessMove move
    public void MakeMove(ChessMove move) throws ClientException {
        try {
            var action = new MakeMoveCommand(authToken, gameId, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }
//
//
//    //4.
//    //        -LEAVE send to server
//    //-game connection updated so root client is no longer in it
//    //-NOTIFICATION sent to all other clients informing them client has left
//
//    //4. Leave: Integer gameID
    public void Leave() throws ClientException {
        try {
            var action = new LeaveCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

    //
//
//    //5.
//    //        -RESIGN sent so server
//    //-server marks the game as over (can modify ChessGame class, or set nextmove to null, think that is easiest)
//    //- NOTIFICATION sent to all clients informing what client has resigned
//    //5. Resign: Integer gameID
    public void Resign() throws ClientException {
        try {
            var action = new ResignCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

}