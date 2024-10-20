package server.controller;

import client.ClientController;
import server.gui.ServerView;
import server.repository.ServerRepositoryView;

import java.util.ArrayList;
import java.util.Iterator;

public class ServerController {
    private static final String SERVER_STARTED = "Server started.";
    private static final String SERVER_ALREADY_STARTED = "Server already started.";
    private static final String SERVER_STOPPED = "Server stopped.";
    private static final String SERVER_ALREADY_STOPPED = "Server already stopped.";
    private static final String SERVER_CONNECTION = " connected to server.";
    private static final String SERVER_DISCONNECTED = " disconnected from server.";
    private static final String LOG_FILENAME = "Log.txt";

    ArrayList<ClientController> clientControllers = new ArrayList<>();

    ServerView serverView;
    ServerRepositoryView serverRepositoryView;

    public boolean isServerWorking;

    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
    }

    public void setServerRepositoryView(ServerRepositoryView serverRepositoryView) {
        this.serverRepositoryView = serverRepositoryView;
    }

    public void startServer() {
        if (isServerWorking) {
            serverView.showMessage(SERVER_ALREADY_STARTED + "\n");
        } else {
            isServerWorking = true;
            serverView.showMessage(SERVER_STARTED + "\n");
            serverView.showMessage(readLog());
        }
    }

    public void stopServer() {
        if (isServerWorking) {
            serverView.showMessage(SERVER_STOPPED + "\n");
            disconnectAllClients();
            isServerWorking = false;
        } else {
            serverView.showMessage(SERVER_ALREADY_STOPPED + "\n");
        }
    }

    public boolean connectClient(ClientController clientController) {
        if (isServerWorking) {
            clientControllers.add(clientController);
            serverView.showMessage(clientController.getLogin() + SERVER_CONNECTION + "\n");
            return true;
        }
        return false;
    }

    public void disconnectClient(ClientController clientController) {
        serverView.showMessage(clientController.getLogin() + SERVER_DISCONNECTED + "\n");
        clientController.disconnectByServer();
        clientControllers.remove(clientController);
    }

    private void disconnectAllClients() {
        Iterator<ClientController> clientControllerIterator = clientControllers.iterator();
        while (clientControllerIterator.hasNext()) {
            ClientController clientControllerNext = clientControllerIterator.next();
            serverView.showMessage(clientControllerNext.getLogin() + SERVER_DISCONNECTED + "\n");
            clientControllerNext.disconnectByServer();
            clientControllerIterator.remove();
        }
    }

    public void saveLog(String message) {
        serverRepositoryView.saveLog(LOG_FILENAME, message);
    }

    public String readLog() {
        return serverRepositoryView.readLog(LOG_FILENAME);
    }

    public void recieveMessage(String sender, String message) {
        serverView.showMessage(sender + ": " + message + "\n");
        saveLog(sender + ": " + message + "\n");
        sendMessageToAll(sender, message);
    }

    public void sendMessageToAll(String sender, String message){
        for (ClientController clientController : clientControllers) {
            clientController.recieveMessage(sender, message);
        }
    }
}
