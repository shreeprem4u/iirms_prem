package com.amrita.IIRMS;

/*
 * File Name        : IIRMS.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow) and Ramesh G (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to start the execution of the whole IIRMS system.
 */
import java.io.*;
import java.net.URL;
import java.awt.*;

import javax.swing.*;
import javax.imageio.*;

import java.awt.event.*;

public class IIRMS {

    public static JFrame mainWindow_MainSystem;                                         //Frame containing the panel
    public static JButton navigationButton_MainSystem;   //Buttons for Navigation and Localization
    public static JPanel mainPanel1_MainSystem;                                         //Panel containing the buttons
    public static JLabel bui_MainSystem;                                                //Label to display the image of the building
    public static JLabel img_MainSystem;      
   public URL url;//Label to display the welcome image

    /*
     * Method name              :   IIRMS
     * Method description       :   Constructor to define the form components and their behaviors
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMS() throws Exception {
        //Defining the frame for the window.
        mainWindow_MainSystem = new JFrame("Indoor Information Representation and Management System");

        //Defining the panel for containing the form components and later added to the frame.
        mainPanel1_MainSystem = new JPanel();
        mainPanel1_MainSystem.setLayout(null);

        //Defining the button "Navigation"
        navigationButton_MainSystem = new JButton("Navigation");
        navigationButton_MainSystem.setBounds(550, 570, 200, 50);

        

        //Defining the side image
        url = new URL("http://172.17.9.60/html/pics/MockBuilding.jpeg");
        ImageIcon icc1 = new ImageIcon(url);
        bui_MainSystem = new JLabel(icc1);
        bui_MainSystem.setBounds(950, 150, 500, 1000);
        mainPanel1_MainSystem.add(bui_MainSystem);

        //Defining the background image
        url = new URL("http://172.17.9.60/html/pics/IIRMS.jpeg");
		ImageIcon icon = new ImageIcon(url);
		Image imgg = icon.getImage();

        imgg = imgg.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);

        ImageIcon icc = new ImageIcon(imgg);
        img_MainSystem = new JLabel(icc);
        img_MainSystem.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        
        //Adding the components to the panel
        mainPanel1_MainSystem.add(navigationButton_MainSystem);
        mainPanel1_MainSystem.add(img_MainSystem);

        //Adding the panel to the frame and defining frame properties
        mainWindow_MainSystem.add(mainPanel1_MainSystem);
        mainWindow_MainSystem.setVisible(true);
        mainWindow_MainSystem.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        mainWindow_MainSystem.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow_MainSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        


        //Defining the action for Navigation button. The action is redirected to the file
        //NavigationIIRMS.java
        navigationButton_MainSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IIRMSNavigationWindow navigationIIRMS = new IIRMSNavigationWindow();
                    mainWindow_MainSystem.setVisible(false);
                } catch (Exception ex) {
                }
            }
        });
    }

    /*
     * Method name              :   main
     * Method description       :   Starting point of execution.
     * Method Arguments         :   String array (args)
     * Arguments description    :   "args" to contain the dynamic inputs, if any. 
     *                              Possible only during command-line execution.
     * Return type              :   void
     * Return type description  :   returns nothing to the compiler.
     */
    public static void main(String[] args) throws Exception {
//        IIRMS ms = new IIRMS();
    	Dstevaluation dste=new Dstevaluation();
    }
}
