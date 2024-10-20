package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class ClientGUI extends JFrame implements ClientView{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JTextArea log;
    private JPanel panelTop, panelBottom;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField tfPassword;
    private JButton btnLogin, btnSend;

    private ClientController clientController;

    public ClientGUI() {
        windowSettings();
        createPanel();
        setVisible(true);
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    private void windowSettings() {
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat client");
    }

    private void createPanel(){
        createHeader();
        createLog();
        createFooter();
    }

    private void createHeader() {
        panelTop = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField();
        tfPassword = new JPasswordField();
        btnLogin = new JButton("Login");

        //Login button
        btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);
    }

    private void createLog() {
        log = new JTextArea();
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
    }

    private void createFooter() {
        panelBottom = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        btnSend = new JButton("Send");

        //Send button
        btnSend.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        //Enter key
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage();
                }
            }
        });

        panelBottom.add(tfMessage, BorderLayout. CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout. SOUTH);
    }

    @Override
    public void showMessage(String message) {
        log.append(message + "\n");
    }

    @Override
    public void connectToServer() {
        if (clientController.connectToServer()) {
            //tfLogin.setEditable(false);
            panelTop.setVisible(false);
        }
    }

    @Override
    public void disconnectFromServer() {
        panelTop.setVisible(true);
    }

    @Override
    public void sendMessage() {
        clientController.sendMessage(tfLogin.getText(), tfMessage.getText());
        tfMessage.setText("");
    }

    @Override
    public String getLogin() {
        return tfLogin.getText();
    }

    @Override
    public String getPassword() {
        return String.valueOf(tfPassword.getPassword());
    }

    @Override
    public String getPort() {
        return tfPort.getText();
    }

    @Override
    public String getIPAddress() {
        return tfIPAddress.getText();
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            clientController.disconnectFromServer();
        }
        super.processWindowEvent(e);
    }
}
