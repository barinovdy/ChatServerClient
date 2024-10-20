package server.gui;

import server.controller.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ServerGUI extends JFrame implements ServerView{
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JButton btnStart, btnStop;
    private JTextArea log;

    private ServerController serverController;

    public ServerGUI() {
        windowSettings();
        createPanel();
        setVisible(true);
    }

    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    private void windowSettings() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
    }

    private void createPanel() {
        topPanel();
        bottomPanel();
    }

    private void topPanel() {
        log = new JTextArea();
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
    }

    private void bottomPanel() {
        btnStart = new JButton("Start");
        btnStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        JPanel panBottom = new JPanel(new GridLayout(1,2));
        panBottom.add(btnStart);
        panBottom.add(btnStop);
        add(panBottom, BorderLayout.SOUTH);
    }

    @Override
    public void showMessage(String message) {
        log.append(message);
    }

    @Override
    public void startServer() {
        serverController.startServer();
    }

    @Override
    public void stopServer() {
        serverController.stopServer();
    }
}
