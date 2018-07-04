package com.amrita.IIRMS.IndoorGML.Graph;

/*
 * File Name        : IndoorGML_GraphInterface.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow)
 * Last Modified    : the 26th of March, 2015
 * Purpose          : Class to allow user to select building for IndoorGMLGraph Visualization.
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

public class IndoorGML_GraphInterface{
	
	public static JFrame fGraph;
	public static JPanel pGraph;
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
	private ImageIcon helpIcon;
	Color blue=new Color(147,221,236);
	public IndoorGML_GraphInterface() throws Exception
	{
		fGraph=new JFrame("IndoorGML Graph Display");
		pGraph=new JPanel();
		pGraph.setLayout(null);
		
		namesofDB=getDBNames();
		dbNamesArray=namesofDB.toArray(new String[namesofDB.size()]);
		
		lBuilding=new JLabel("Select the building");
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
					new IndoorGML_GraphMainVisualization();
					fGraph.hide();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        });
        lTitle=new JLabel("IndoorGML Graph Display",SwingConstants.CENTER);
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
        lhelp.setToolTipText("<html><center>This tool helps to Visualize graph such as adjacency, connectivity and "+ "<br> constraint graph of the selected Building.</html>");
        
        pGraph.add(lBuilding);
        pGraph.add(cDbName);
        pGraph.add(bOk);
        pGraph.add(lhelp);
        pGraph.add(lTitle);
        
        fGraph.add(pGraph);
        fGraph.setVisible(true);
        fGraph.setSize(450, 300);
        fGraph.setLocation(350, 200);
		
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

