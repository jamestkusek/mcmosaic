package com.personal.james;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AppGUI {

    public static JLabel transformReport;
    public static JFrame generate() {

        JFrame appGUI = new JFrame();
        appGUI.setTitle("Minecraft Mosaic Maker");
        appGUI.setLayout(new BorderLayout());
        appGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appGUI.setResizable(true);
        appGUI.setSize(500,500);
        appGUI.setMinimumSize(new Dimension(500,500));
        appGUI.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        ImageIcon image = new ImageIcon("src/main/resources/appicon.png");
        appGUI.setIconImage(image.getImage());
        appGUI.setVisible(true);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255,0,0));
        titlePanel.setPreferredSize(new Dimension(400,75));
        JLabel title = new JLabel();
        title.setText("Minecraft Mosaic Maker");
        title.setFont(new Font("Comic Sans MS",Font.PLAIN, 36));
        titlePanel.add(title);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0,255,0));
        buttonsPanel.setPreferredSize(new Dimension(400,350));
        buttonsPanel.setLayout(new GridLayout(3,1));

        JPanel bottomPanel = new JPanel();
        transformReport = new JLabel();
        bottomPanel.setBackground(new Color(0,0,255));
        bottomPanel.setPreferredSize(new Dimension(400,50));
        bottomPanel.add(transformReport);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(127,0,127));
        leftPanel.setPreferredSize(new Dimension(50,400));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0,127,127));
        rightPanel.setPreferredSize(new Dimension(50,400));

        JButton button1 = new JButton();
        button1.setBackground(new Color(0,110,0));
        button1.setPreferredSize(new Dimension(300,50));
        button1.addActionListener(e ->Application.transformAllImages());
        button1.setText("Transform all images");


        JButton button2 = new JButton();
        button2.setBackground(new Color(200,200,0));
        button2.setPreferredSize(new Dimension(300,50));
        button2.addActionListener(e -> {try{
            Desktop.getDesktop().open(new File("images/input"));
        }
        catch (IOException z) {
            z.printStackTrace();
        }});
        button2.setText("Open input folder");

        JButton button3 = new JButton();
        button3.setBackground(new Color(54,200,179));
        button3.setPreferredSize(new Dimension(300,50));
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

    public static void reportInfo(String text) {
        transformReport.setText(text);
    }

}
