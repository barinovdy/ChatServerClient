package client;

public interface ClientView {
    void showMessage(String message);
    void sendMessage();
    void connectToServer();
    void disconnectFromServer();

    String getLogin();
    String getPassword();
    String getPort();
    String getIPAddress();
}
