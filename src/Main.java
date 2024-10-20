import client.ClientController;
import client.ClientGUI;
import server.controller.ServerController;
import server.gui.ServerGUI;
import server.repository.ServerRepository;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //создание объектов сервера и создание связи между ними
        ServerGUI serverGUI = new ServerGUI();
        ServerController serverController = new ServerController();
        ServerRepository serverRepository = new ServerRepository();
        serverController.setServerView(serverGUI);
        serverController.setServerRepositoryView(serverRepository);
        serverGUI.setServerController(serverController);

        //создание объектов клиента 1 и создание связи между ними
        ClientGUI clientGUI1 = new ClientGUI();
        ClientController clientController1 = new ClientController();
        clientController1.setClientView(clientGUI1);
        clientGUI1.setClientController(clientController1);
        clientController1.setServerController(serverController);

        //создание объектов клиента 2 и создание связи между ними
        ClientGUI clientGUI2 = new ClientGUI();
        ClientController clientController2 = new ClientController();
        clientController2.setClientView(clientGUI2);
        clientGUI2.setClientController(clientController2);
        clientController2.setServerController(serverController);
    }
}