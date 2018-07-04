package com.amrita.IIRMS;

/*
 * File Name        : IIRMSConfigurationInterface.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow) and Ramesh G (Junior Research Fellow),Ajeissh M (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 12th of October, 2015
 * Purpose          : Class to display the Configuration Interface of IIRMS system.
 */
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.imageio.ImageIO;

import com.amrita.IIRMS.DB.*;
import com.amrita.IIRMS.DB.Management.*;

public class IIRMSConfigurationInterface extends JFrame {

    //String list to contain the names of databases.
    public static java.util.List<String> namesofDB;
    //Declaration of menubar, menu and menuitems. (a huge number of them... ;) )
    public static JMenuBar menuBar;
    
    public static int Isc;
    
    public static JLabel img,backgroundImage,lConfig;
    public static Connection conn;
    public static ResultSet rs;
    public static Statement stmt;
    public static String[] dbNamesArray;
	private JButton Databaseadmin_IIRMS;
	private JButton Buildingadmin_IIRMS;
	private JPanel ConfigPanel;
	private ImageIcon dbAdminImage,buildAdminImage;
	public URL url;
	 

    /*
     * Method name              :   IIRMSConfigurationInterface
     * Method description       :   Constructor to define the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMSConfigurationInterface() throws Exception {

       //Defining button for Database admin && building admin
    	ConfigPanel = new JPanel();
        ConfigPanel.setLayout(null);
        
        lConfig=new JLabel("CONFIGURATION INTERFACE",SwingConstants.CENTER);
        lConfig.setBounds(15,0,1400,300);
        lConfig.setFont(new Font("Serif",Font.BOLD,60));
        lConfig.setForeground(Color.white);
        
        url=new URL("http://172.17.9.60/html/pics/background.jpg");
        ImageIcon icon1=new ImageIcon(url);
        Image bgImage=icon1.getImage();
        bgImage=bgImage.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);
        ImageIcon bgIcon=new ImageIcon(bgImage);
        backgroundImage=new JLabel(bgIcon);
        backgroundImage.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        
        
        
        url=new URL("http://172.17.9.60/html/pics/db-admin.png");
        ImageIcon icon2=new ImageIcon(url);
        Image img1=icon2.getImage();
        img1=img1.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        dbAdminImage=new ImageIcon(img1);
        
    	Databaseadmin_IIRMS = new JButton("Database Administrator",dbAdminImage);
    	Databaseadmin_IIRMS.setVerticalTextPosition(SwingConstants.CENTER);
    	Databaseadmin_IIRMS.setHorizontalTextPosition(SwingConstants.RIGHT);
    	Databaseadmin_IIRMS.setBounds(420, 400, 300, 50);
    	Databaseadmin_IIRMS.setToolTipText("Click to access tools to configure database and export/import IndoorGML");
    	//action listener
    	Databaseadmin_IIRMS.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 IIRMSDatabaseAdmin_UI databaseadmin= new IIRMSDatabaseAdmin_UI();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
    	
    	url=new URL("http://172.17.9.60/html/pics/build-admin.jpg");
    	ImageIcon icon3=new ImageIcon(url);
        Image img2=icon3.getImage();
        img2=img2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        buildAdminImage=new ImageIcon(img2);
    	
    	Buildingadmin_IIRMS = new JButton("Building Administrator",buildAdminImage);
    	Buildingadmin_IIRMS.setVerticalTextPosition(SwingConstants.CENTER);
    	Buildingadmin_IIRMS.setHorizontalTextPosition(SwingConstants.RIGHT);
    	Buildingadmin_IIRMS.setBounds(790, 400, 300, 50);
    	Buildingadmin_IIRMS.setToolTipText("Click to access tools to modify building information and sensor configuration");

    	  	Buildingadmin_IIRMS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                	IIRMSBuildingAdmin_UI buildingadmin= new IIRMSBuildingAdmin_UI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    	  	
       
        backgroundImage.add(Databaseadmin_IIRMS);
        backgroundImage.add(Buildingadmin_IIRMS);
        backgroundImage.add(lConfig);
        ConfigPanel.add(backgroundImage);
        
        //Getting the DB names, which will be used by other functionalities.
        namesofDB = getDBNames();
        dbNamesArray = namesofDB.toArray(new String[namesofDB.size()]);

        pack();
        add(ConfigPanel);
        setTitle("Configure UI");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
     * Method name              :   getDBNames
     * Method description       :   Method to get the names of the databases in the data repository
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   java.util.List<String> (dbNames)
     * Return type description  :   "dbNames"   --> List of databases (building)
     */
    public static java.util.List<String> getDBNames() throws Exception {
    	System.out.println("Prem");
        Class.forName("org.postgresql.Driver");
        //Establishing connection with the data repository
        System.out.println("state1");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/prem", "researcher", "researcher");
        System.out.println("state2");
        java.util.List<String> dbNames = new java.util.ArrayList<String>();
        //Query to get the list of databases from the data repository
        String query = "select datname from pg_database where datistemplate = FALSE and "
                + "datname like '%Building%' or "
                + "datname like '%building%'";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        dbNames.add(" ");
        while (rs.next()) {
            String inDB = rs.getString(1);
            System.out.println(inDB);
            dbNames.add(inDB);
        }
        return dbNames;
    }

    /*
     * Method name              :   processWindowEvent
     * Method description       :   Overriding the existing processWindowEvent() method,
     *                              to decide on the window event.
     * Method Arguments         :   WindowEvent (we)
     * Arguments description    :   "we"    --> To identify the type of event that has happened in window.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    @Override
    protected void processWindowEvent(WindowEvent we) {
        super.processWindowEvent(we); //To change body of generated methods, choose Tools | Templates.
        //Checking whether the event happened is "Close the Window" option.
        if (we.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
            try {
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(true);
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(true);
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } catch (Exception ex) {
            }
        }
    }
}