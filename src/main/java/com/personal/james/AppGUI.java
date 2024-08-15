package com.personal.james;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;



public class AppGUI {

    static final Color buttonColour = Color.WHITE;
    public static JLabel statusMessage;

    public static JFrame generate() {
        JFrame appGUI = new JFrame();
        appGUI.setTitle("MCMosaic");
        appGUI.setLayout(new BorderLayout());
        appGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appGUI.setResizable(true);
        appGUI.setSize(500,500);
        appGUI.setMinimumSize(new Dimension(500,500));
        appGUI.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        ImageIcon image = new ImageIcon("external-resources/appicon.png");
        appGUI.setIconImage(image.getImage());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(400,75));
        JLabel title = new JLabel();
        title.setText("MCMosaic");
        title.setFont(new Font("Comic Sans MS",Font.PLAIN, 36));
        titlePanel.add(title);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(400,350));
        buttonsPanel.setLayout(new GridLayout(3,1));

        JPanel bottomPanel = new JPanel();
        statusMessage = new JLabel();
        statusMessage.setForeground(Color.BLACK);
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(400,50));
        bottomPanel.add(statusMessage);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(255,255,255));
        leftPanel.setPreferredSize(new Dimension(50,400));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(255,255,255));
        rightPanel.setPreferredSize(new Dimension(50,400));

        JButton button1 = new JButton();
        button1.setBackground(buttonColour);
        button1.setFocusPainted(false);
        button1.setPreferredSize(new Dimension(300,50));
        button1.setMargin(new Insets(10,10,10,10));
        button1.addActionListener(e ->Application.transformAllImages());
        button1.setText("Transform all images");


        JButton button2 = new JButton();
        button2.setBackground(buttonColour);
        button2.setFocusPainted(false);
        button2.setPreferredSize(new Dimension(300,50));
        button2.setMargin(new Insets(10,10,10,10));
        button2.addActionListener(x -> {try{
            Desktop.getDesktop().open(new File("images/input"));
        }
        catch (IOException e) {
            System.out.println("/images/input folder not found");
            e.printStackTrace();
        }});
        button2.setText("Open input folder");

        JButton button3 = new JButton();
        button3.setBackground(buttonColour);
        button3.setFocusPainted(false);
        button3.setPreferredSize(new Dimension(300,50));
        button3.setMargin(new Insets(10,10,10,10));
        button3.addActionListener(e -> {try{
            Desktop.getDesktop().open(new File("images/output"));
        }
        catch (IOException z) {
            z.printStackTrace();
        }});
        button3.setText("Open output folder");

        buttonsPanel.add(button1);
        buttonsPanel.add(button2);
        buttonsPanel.add(button3);

        appGUI.add(titlePanel,BorderLayout.NORTH);
        appGUI.add(buttonsPanel,BorderLayout.CENTER);
        appGUI.add(bottomPanel,BorderLayout.SOUTH);
        appGUI.add(leftPanel,BorderLayout.WEST);
        appGUI.add(rightPanel,BorderLayout.EAST);

        return appGUI;
    }

    /**
     * Updates the GUI to display info from the Application class
     * TODO: make sure at least 2-3 seconds between messages
     * @param message, a String object
     */
    public static void updateFeedback(String message) {
        statusMessage.setText(message);
    Timer timer = new Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            statusMessage.setText("");
        }
    });
    timer.setRepeats(false);
    timer.start();
    }



}
