package clientlogic.websocket;

import clientlogic.ClientController;
import com.google.gson.Gson;
import exceptionclient.ClientException;
import webSocketMessages.serverMessages.ServerMessage;
//import exception.ResponseException;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


// Needs onMessage/addMessageHandler to dictate how it responds to messages
// needs way to connect to server, onOpen/connectToServer

// All websocket functionality in ChessGame state will call these methods, just like normal server facade




//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;

    ClientController controller;


    public WebSocketFacade(String url, ClientController controller) throws ClientException {

        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.controller = controller;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    controller.printWS(notification.toString());
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }





//    public void enterPetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.ENTER, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void leavePetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.EXIT, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//            this.session.close();
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

}

