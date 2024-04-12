package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    String errorMessage;

    public ErrorMessage(ServerMessageType type, String content) {
        super(type);
        this.errorMessage = content;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

}
