import Chat.ChatGUI;
import Server.ServerGUI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ServerGUI serverGUI = new ServerGUI();
        new ChatGUI(serverGUI);
        new ChatGUI(serverGUI);
    }
}