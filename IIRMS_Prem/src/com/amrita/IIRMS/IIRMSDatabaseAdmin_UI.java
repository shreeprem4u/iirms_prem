package com.amrita.IIRMS;
/*
 * File Name        : IIRMSDatabaseAdmin_UI.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : GUI design of Database Administrator in Configuration Interface  .
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.amrita.IIRMS.DB.DB_Script_File;
import com.amrita.IIRMS.DB.IndoorGML_To_DB;
import com.amrita.IIRMS.DB.Management.Add_Attribute;
import com.amrita.IIRMS.DB.Management.Delete_Attribute;
import com.amrita.IIRMS.DB.Management.View_Table_Information;
import com.amrita.IIRMS.IndoorGML.DB_To_IndoorGML_Tool;
import com.amrita.IIRMS.IndoorGML.SHP_To_IndoorGML_Tool;

public class IIRMSDatabaseAdmin_UI extends JFrame {
	
	
	
	private JPanel DadminPanel;
	private JButton Databaseadmin_cadtoshp;
	private JLabel DBpopulation,backgroundImage;
	private JButton Databaseadmin_shptiposgres;
	private JLabel DBModification;
	private JButton Databaseadmin_addatribute;
	private JButton Databaseadmin_delatribute;
	private JButton Databaseadmin_viewtable;
	private JLabel DBIndoorgml,ladmin,adminIcon;
	private JButton Databaseadmin_gmltodb;
	private JButton Databaseadmin_shptogml;
	private JButton Databaseadmin_dbtogml;
	private ImageIcon img_shpToDb,img_addAttr,img_deleteAttr,img_viewTable,img_igmlToDb,img_shpToIgml,img_dbToIgml;
	private URL url;
	

	public IIRMSDatabaseAdmin_UI() throws Exception
	{
			Color blue=new Color(100,191,219);
		 	DadminPanel = new JPanel();
	        DadminPanel.setLayout(null);
	        
	        url = new URL("http://172.17.9.60/html/pics/background1.png");
	        ImageIcon icon1=new ImageIcon(url);
	        Image bgImage=icon1.getImage();
	        bgImage=bgImage.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);
	        ImageIcon bgIcon=new ImageIcon(bgImage);
	        backgroundImage=new JLabel(bgIcon);
	        backgroundImage.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
	        
	        ladmin=new JLabel("Database Administrator");
	        ladmin.setBounds(1100, 30, 200, 20);
	        ladmin.setForeground(Color.WHITE);
	        
	        url=new URL("http://172.17.9.60/html/pics/db-admin.png");
	        ImageIcon icon2=new ImageIcon(url);
	        Image image=icon2.getImage();
	        image=image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        ImageIcon icc1 = new ImageIcon(image);
	        adminIcon = new JLabel(icc1);
	        adminIcon.setBounds(1030, 20,50 ,50 );
	        
	        DBpopulation= new JLabel("Database Creation");
	        DBpopulation.setBounds(10, 150, 300, 15);
	        DBpopulation.setFont(new Font("Serif",Font.BOLD,16));
	        DBpopulation.setForeground(Color.white);
	       
	        Databaseadmin_cadtoshp = new JButton("CAD to SHP");
	        Databaseadmin_cadtoshp.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_cadtoshp.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_cadtoshp.setBounds(10, 160, 280, 50);
	    	//action listener
	        Databaseadmin_cadtoshp.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        
	        url=new URL("http://172.17.9.60/html/pics/shp_to_postgres.png");
	        ImageIcon icon3=new ImageIcon(url);
	        Image img1=icon3.getImage();
	        img1=img1.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
	        img_shpToDb=new ImageIcon(img1);
	        
	        Databaseadmin_shptiposgres = new JButton("SHP to PostgreSQL",img_shpToDb);
	        Databaseadmin_shptiposgres.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_shptiposgres.setHorizontalTextPosition(SwingConstants.RIGHT);
	        //Databaseadmin_shptiposgres.setBounds(230, 30, 210, 180);
	        Databaseadmin_shptiposgres.setBounds(10, 180, 270, 70);
	        Databaseadmin_shptiposgres.setToolTipText("This helps to Create Database from Shapefiles of Building");

	        //Databaseadmin_shptiposgres.setBackground(blue);
	    	//action listener
	        Databaseadmin_shptiposgres.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 DB_Script_File dbsf = new DB_Script_File();
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        DBModification= new JLabel("Modify/view Database Structure");
	        DBModification.setBounds(10, 320, 330, 15);
	        DBModification.setFont(new Font("Serif",Font.BOLD,16));
	        DBModification.setForeground(Color.white);
	        
	        url=new URL("http://172.17.9.60/html/pics/add-attr.png");
	        ImageIcon icon4=new ImageIcon(url);
	        Image img2=icon4.getImage();
	        img2=img2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        img_addAttr=new ImageIcon(img2);
	        
	        Databaseadmin_addatribute = new JButton("Add Attribute",img_addAttr);
	        Databaseadmin_addatribute.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_addatribute.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_addatribute.setBounds(10, 350, 270, 70);
	        Databaseadmin_addatribute.setToolTipText("This helps to Create new attribute into Database");

	        //Databaseadmin_addatribute.setBackground(blue);
	    	//action listener
	        Databaseadmin_addatribute.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 Add_Attribute addAttr=new Add_Attribute();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        
	        url=new URL("http://172.17.9.60/html/pics/delete-attr.png");
	        ImageIcon icon5=new ImageIcon(url);
	        Image img3=icon5.getImage();
	        img3=img3.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        img_deleteAttr=new ImageIcon(img3);
	        
	        
	        Databaseadmin_delatribute = new JButton("Delete Attribute",img_deleteAttr);
	        Databaseadmin_delatribute.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_delatribute.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_delatribute.setBounds(300, 350, 270, 70);
	        Databaseadmin_delatribute.setToolTipText("This helps to Delete attribute from Database");

	        //Databaseadmin_delatribute.setBackground(blue);
	    	//action listener
	        Databaseadmin_delatribute.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 Delete_Attribute deleteAttr=new Delete_Attribute();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        
	        url=new URL("http://172.17.9.60/html/pics/view-table.png");
	        ImageIcon icon6=new ImageIcon(url);
	        Image img4=icon6.getImage();
	        img4=img4.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        img_viewTable=new ImageIcon(img4);
	        
	        Databaseadmin_viewtable = new JButton("View Table Structure",img_viewTable);
	        Databaseadmin_viewtable.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_viewtable.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_viewtable.setBounds(590, 350, 270, 70);
	        Databaseadmin_viewtable.setToolTipText("This helps to view Table structure from Database");

	        //Databaseadmin_viewtable.setBackground(blue);
	    	//action listener
	        Databaseadmin_viewtable.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 View_Table_Information vti = new View_Table_Information();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        DBIndoorgml= new JLabel("Export/Import of IndoorGML");
	        DBIndoorgml.setBounds(10, 530, 290, 15);
	        DBIndoorgml.setFont(new Font("Serif",Font.BOLD,16));
	        DBIndoorgml.setForeground(Color.white);
	        
	        url=new URL("http://172.17.9.60/html/pics/igml_to_db.png");
	        ImageIcon icon7=new ImageIcon(url);
	        Image img5=icon7.getImage();
	        img5=img5.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
	        img_igmlToDb=new ImageIcon(img5);
	        
	        Databaseadmin_gmltodb = new JButton("IndoorGML to DB",img_igmlToDb);
	        Databaseadmin_gmltodb.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_gmltodb.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_gmltodb.setBounds(10, 560, 270, 70);
	        Databaseadmin_gmltodb.setToolTipText("This helps to upload graph details to database from IndoorGML");

	        //Databaseadmin_gmltodb.setBackground(blue);
	    	//action listener
	        Databaseadmin_gmltodb.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 
	                	 IndoorGML_To_DB igml2db=new IndoorGML_To_DB();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        
	        url=new URL("http://172.17.9.60/html/pics/shp_to_igml.png");
	        ImageIcon icon8=new ImageIcon(url);
	        Image img6=icon8.getImage();
	        img6=img6.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
	        img_shpToIgml=new ImageIcon(img6);
	        
	        Databaseadmin_shptogml = new JButton("SHP to IndoorGML",img_shpToIgml);
	        Databaseadmin_shptogml.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_shptogml.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_shptogml.setBounds(300, 560, 270, 70);
	        Databaseadmin_shptogml.setToolTipText("This helps to Create IndoorGML file from Shapefiles of the Building");

	        //Databaseadmin_shptogml.setBackground(blue);
	    	//action listener
	        Databaseadmin_shptogml.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 SHP_To_IndoorGML_Tool shp2igml=new SHP_To_IndoorGML_Tool();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        
	        url=new URL("http://172.17.9.60/html/pics/db_to_igml.png");
	        ImageIcon icon9=new ImageIcon(url);
	        Image img7=icon9.getImage();
	        img7=img7.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
	        img_dbToIgml=new ImageIcon(img7);
	        
	        Databaseadmin_dbtogml = new JButton("DB to IndoorGML",img_dbToIgml);
	        Databaseadmin_dbtogml.setVerticalTextPosition(SwingConstants.CENTER);
	        Databaseadmin_dbtogml.setHorizontalTextPosition(SwingConstants.RIGHT);
	        Databaseadmin_dbtogml.setBounds(590, 560, 270, 70);
	        Databaseadmin_dbtogml.setToolTipText("This helps to create IndoorGML file from Database");

	        //Databaseadmin_dbtogml.setBackground(blue);
	    	//action listener
	        Databaseadmin_dbtogml.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent ae) {
	                 try {
	                	 DB_To_IndoorGML_Tool db2igml=new DB_To_IndoorGML_Tool();
	                	 
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         });
	        //DadminPanel.add(Databaseadmin_cadtoshp);
	        
	        DadminPanel.add(ladmin);
	        DadminPanel.add(adminIcon);
	        DadminPanel.add(DBpopulation);
	        DadminPanel.add(Databaseadmin_shptiposgres);
	        DadminPanel.add(DBModification);
	        DadminPanel.add(Databaseadmin_addatribute);
	        DadminPanel.add(Databaseadmin_delatribute);
	        DadminPanel.add(Databaseadmin_viewtable);
	        DadminPanel.add(DBIndoorgml);
	        DadminPanel.add(Databaseadmin_gmltodb);
	        DadminPanel.add(Databaseadmin_shptogml);
	        DadminPanel.add(Databaseadmin_dbtogml);
	        DadminPanel.add(backgroundImage);
	       
	        add(DadminPanel);
	        setTitle("Database Adminstrator UI");
	        setVisible(true);
	        setExtendedState(JFrame.MAXIMIZED_BOTH);
	        setMinimumSize(new Dimension(750, 500));
	    	
	}
}
