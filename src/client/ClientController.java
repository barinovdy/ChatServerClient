package client;

import server.controller.ServerController;

public class ClientController {
    private static final String CONNECTION_SUCCEED = "Connection to server succeed.";
    private static final String CONNECTION_FAILED = "Connection to server failed.";
    private static final String SERVER_DISCONNECTED = "Client disconnected from server.";
    private static final String SERVER_STOPPED = "Server stopped.";
    private static final String SERVER_DROPPED = "Server dropped connection.";

    private boolean isConnectedToServer = false;
    private ClientView clientView;
    private ServerController server;

    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }

    public void setServerController(ServerController server) {
        this.server = server;
    }

    public boolean connectToServer() {
        if (!isConnectedToServer & connectionDataIsValid()){
            if (server.connectClient(this)) {
                //Connect
                clientView.showMessage(CONNECTION_SUCCEED);
                isConnectedToServer = true;
                clientView.showMessage(server.readLog());
                return true;
            } else {
                //Connection failed
                clientView.showMessage(SERVER_DROPPED);
                return false;
            }
        } else {
            //Connection failed
            clientView.showMessage(CONNECTION_FAILED);
            return false;
        }
    }

    private boolean connectionDataIsValid() {
        boolean valid = !clientView.getLogin().isEmpty() &
                !clientView.getIPAddress().isEmpty() &
                !clientView.getPort().isEmpty() &
                !clientView.getPassword().isEmpty();
        return valid;
    }

    public void disconnectFromServer(){
        if (isConnectedToServer) {
            server.disconnectClient(this);
        }
    }

    public void disconnectByServer(){
        if (isConnectedToServer) {
            clientView.showMessage(SERVER_STOPPED);
            isConnectedToServer = false;
            clientView.disconnectFromServer();
        }
    }

    public void sendMessage(String sender, String message) {
        if (isConnectedToServer){
            if (server.isServerWorking) {
                server.recieveMessage(sender, message);
            } else {
                clientView.showMessage(SERVER_DISCONNECTED);
            }
        }
    }

    public void recieveMessage(String sender, String message) {
        clientView.showMessage(sender + ": " + message);
    }

    public String getLogin() {
        return clientView.getLogin();
    }
}

