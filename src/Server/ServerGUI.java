package Server;

import Chat.ChatGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ServerGUI extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final String SERVER_STARTED = "Server started.";
    private static final String SERVER_ALREADY_STARTED = "Server already started.";
    private static final String SERVER_STOPPED = "Server stopped.";
    private static final String SERVER_ALREADY_STOPPED = "Server already stopped.";
    private static final String SERVER_CONNECTION = " connected to server.";
    private static final String SERVER_DISCONNECTED = " disconnected from server.";
    private static final String LOG_FILENAME = "Log.txt";


    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();
    public boolean isServerWorking;

    ArrayList<ChatGUI> chatGUIList = new ArrayList<>();

    public ServerGUI() {
        isServerWorking = false;
        btnStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartButtonListener();

            }
        });
        btnStop.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StopButtonListener();
            }
        });
        WindowSettings();
    }

    private void StopButtonListener() {
        if (isServerWorking) {
            log.append(SERVER_STOPPED + "\n");
            //System.out.println(SERVER_STOPPED);
            ServerDisconnected();
        } else {
            log.append(SERVER_ALREADY_STOPPED + "\n");
            //System.out.println(SERVER_ALREADY_STOPPED);
        }
    }

    private void StartButtonListener() {
        if (isServerWorking) {
            log.append(SERVER_ALREADY_STARTED + "\n");
            //System.out.println(SERVER_ALREADY_STARTED);
        } else {
            isServerWorking = true;
            log.append(SERVER_STARTED + "\n");
            //System.out.println(SERVER_STARTED);
            log.append(ReadLog());
        }
    }

    private void WindowSettings() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        log.setEditable(false);
        add(log);

        JPanel panBottom = new JPanel(new GridLayout(1,2));
        panBottom.add(btnStart);
        panBottom.add(btnStop);
        add(panBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void SaveLog(String message){
        try (FileWriter writer = new FileWriter(LOG_FILENAME, true)){
            writer.write(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String ReadLog(){
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_FILENAME);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            stringBuilder.append("\n");
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void RecieveMessage(String sender, String message){
        log.append(sender + ": " + message + "\n");
        SaveLog(sender + ": " + message + "\n");
        SendMessageToAll(sender, message);
    }

    public void SendMessageToAll(String sender, String message){
        for (ChatGUI chatGUI : chatGUIList) {
            chatGUI.RecieveMessage(sender, message);
        }
    }

    public boolean IncomeConnection(ChatGUI chatGUI) {
        if (isServerWorking) {
            chatGUIList.add(chatGUI);
            log.append(chatGUI.GetLogin() + SERVER_CONNECTION + "\n");
            return true;
        }
        return false;
    }

    public void DisconnectClient(ChatGUI chatGUI){
        log.append(chatGUI.GetLogin() + SERVER_DISCONNECTED + "\n");
        chatGUIList.remove(chatGUI);
        chatGUI.ServerStopped();
    }

    public void ServerDisconnected(){
        for (ChatGUI chatGUI : chatGUIList) {
            chatGUI.ServerStopped();
        }
        isServerWorking = false;
    }
}
