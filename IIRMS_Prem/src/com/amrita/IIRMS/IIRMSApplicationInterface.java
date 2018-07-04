package com.amrita.IIRMS;

/*
 * File Name        : IIRMSApplicationInterface.java
 * Project Name     : Indoor Information Representation and Management System
 * Author           : Ajeissh M (Junior Research Fellow) and Ramesh G (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to display the Application Interface of IIRMS system.
 */
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.*;

import java.awt.event.*;

import com.amrita.IIRMS.DB.DB_Script_File;
import com.amrita.IIRMS.DB.IndoorGML_To_DB;
import com.amrita.IIRMS.IndoorGML.*;
import com.amrita.IIRMS.IndoorGML.Graph.IndoorGML_GraphInterface;
import com.amrita.IIRMS.IndoorGML.Graph.IndoorGML_GraphMainVisualization;
import com.amrita.IIRMS.Navigation.*;
import com.amrita.IIRMS.Visualization.IIRMSQueryInterface;

public class IIRMSApplicationInterface extends JFrame {

    //Panel to contain all the Java Window Components
    public static JPanel applnPanel;
    //Label to display the main background image.
    public static JLabel img,backgroundImage,lQuery,lIgml,lAppln;
    
    public static JButton queryUI,igmlViewer,igmlGraph;
    //Connection, Statement, ResultSet objects to contain the database connection.
    public static Connection conn;
    public static Statement stmt;
    public static ResultSet rs;
    public static String dbNameSelected = null;
    public static JFileChooser fileChooser;
    public static ImageIcon img_queryUI,img_igmlViewer,img_igmlGraph;
    public URL url;
	private ImageIcon img_back;
	private JButton back;

    /*
     * Method name              :   getDBNames
     * Method description       :   Method to retrieve the names of databases (buildings) within the data repository.
     * Method Arguments         :   null
     * Arguments description    :   -
     * Return type              :   java.util.List<String>
     * Return type description  :   List of names of databases from the data repository.
     */
    public static java.util.List<String> getDBNames() throws Exception {
        Class.forName("org.postgresql.Driver");
        //Connection with the data repository
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/prem", "researcher", "researcher");
        java.util.List<String> dbNames = new ArrayList<String>();
        //Query to retrieve the names of the databases of the data repository
        String query = "select datname from pg_database where datistemplate = FALSE and "
                + "datname like '%Building%' or "
                + "datname like '%building%'";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            String inDB = rs.getString(1);
            System.out.println(inDB);
            dbNames.add(inDB);
        }
        return dbNames;
    }

    /*
     * Method name              :   IIRMSApplicationInterface
     * Method description       :   Constructor to initialize the class variables
     * Method Arguments         :   null
     * Arguments description    :   -
     * Return type              :   no return type
     * Return type description  :   -
     */
    public IIRMSApplicationInterface() throws Exception {

        //Define the panel and set "null" layout for customization of the window components.
        applnPanel = new JPanel();
        applnPanel.setLayout(null);
        
        lAppln=new JLabel("APPLICATION INTERFACE",SwingConstants.CENTER);
        lAppln.setBounds(15,0,1400,300);
        lAppln.setFont(new Font("Serif",Font.BOLD,55));
        lAppln.setForeground(Color.white);
        
        //Defining the background image.
        url=new URL("http://172.17.9.60/html/pics/background1.png");
        ImageIcon icon1=new ImageIcon(url);
        Image bgImage=icon1.getImage();
        bgImage=bgImage.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);
        ImageIcon bgIcon=new ImageIcon(bgImage);
        backgroundImage=new JLabel(bgIcon);
        backgroundImage.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        
        url=new URL("http://172.17.9.60/html/pics/indoor-query.png");
        ImageIcon icon2=new ImageIcon(url);
        Image img1=icon2.getImage();
        img1=img1.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
        img_queryUI=new ImageIcon(img1);
        
        lQuery= new JLabel("Indoor Querying");
        lQuery.setBounds(10, 300, 250, 15);
        lQuery.setFont(new Font("Serif",Font.BOLD,14));
        lQuery.setForeground(Color.white);
        
        queryUI = new JButton("Indoor Query Interface",img_queryUI);
        queryUI.setVerticalTextPosition(SwingConstants.CENTER);
        queryUI.setHorizontalTextPosition(SwingConstants.RIGHT);
        //Databaseadmin_shptiposgres.setBounds(230, 30, 210, 180);
        queryUI.setBounds(10, 330, 300, 70);
        queryUI.setToolTipText("Click to access Indoor Query Interface");
        //Databaseadmin_shptiposgres.setBackground(blue);
    	//action listener
        queryUI.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 IIRMSQueryInterface queryUI=new IIRMSQueryInterface();
                	 
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
        
        lIgml= new JLabel("IndoorGML");
        lIgml.setBounds(10, 470, 250, 15);
        lIgml.setFont(new Font("Serif",Font.BOLD,14));
        lIgml.setForeground(Color.white);
        
        url=new URL("http://172.17.9.60/html/pics/igml-docView.png");
        ImageIcon icon3=new ImageIcon(url);
        Image img2=icon3.getImage();
        img2=img2.getScaledInstance(65, 50, Image.SCALE_SMOOTH);
        img_igmlViewer=new ImageIcon(img2);
        
        igmlViewer = new JButton("IndoorGML Document Viewer",img_igmlViewer);
        igmlViewer.setVerticalTextPosition(SwingConstants.CENTER);
        igmlViewer.setHorizontalTextPosition(SwingConstants.RIGHT);
        //Databaseadmin_shptiposgres.setBounds(230, 30, 210, 180);
        igmlViewer.setBounds(10, 500, 330, 70);
        igmlViewer.setToolTipText("Click to View IndoorGML Document");

        //Databaseadmin_shptiposgres.setBackground(blue);
    	//action listener
        igmlViewer.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 IndoorGML_DocumentViewer docView=new IndoorGML_DocumentViewer();
                	 
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
        
        url=new URL("http://172.17.9.60/html/pics/igml-graph.png");
        ImageIcon icon4=new ImageIcon(url);
        Image img3=icon4.getImage();
        img3=img3.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
        img_igmlGraph=new ImageIcon(img3);
        
        igmlGraph = new JButton("IndoorGML Graph Display",img_igmlGraph);
        igmlGraph.setVerticalTextPosition(SwingConstants.CENTER);
        igmlGraph.setHorizontalTextPosition(SwingConstants.RIGHT);
        //Databaseadmin_shptiposgres.setBounds(230, 30, 210, 180);
        igmlGraph.setBounds(370, 500, 340, 70);
        igmlGraph.setToolTipText("Click to access IndoorGML Graph Interface");

        //Databaseadmin_shptiposgres.setBackground(blue);
    	//action listener
        igmlGraph.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 
                	 IndoorGML_GraphInterface graphUI=new IndoorGML_GraphInterface();
                	 
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
        
        

        applnPanel.add(lAppln);
       // applnPanel.add(back);
        applnPanel.add(lQuery);
        applnPanel.add(queryUI);
        applnPanel.add(lIgml);
        applnPanel.add(igmlViewer);
        applnPanel.add(igmlGraph);
        applnPanel.add(backgroundImage);
      

        //Adding the panel to the frame and defining frame properties
        add(applnPanel);
        setTitle("Application UI");
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(750, 500));
    }

    /*
     * Method name              :   processWindowEvent
     * Method description       :   Define the process to be done when working with a window.
     * Method Arguments         :   WindowEvent (we)
     * Arguments description    :   "we" to identify the type of event happened with the window.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    @Override
    protected void processWindowEvent(WindowEvent we) {
        super.processWindowEvent(we); //To change body of generated methods, choose Tools | Templates.
        //Checking whether the event that has happened is "Close the Window" choice
        if (we.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
            try {
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(true);
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(true);
                IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}