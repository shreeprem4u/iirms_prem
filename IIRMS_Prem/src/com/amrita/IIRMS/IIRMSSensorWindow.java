package com.amrita.IIRMS;

/*
 * File Name        : IIRMSSensorWindow.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow)
 * Last Modified    : the 19th of October, 2015
 * Purpose          : Class to allow user to select the building for configure sensor.
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
import java.sql.Statement;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.amrita.IIRMS.DB.Management.SensorModeling;



public class IIRMSSensorWindow {
	
	public static JFrame fSensor;
	public static JPanel pSensor;
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
	
	public IIRMSSensorWindow() throws Exception
	{
		fSensor=new JFrame("Sensor Configuration");
		pSensor=new JPanel();
		pSensor.setLayout(null);
		
		namesofDB=getDBNames();
		dbNamesArray=namesofDB.toArray(new String[namesofDB.size()]);
		
		lBuilding=new JLabel("Select the Building");
		lBuilding.setBounds(50, 100, 250, 25);
        
        cDbName=new JComboBox(dbNamesArray);
        cDbName.setBounds(210, 100, 200, 30);
        
        cDbName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dbNameSelected=cDbName.getSelectedItem().toString();
				System.out.println(cDbName.getSelectedItem());
			}
        });
        
        bOk=new JButton("OK");
        bOk.setBounds(170, 185, 100, 30);
        bOk.setBackground(blue);
        bOk.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new SensorModeling();
					fSensor.hide();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        });
        lTitle=new JLabel("Configuring Sensor In Building",SwingConstants.CENTER);
        lTitle.setBounds(0,0,450,30);
        lTitle.setFont(new Font("Serif",Font.BOLD,15));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);
        URL url= new URL("http://172.17.9.60/html/pics/help.png");
        ImageIcon icon = new ImageIcon(url);
		Image helpImg = icon.getImage();
        helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        helpIcon=new ImageIcon(helpImg);
        JLabel lhelp=new JLabel(helpIcon);
        lhelp.setBounds(400, 10, 30, 30);
        lhelp.setToolTipText("<html><center>This tool helps to Configuire the sensor in the Building </center></html>");
       
        pSensor.add(lBuilding);
        pSensor.add(lhelp);
        pSensor.add(lTitle);
        pSensor.add(cDbName);
        pSensor.add(bOk);
		fSensor.add(pSensor);
		fSensor.setVisible(true);
		fSensor.setSize(450, 300);
		fSensor.setLocation(350, 200);
		
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
