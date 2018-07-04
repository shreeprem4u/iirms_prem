package com.amrita.IIRMS.IndoorGML;
/*
 * File Name        : IndoorGML_DocumentViewer.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E(Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : Class contains GUI and functionality Display IndoorGML document
 */
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.ibatis.jdbc.ScriptRunner;

public class IndoorGML_DocumentViewer {
	
private JFrame inFrame;
private JPanel inPanel;
private JTextField tFile;
private JLabel lFile;
private JButton bBrowse;
private JFileChooser jfc;
private JLabel lTitle;
public static FileNameExtensionFilter fnef = new FileNameExtensionFilter("IndoorGML files", "xml");
Color blue=new Color(147,221,236);
private ImageIcon helpIcon;
public IndoorGML_DocumentViewer() throws MalformedURLException{
	 inFrame = new JFrame("IndoorGML Document Viewer");

     //Setting layout for customization of form components.
     inPanel = new JPanel();
     inPanel.setLayout(null);

     lFile = new JLabel("<html><center>Select the location of the IndoorGML File </center></html>");
     lFile.setBounds(50, 120, 500, 20);  
     bBrowse = new JButton("Browse");
     bBrowse.setBounds(360, 120, 100, 20);
     bBrowse.setBackground(blue);
     jfc = new JFileChooser("/home/Documents");
     //Defining the action for "Browse" button.
     bBrowse.addActionListener(new ActionListener() {
        

		@Override
         public void actionPerformed(ActionEvent ae) {
			jfc.setFileFilter(fnef);
			jfc.showOpenDialog(null);
     		File indoorFile=jfc.getSelectedFile();
     		String pathValue = indoorFile.getPath();
     		File file = new File(pathValue);
        	Desktop desktop = Desktop.getDesktop();
        	try {
				desktop.open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
         }
     });
     
     lTitle=new JLabel("IndoorGML Document Viewer",SwingConstants.CENTER);
     lTitle.setBounds(0,0,600,50);
     lTitle.setFont(new Font("Serif",Font.BOLD,18));
     lTitle.setBackground(blue);
     lTitle.setOpaque(true);
     URL url= new URL("http://172.17.9.60/html/pics/help.png");
     ImageIcon icon = new ImageIcon(url);
      Image helpImg = icon.getImage();
     helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
     helpIcon=new ImageIcon(helpImg);
     JLabel lhelp=new JLabel(helpIcon);
     lhelp.setBounds(550, 10, 30, 30);
     lhelp.setToolTipText("<html><center>This tool helps to view Selected IndoorGML Document.</html>");
    

     //Adding the form components to the panel.
     inPanel.add(lFile);  
     inPanel.add(bBrowse);
     inPanel.setBackground(Color.WHITE);
     inPanel.add(lhelp);
     inPanel.add(lTitle);
     //Adding the panel to the frame.
     inFrame.add(inPanel);
     inFrame.setMinimumSize(new java.awt.Dimension(600, 300));
     inFrame.setSize(600, 300);
     inFrame.setLocation(200, 200);
     inFrame.setVisible(true);
	
}
}