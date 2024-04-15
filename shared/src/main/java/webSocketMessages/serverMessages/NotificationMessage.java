package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    String message;

    public NotificationMessage(String content) {
        super(ServerMessageType.NOTIFICATION);
        this.message = content;
    }


    public String getMessage() {
        return this.message;
    }


}
