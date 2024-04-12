package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    String message;

    public NotificationMessage(ServerMessage.ServerMessageType type, String content) {
        super(type);
        this.message = content;
    }


    public String getMessage() {
        return this.message;
    }


}
