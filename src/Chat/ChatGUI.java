package Chat;

import Server.ServerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class ChatGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final String CONNECTION_SUCCEED = "Connection to server succeed.";
    private static final String CONNECTION_FAILED = "Connection to server failed.";
    private static final String SERVER_DISCONNECTED = "Client disconnected from server.";
    private static final String SERVER_STOPPED = "Server stopped.";

    private final JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField();
    private final JPasswordField tfPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private boolean isServerConnection = false;
    private ServerGUI serverGUI;

    public ChatGUI(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
        WindowSettings();

        //Login button
        btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerConnection();
            }
        });

        //Send button
        btnSend.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SendMessage();
            }
        });

        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    SendMessage();
                }
            }
        });
    }

    private void WindowSettings() {
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout. CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout. SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        setVisible(true);
    }

    private void SendMessage() {
        if (isServerConnection && serverGUI.isServerWorking) {
            String message = tfMessage.getText();
            //log.append(tfLogin.getText() + ": " + message + "\n");
            serverGUI.RecieveMessage(tfLogin.getText(), message);
            tfMessage.setText("");
        }
        if (!serverGUI.isServerWorking) {
            log.append(SERVER_DISCONNECTED + "\n");
        }
    }

    private void ServerConnection() {
        if (!isServerConnection) {
            if (serverGUI.isServerWorking) {
                //if server working
                if (tfLogin.getText().isEmpty() &&
                        tfIPAddress.getText().isEmpty() &&
                        tfPort.getText().isEmpty() &&
                        tfPassword.toString().isEmpty()) {
                    //Connection failed
                    log.append(CONNECTION_FAILED + "\n");
                } else {
                    //Connect
                    if (serverGUI.IncomeConnection(this)) {
                        log.append(CONNECTION_SUCCEED + "\n");
                        isServerConnection = true;
                        tfLogin.setEditable(false);
                        log.append(serverGUI.ReadLog());
                    } else {
                        log.append(CONNECTION_FAILED + "\n");
                    }
                }
            } else {
                //if server stopped
                log.append(CONNECTION_FAILED + "\n");
            }
        }
    }

    public void DisconnectFromServer(){
        if (isServerConnection) {
            serverGUI.DisconnectClient(this);
        }
    }

    public void ServerStopped(){
        if (isServerConnection) {
            log.append(SERVER_STOPPED + "\n");
            isServerConnection = false;
        }
    }

    public void RecieveMessage(String sender, String message) {
        log.append(sender + ": " + message + "\n");
    }

    public String GetLogin(){
        return tfLogin.getText();
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            DisconnectFromServer();
        }
        super.processWindowEvent(e);
    }
}
