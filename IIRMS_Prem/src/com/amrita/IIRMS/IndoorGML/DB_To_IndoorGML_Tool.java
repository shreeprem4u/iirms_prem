package com.amrita.IIRMS.IndoorGML;
/*
 * File Name        : DB_To_IndoorGML_Tool.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : GUI design of database to IndoorGML in Database AdminstratorUI.
 */
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DB_To_IndoorGML_Tool {
	
	public static JFrame frameIndoorGML;
	public static JPanel panelIndoorGML;
	public static JLabel lDatabase,lLocation;
	public static JButton bOk,bBrowse;
	public static JComboBox cDatabase;
	public static JFileChooser fileChooser;
	public static String[] dbNamesArray;
	public static java.util.List<String> dbNameList;
	public static JTextField tLocation;
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	private ImageIcon helpIcon;
    private JLabel lTitle;
    Color blue=new Color(147,221,236);
	public DB_To_IndoorGML_Tool() throws Exception{
		
		frameIndoorGML=new JFrame("IndoorGML Instance Document Generation Tool");
		panelIndoorGML=new JPanel();
		panelIndoorGML.setBackground(Color.WHITE);
		panelIndoorGML.setLayout(null);
		lTitle=new JLabel("Import IndoorGML from Database",SwingConstants.CENTER);
        lTitle.setBounds(0,0,650,50);
        lTitle.setFont(new Font("Serif",Font.BOLD,18));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);
        
       
        URL url= new URL("http://172.17.9.60/html/pics/help.png");
        ImageIcon icon = new ImageIcon(url);
		Image helpImg = icon.getImage();
        helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        helpIcon=new ImageIcon(helpImg);
        JLabel lhelp=new JLabel(helpIcon);
        lhelp.setBounds(600, 10, 30, 30);
        lhelp.setToolTipText("<html><center>This tool helps to generate the IndoorGML Document From Database."
                + "<br/>1. Select the Database"
                + "<br/>2. Click the 'Browse' button  to specify the  location to store the IndoorGML document."
                + "<br/>3. Click the button 'Ok'</center></html>");
		
		lDatabase=new JLabel("Select the database");
		lDatabase.setBounds(60,140,250,20);
		
		dbNameList=getDBNames();
		dbNamesArray=dbNameList.toArray(new String[dbNameList.size()]);
		cDatabase=new JComboBox(dbNamesArray);
		cDatabase.setBounds(250, 140, 200, 20);
		
		lLocation=new JLabel("Select the location");
		lLocation.setBounds(60,200,350,20);
		
		tLocation=new JTextField(15);
		tLocation.setBounds(250, 200, 200, 20);
		tLocation.setEditable(false);
		bBrowse=new JButton("Browse");
		bBrowse.setBackground(blue);
		bBrowse.setBounds(480, 200, 100, 20);
		bBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser=new JFileChooser("/home/Documents");
				fileChooser.setDialogTitle("Location to store IndoorGML file");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					String selectedFile=fileChooser.getSelectedFile().getPath();
					tLocation.setText(selectedFile);
				}
				
				
			}
			
		});
		
		bOk=new JButton("OK");
		bOk.setBackground(blue);
		bOk.setBounds(250, 270, 100, 20);
		bOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					IndoorGML_DB_Parser dbParser=new IndoorGML_DB_Parser(cDatabase.getSelectedItem().toString());
					IndoorGML_Creation_From_DB igmlCreator=new IndoorGML_Creation_From_DB(tLocation.getText());
					igmlCreator.XML_Creation();
					String pathValue=tLocation.getText()+"/IndoorGML_Document.xml";
					File file = new File(pathValue);
                	Desktop desktop = Desktop.getDesktop();
        			desktop.open(file);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		panelIndoorGML.add(lhelp);
		panelIndoorGML.add(lTitle);
		panelIndoorGML.add(lDatabase);
		panelIndoorGML.add(cDatabase);
		panelIndoorGML.add(lLocation);
		panelIndoorGML.add(tLocation);
		panelIndoorGML.add(bBrowse);
		panelIndoorGML.add(bOk);
		frameIndoorGML.add(panelIndoorGML);
		frameIndoorGML.setVisible(true);
		frameIndoorGML.setSize(650, 400);
	}

	private List<String> getDBNames() throws Exception {
		
		java.util.List<String> dbNames=new java.util.ArrayList<String>();
		Class.forName("org.postgresql.Driver");
		conn=DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/","researcher","researcher");
		String query="select datname from pg_database where datistemplate=FALSE and "
				+"datname like '%Building%' or "
				+"datname like '%building%'";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(query);
		dbNames.add("  ");
		while(rs.next())
		{
			String inDB=rs.getString(1);
			dbNames.add(inDB);
		}
		
		return dbNames;
	}
}