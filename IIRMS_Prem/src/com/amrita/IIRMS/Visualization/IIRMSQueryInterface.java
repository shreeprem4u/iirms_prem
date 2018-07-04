package com.amrita.IIRMS.Visualization;
/*
 * File Name        : IIRMSQueryInterface.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow)
 * Last Modified    : the 26th of March, 2015
 * Purpose          : Class to allow user to select building for 2D and 3D Visualization and Querying.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.amrita.IIRMS.IIRMSApplicationInterface;
import com.amrita.IIRMS.IIRMSMainVisualization;
import com.amrita.IIRMS.DB.Management.SensorModeling;

public class IIRMSQueryInterface {
	
	public static JFrame fQuery;
	public static JPanel pQuery;
	public static JLabel lBuilding;
	public static JComboBox cDbName;
	public static JButton bOk;
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	public static List<String> namesofDB;
	public static String[] dbNamesArray;
	public static String dbNameSelected;
	private JLabel lTitle;
	Color blue=new Color(147,221,236);
	private ImageIcon helpIcon;
	public IIRMSQueryInterface() throws Exception
	{
		fQuery=new JFrame("Indoor Query Interface");
		pQuery=new JPanel();
		pQuery.setLayout(null);
		
		namesofDB=getDBNames();
		dbNamesArray=namesofDB.toArray(new String[namesofDB.size()]);
		
		lTitle=new JLabel("Indoor Query Interface",SwingConstants.CENTER);
        lTitle.setBounds(0,0,450,50);
        lTitle.setFont(new Font("Serif",Font.BOLD,18));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);
        URL url= new URL("http://172.17.9.60/html/pics/help.png");
        ImageIcon icon = new ImageIcon(url);
		Image helpImg = icon.getImage();
        helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        helpIcon=new ImageIcon(helpImg);
        JLabel lhelp=new JLabel(helpIcon);
        lhelp.setBounds(400, 10, 30, 30);
        lhelp.setToolTipText("<html><center>This tool helps to Visualize the Selected Building and Querying"+" <br>the details about the Building.</html>");
		lBuilding=new JLabel("Select the Building");
		lBuilding.setBounds(50, 100, 250, 25);
        
        cDbName=new JComboBox(dbNamesArray);
        cDbName.setBounds(210, 100, 200, 30);
        
        cDbName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dbNameSelected=cDbName.getSelectedItem().toString();
				
			}
        });
        
        bOk=new JButton("OK");
        bOk.setBackground(blue);
        bOk.setBounds(170, 185, 100, 30);
        bOk.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Connection conn=null;
			        int floor_num=0,room_num=0,window_num=0,room_corridor=0;
			        
			        	try {
							Class.forName("org.postgresql.Driver");
							conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/"+dbNameSelected, "researcher", "researcher");
						} catch (ClassNotFoundException ce) {
							ce.printStackTrace();
						} catch (SQLException se) {
							se.printStackTrace();
						}
			        	
			        try
			        {
			        	String queryBuilding="select count(floor_id) from floor";
			        	Statement stmtBuilding = conn.createStatement();
			        	ResultSet rsBuilding=stmtBuilding.executeQuery(queryBuilding);
			        	while(rsBuilding.next())
			        	{
			        		floor_num=rsBuilding.getInt(1);
			        	}
			        	
			        	String queryRoom1="select count(room_id) from room where room_type LIKE '%orridor%'";
			        	Statement stmtRoom1 = conn.createStatement();
			        	ResultSet rsRoom1=stmtRoom1.executeQuery(queryRoom1);
			        	while(rsRoom1.next())
			        	{
			        		room_corridor=rsRoom1.getInt(1);
			        	}
			        	
			        	String queryRoom="select count(room_id) from room";
			        	Statement stmtRoom = conn.createStatement();
			        	ResultSet rsRoom=stmtRoom.executeQuery(queryRoom);
			        	while(rsRoom.next())
			        	{
			        		int rooms=rsRoom.getInt(1);
			        		room_num=rooms-room_corridor;
			        	}
			        	
			        	String queryWindow="select count(window_id) from windows";
			        	Statement stmtWindow = conn.createStatement();
			        	ResultSet rsWindow=stmtWindow.executeQuery(queryWindow);
			        	while(rsWindow.next())
			        	{
			        		window_num=rsWindow.getInt(1);
			        	}
					} catch (SQLException sqlExp) {
						sqlExp.printStackTrace();
					}
			        
			        
			        String heading="<html><center> Building Information<center>";
			        String buildName="<br/><p>The name of the selected building is "+dbNameSelected+".";
			        String text="<br/>This building consists of "+floor_num+" floors ,"+room_num+" rooms and "+window_num+" windows.</p>";
			        String message=heading+buildName+text;
			        JOptionPane.showMessageDialog(null,message , "IIRMS - Message", 1);
			        try {
			            //Visualization is done in this file.
			            IIRMSMainVisualization mainExecution = new IIRMSMainVisualization();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			        fQuery.hide();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        });
        
        pQuery.add(lBuilding);
        pQuery.add(cDbName);
        pQuery.add(bOk);
        pQuery.add(lhelp);
        pQuery.add(lTitle);
		fQuery.add(pQuery);
		fQuery.setVisible(true);
		fQuery.setSize(450, 300);
		fQuery.setLocation(350, 200);
		
	}
	
	
	public static java.util.List<String> getDBNames() throws Exception {
        Class.forName("org.postgresql.Driver");
        //Establishing connection with the data repository
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/", "researcher", "researcher");
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

}


