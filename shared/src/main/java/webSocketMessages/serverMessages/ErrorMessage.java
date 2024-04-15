package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    String errorMessage;

    public ErrorMessage(String content) {
        super(ServerMessageType.ERROR);
        this.errorMessage = content;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

}
