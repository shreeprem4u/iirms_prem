package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : Add_Attribute.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class for adding an attribute to the table in a database.
 */
import com.amrita.IIRMS.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.sql.*;

public class Add_Attribute extends JFrame {

    public JLabel lBuilding, lTable, lColumnName, lColumnType, lLength;
    public JComboBox cBuilding, cTable, cColumnType, cTableNamesFinal;
    public JTextField tColumnName, tLength;
    public JButton bOk, bCancel, bReset;
    public JPanel pAttribute;
    public String[] tableNames = {" "};
    public String[] tableTypes = {" ", "CHARACTER VARYING", "INTEGER", "GEOMETRY", "DOUBLE PRECISION", "BOOLEAN"};
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    private JLabel lTitle;
    private ImageIcon helpIcon;

    /*
     * Method name              :   Add_Attribute
     * Method description       :   Constructor for defining the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Add_Attribute() throws Exception{
        LoadDB();
        pAttribute = new JPanel();
        pAttribute.setLayout(null);
        pAttribute.setBackground(Color.white);
        
        Color blue=new Color(147,221,236);
        lTitle=new JLabel("Modify database - Add Attribute",SwingConstants.CENTER);
        lTitle.setBounds(0,0,750,50);
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
        lhelp.setToolTipText("<html>This helps to modify the database <br/>by adding attributes<html> ");

        bOk = new JButton("OK");
        bOk.setBounds(100, 390, 100, 25);
        bOk.setBackground(blue);
        //Defining the action for "OK"
        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    if ((!cBuilding.getSelectedItem().equals(" ")) || (!cColumnType.getSelectedItem().equals(" ")) || (!tColumnName.getText().equals(" ")) || (!cTableNamesFinal.getSelectedItem().equals(" "))) {
                        Connection connOk = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem(),
                                "researcher", "researcher");
                        String query = "ALTER TABLE " + cTableNamesFinal.getSelectedItem() + " ADD COLUMN " + tColumnName.getText() + " " + cColumnType.getSelectedItem();
                        if (tLength.isEnabled()) {
                            query += "(" + tLength.getText() + ")";
                        }
                        System.out.println(query);
                        JOptionPane.showMessageDialog(null, query);
                        Statement stmtOk = connOk.createStatement();
                        stmtOk.executeUpdate(query);
                        JOptionPane.showMessageDialog(null, "Attribute added!", "IIRMS - Message", 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Some details are yet to be filled!", "IIRMS - Message", 2);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        bCancel = new JButton("Cancel");
        bCancel.setBounds(250, 390, 100, 25);
        bCancel.setBackground(blue);
        //Defining the action for "Cancel"
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });

        bReset = new JButton("Reset");
        bReset.setBounds(400, 390, 100, 25);
        bReset.setBackground(blue);
        //Defining the action for "Reset"
        bReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cBuilding.setSelectedIndex(0);
                cTableNamesFinal.setSelectedIndex(0);
                cColumnType.setSelectedIndex(0);
                tColumnName.setText("");
                tLength.setText("");
            }
        });

        //Panel to contain the form components.
        pAttribute.add(bOk);
        pAttribute.add(bCancel);
        pAttribute.add(bReset);

        lBuilding = new JLabel("Select the Database");
        lBuilding.setBounds(50, 90, 200, 30);

        cBuilding = new JComboBox(IIRMSConfigurationInterface.getDBNames().toArray());
        cBuilding.setBounds(300, 90, 200, 30);
        
        //Defining the action for database drop down box.
        //Once the database is selected, the tables from the database is retrieved.
        cBuilding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    System.out.println(cBuilding.getSelectedItem());
                    pAttribute.remove(cTableNamesFinal);
                    if (!cBuilding.getSelectedItem().equals(" ")) {
                        String connValue = "jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem();
                        System.out.println(connValue);
                        conn = DriverManager.getConnection(connValue,
                                "researcher", "researcher");
                        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_type='BASE TABLE'";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        System.out.println("\nThe tables are: ");
                        java.util.List<String> tableNamesList = new java.util.ArrayList<String>();
                        tableNamesList.add(" ");
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                            tableNamesList.add(rs.getString(1));
                        }
                        String[] tableNamesIn = tableNamesList.toArray(new String[tableNamesList.size()]);
                        cTableNamesFinal = new JComboBox(tableNamesIn);
                        //cTable.repaint();
                        //pAttribute.repaint();
                        cTableNamesFinal.setBounds(300, 150, 200, 30);
                        cTableNamesFinal.setVisible(true);
                        pAttribute.add(cTableNamesFinal);
                        conn.close();
                    }
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        });

        lTable = new JLabel("Table Name");
        lTable.setBounds(50, 150, 200, 30);

        cTableNamesFinal = new JComboBox(tableNames);
        cTableNamesFinal.setBounds(300, 150, 200, 30);
        
        pAttribute.add(cTableNamesFinal);

        lColumnName = new JLabel("Column Name");
        lColumnName.setBounds(50, 210, 200, 30);

        tColumnName = new JTextField(25);
        tColumnName.setBounds(300, 210, 200, 30);

        lColumnType = new JLabel("Column Type");
        lColumnType.setBounds(50, 270, 200, 30);

        cColumnType = new JComboBox(tableTypes);
        cColumnType.setBounds(300, 270, 200, 30);
        cColumnType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //"CHARACTER VARYING", "INTEGER", "GEOMETRY", "DOUBLE PRECISION", "BOOLEAN"};
                if (cColumnType.getSelectedItem().equals("CHARACTER VARYING")) {
                    tLength.setEnabled(true);
                } else {
                    tLength.setText(" ");
                    tLength.setEnabled(false);
                }
            }
        });

        lLength = new JLabel("Length");
        lLength.setBounds(50, 330, 200, 30);

        tLength = new JTextField(10);
        tLength.setBounds(300, 330, 200, 30);
        tLength.setEnabled(false);

        pAttribute.add(lhelp);
        pAttribute.add(lTitle);
        pAttribute.add(cBuilding);
        pAttribute.add(lLength);
        pAttribute.add(lColumnType);
        pAttribute.add(cColumnType);
        pAttribute.add(tColumnName);
        pAttribute.add(lColumnName);
        pAttribute.add(lTable);
        pAttribute.add(tLength);
        pAttribute.add(lBuilding);
        add(pAttribute);
        setTitle("Adding a New Attribute");
        setSize(700, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    

    /*
     * Method name              :   LoadDB
     * Method description       :   Method to load the driver of PostgreSQL database.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void LoadDB() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
        }
    }
}
