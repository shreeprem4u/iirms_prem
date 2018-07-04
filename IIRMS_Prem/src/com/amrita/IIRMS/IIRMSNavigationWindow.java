package com.amrita.IIRMS;

/*
 * File Name        : IIRMSNavigationWindow.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow) and Ramesh G (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to display the Navigation Window.
 */
import java.awt.*;

import javax.swing.*;
import javax.imageio.*;

import java.io.*;
import java.net.URL;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class IIRMSNavigationWindow {

    public static JFrame mainWindow_NavigationIIRMS;
    public static JButton configInterface_NavigationIIRMS, applnInterface_NavigationIIRMS;
    public static JPanel mainPanel_NavigationIIRMS;
    public static JLabel img_NavigationIIRMS, bui_NavigationIIRMS,lWelcome,lsideImage;
	private Image imgbuild;
	private ImageIcon iccbuild;
	private JLabel lWelcome1;
	private JLabel lWelcome2;
	private JLabel lWelcome11;
	

    /*
     * Method name              :   IIRMSNavigationWindow
     * Method description       :   Constructor to define the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMSNavigationWindow() throws Exception {
        //Defining the frame to contain the display for Navigation option.
        mainWindow_NavigationIIRMS = new JFrame("Indoor Information Representation and Management System");

        lWelcome=new JLabel("<html><center>Indoor Information Representation and Management System</center> </html>",SwingConstants.CENTER);
        lWelcome.setBounds(10,0,1400,300);
        lWelcome.setFont(new Font("Serif",Font.BOLD,32));
        lWelcome.setForeground(Color.white);
        lWelcome1=new JLabel("<html> The project is funded by NRDMS, DST, India (NRDMS/11/1925/012) and developed by </html>",SwingConstants.CENTER);
        lWelcome1.setBounds(10,50,1400,300);
        lWelcome1.setFont(new Font("Serif",Font.BOLD,16));
        lWelcome1.setForeground(Color.white);
        lWelcome11=new JLabel("<html> Amrita Multidimensional Data Analytics Lab, Amrita University </html>",SwingConstants.CENTER);
        lWelcome11.setBounds(10,70,1400,300);
        lWelcome11.setFont(new Font("Serif",Font.BOLD,16));
        lWelcome11.setForeground(Color.white);
        lWelcome2=new JLabel("<html> (This Application is for DST Evaluation Purpose) </html>",SwingConstants.CENTER);
        lWelcome2.setBounds(10,100,1400,300);
        lWelcome2.setFont(new Font("Serif",Font.BOLD,16));
        lWelcome2.setForeground(Color.white);
        //lWelcome.setVerticalAlignment(SwingConstants.CENTER);
        
        //Defining the panel to contain the components.
        mainPanel_NavigationIIRMS = new JPanel();
        mainPanel_NavigationIIRMS.setLayout(null);
        
      //Defining the side image
        URL url = new URL("http://172.17.9.60/html/pics/mock_building.png");
        ImageIcon icon = new ImageIcon(url);
		Image image = icon.getImage();
		image=image.getScaledInstance(200, 280, Image.SCALE_SMOOTH);
        ImageIcon icc1 = new ImageIcon(image);
        lsideImage = new JLabel(icc1);
        lsideImage.setBounds(950, 150, 500, 1000);
        

        //Defining the button "Configure UI"
        URL url1=new URL("http://172.17.9.60/html/pics/config.png");
        ImageIcon icon1=new ImageIcon(url1);
        imgbuild = icon1.getImage();
		imgbuild = imgbuild.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		iccbuild = new ImageIcon(imgbuild);
        configInterface_NavigationIIRMS = new JButton("Configuration Interface",iccbuild);
        configInterface_NavigationIIRMS.setVerticalTextPosition(SwingConstants.CENTER);
        configInterface_NavigationIIRMS.setHorizontalTextPosition(SwingConstants.RIGHT);
        configInterface_NavigationIIRMS.setBounds(300, 430, 320, 50);
        configInterface_NavigationIIRMS.setToolTipText("Click here to do administration task such as creating database, modifiying attributes, Indoorgml creation");
        configInterface_NavigationIIRMS.setFont(new Font("Arial", Font.BOLD, 17));
        //Defining the side image.
        URL url2=new URL("http://172.17.9.60/html/pics/MockBuilding.jpeg");
        ImageIcon icon2=new ImageIcon(url2);
        Image img1 = icon2.getImage();
        ImageIcon icc2 = new ImageIcon(img1);
        bui_NavigationIIRMS = new JLabel(icc2);
        bui_NavigationIIRMS.setBounds(950, 150, 500, 1000);
       // mainPanel_NavigationIIRMS.add(bui_NavigationIIRMS);

        bui_NavigationIIRMS = new JLabel();

        //Defining the button "Application UI"
        URL url3=new URL("http://172.17.9.60/html/pics/appln.png");
        ImageIcon icon3=new ImageIcon(url3);
        imgbuild = icon3.getImage();
		imgbuild = imgbuild.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		iccbuild = new ImageIcon(imgbuild);
        applnInterface_NavigationIIRMS = new JButton("Application Interface",iccbuild);
        applnInterface_NavigationIIRMS.setVerticalTextPosition(SwingConstants.CENTER);
        applnInterface_NavigationIIRMS.setHorizontalTextPosition(SwingConstants.RIGHT);
        applnInterface_NavigationIIRMS.setBounds(780, 430, 320, 50);
        applnInterface_NavigationIIRMS.setToolTipText("Click here to access the Indoor Representation Tools");
        applnInterface_NavigationIIRMS.setFont(new Font("Arial", Font.BOLD, 17));
        //Defining the background image.
        
        URL url4=new URL("http://172.17.9.60/html/pics/background.jpg");
        ImageIcon icon4=new ImageIcon(url4);
        Image imgg = icon4.getImage();

        imgg = imgg.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);

        ImageIcon icc3 = new ImageIcon(imgg);
        img_NavigationIIRMS = new JLabel(icc3);
        img_NavigationIIRMS.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);

        img_NavigationIIRMS.add(lWelcome);
       // img_NavigationIIRMS.add(lWelcome1);
        //img_NavigationIIRMS.add(lWelcome11);
        //img_NavigationIIRMS.add(lWelcome2);
        mainPanel_NavigationIIRMS.add(lsideImage);
        mainPanel_NavigationIIRMS.add(configInterface_NavigationIIRMS);
        mainPanel_NavigationIIRMS.add(applnInterface_NavigationIIRMS);
        mainPanel_NavigationIIRMS.add(img_NavigationIIRMS);
        
        //mainPanel_NavigationIIRMS.add(lWelcome);
        

        mainWindow_NavigationIIRMS.add(mainPanel_NavigationIIRMS);
        mainWindow_NavigationIIRMS.setVisible(true);
        mainWindow_NavigationIIRMS.pack();
        mainWindow_NavigationIIRMS.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow_NavigationIIRMS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Defining the action for Application UI button
        applnInterface_NavigationIIRMS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IIRMSApplicationInterface applnInterface = new IIRMSApplicationInterface();
                    IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(false);
                } catch (Exception ex) {
                }
            }
        });
        //Defining the action for Configure UI button
        configInterface_NavigationIIRMS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IIRMSLogin loginPage = new IIRMSLogin();
                } catch (Exception ex) {
                }
            }
        });
        //Defining the process to be done when
        //some event happens with the window.
       
    }
    
    /*protected void paintComponent(Graphics g)
    {
    	g.drawImage(img, x, y, observer)
    	
    }*/
}