package clientlogic.websocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptionclient.ClientException;
import model.customSerializers.JsonRegistrar;
import state.ChessGameState;
import state.StateNotifier;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;



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
                                WebSocketPrinter.printGame(state.DrawBoard(state.getGame().getBoard().getSquares()));
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


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public void JoinPlayer(String playerColor) throws ClientException {
        try {
            ChessGame.TeamColor realColor = (playerColor.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK);
            var action = new JoinPlayerCommand(authToken, gameId, realColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }


    public void JoinObserver() throws ClientException {
        try {
            var action = new JoinObserverCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }


    public void MakeMove(ChessMove move) throws ClientException {
        try {
            var action = new MakeMoveCommand(authToken, gameId, move);
            this.session.getBasicRemote().sendText(JsonRegistrar.getChessGameGson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

    public void Leave() throws ClientException {
        try {
            var action = new LeaveCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

    public void Resign() throws ClientException {
        try {
            var action = new ResignCommand(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }

}