package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : SHP_To_IndoorGML_Tool.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow), Dinesh Kumar E (Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          :GUI design of shapefile to IndoorGML in Database AdminstratorUI.
 */
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URL;

import com.amrita.IIRMS.*;
import com.amrita.IIRMS.DB.*;

public class SHP_To_IndoorGML_Tool {

    public static JFrame fIndoorGML;
    public static JPanel pIndoorGML;
    public static JLabel lIntro,lShp;
    public static JButton bBrowse, bDocument;
    public static JTextField tLocation; 
    public static Connection conn;
    public static Statement stmt;
    public static ResultSet rs;
    private ImageIcon helpIcon;
    private JLabel lTitle;
    Color blue=new Color(147,221,236);
    /*
     * Method name              :   IndoorGML_Tool
     * Method description       :   Constructor for defining form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public SHP_To_IndoorGML_Tool() throws Exception {

        

        fIndoorGML = new JFrame("IndoorGML Instance Document Generation Tool");

        pIndoorGML = new JPanel();
        pIndoorGML.setBackground(Color.WHITE);
        pIndoorGML.setLayout(null);


        lTitle=new JLabel("Import IndoorGML from Shapefiles",SwingConstants.CENTER);
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
        lhelp.setToolTipText("<html><center>This tool helps to generate the IndoorGML Instance Document."
                + "<br/>1. Select the shapefiles"
                + "<br/>2. Click the button 'IndoorGML Document' to create the document</center></html>");
        
        
        lShp=new JLabel("Select the shape files");
        lShp.setBounds(80,100,200,20);
        tLocation=new JTextField(350);
        tLocation.setBounds(260, 100, 190, 20);
        tLocation.setEditable(false);

        bBrowse = new JButton("Browse");
        bBrowse.setBounds(470, 100, 100, 20);
        bBrowse.setBackground(blue);
        bBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                IndoorGML_File_Parser indoorGML_File_Parser;
                try {
                    indoorGML_File_Parser = new IndoorGML_File_Parser();
                    bDocument.setEnabled(true);
                    tLocation.setText(indoorGML_File_Parser.filePathForIndoorGML);
                    fIndoorGML.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        bDocument = new JButton("Create IndoorGML Document");
        bDocument.setBackground(blue);
        bDocument.setBounds(140, 160, 250, 20);

        bDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                	System.out.println("Start Process step 1");
                    IndoorGML_Creation_From_SHP igmlC = new IndoorGML_Creation_From_SHP(IndoorGML_File_Parser.filePathForIndoorGML);
                    System.out.println("Step 2");
                    System.out.println("CHECK file parser: "+IndoorGML_File_Parser.filePathForIndoorGML);
                    System.out.println("CHECK file parser: "+IndoorGML_File_Parser.roomList);
                    igmlC.XML_Creation();
                    System.out.println("Step 3");
                    String pathValue=tLocation.getText()+"/IndoorGML_Document.xml";
					File file = new File(pathValue);
                	Desktop desktop = Desktop.getDesktop();
        			desktop.open(file);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        pIndoorGML.add(lhelp);
        pIndoorGML.add(lTitle);
        pIndoorGML.add(lShp);
        pIndoorGML.add(tLocation);
        pIndoorGML.add(bBrowse);
        pIndoorGML.add(bDocument);
        fIndoorGML.add(pIndoorGML);
        fIndoorGML.setVisible(true);
        fIndoorGML.setSize(650, 300);
        fIndoorGML.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                fIndoorGML.setVisible(true);
                fIndoorGML.setEnabled(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                fIndoorGML.setVisible(true);
                fIndoorGML.setEnabled(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                fIndoorGML.setVisible(false);
                fIndoorGML.setEnabled(false);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                fIndoorGML.setVisible(true);
                fIndoorGML.setEnabled(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                fIndoorGML.setVisible(true);
                fIndoorGML.setEnabled(true);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                fIndoorGML.setVisible(true);
                fIndoorGML.setEnabled(true);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                fIndoorGML.setEnabled(false);
            }
        });
    }
}