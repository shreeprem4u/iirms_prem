package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : Delete_Attribute.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class for deleting an attribute off the database.
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

public class Delete_Attribute extends JFrame {

    public JLabel lBuilding, lTable, lColumnName;
    public JComboBox cBuilding, cTable, cColumns;
    public JButton bOk, bCancel, bReset;
    public JPanel pRemAttribute;
    public String[] tableNames = {" "};
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    private JLabel lTitle;
    private ImageIcon helpIcon;

    /*
     * Method name              :   Delete_Attribute
     * Method description       :   Constructor to define the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Delete_Attribute() throws Exception {

        pRemAttribute = new JPanel();
        pRemAttribute.setLayout(null);
        pRemAttribute.setBackground(Color.white);
        
        Color blue=new Color(147,221,236);
        lTitle=new JLabel("Modify database - Delete Attribute",SwingConstants.CENTER);
        lTitle.setBounds(0,0,700,50);
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
        lhelp.setToolTipText("<html>This helps to modify the database <br/>by deleting attributes<html> ");

        bOk = new JButton("OK");
        bOk.setBounds(90, 320, 100, 25);
        bOk.setBackground(blue);
        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    if ((!cBuilding.getSelectedItem().equals(" "))) {
                        Connection connOk = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem(),
                                "researcher", "researcher");
                        String query = "ALTER TABLE " + cTable.getSelectedItem() + " DROP COLUMN " + cColumns.getSelectedItem() + " CASCADE";
                        System.out.println(query);
                        JOptionPane.showMessageDialog(null, query);
                        Statement stmtOk = connOk.createStatement();
                        stmtOk.executeUpdate(query);
                        JOptionPane.showMessageDialog(null, "Attribute deleted!", "IIRMS - Message", 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Some details are yet to be filled!", "IIRMS - Message", 2);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        bCancel = new JButton("Cancel");
        bCancel.setBounds(240, 320, 100, 25);
        bCancel.setBackground(blue);
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });

        bReset = new JButton("Reset");
        bReset.setBounds(390, 320, 100, 25);
        bReset.setBackground(blue);
        bReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cBuilding.setSelectedIndex(0);
                cColumns.setSelectedIndex(0);
                cTable.setSelectedIndex(0);
            }
        });

        pRemAttribute.add(bOk);
        pRemAttribute.add(bCancel);
        pRemAttribute.add(bReset);

        lBuilding = new JLabel("Select the building");
        lBuilding.setBounds(50, 90, 200, 30);

        lTable = new JLabel("Select the table");
        lTable.setBounds(50, 150, 200, 30);

        lColumnName = new JLabel("Select the attribute");
        lColumnName.setBounds(50, 210, 200, 30);

        cBuilding = new JComboBox(IIRMSConfigurationInterface.getDBNames().toArray());
        cBuilding.setBounds(300, 90, 200, 30);
        cBuilding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    pRemAttribute.remove(cTable);
                    System.out.println(cBuilding.getSelectedItem());
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
                        cTable = new JComboBox(tableNamesIn);
                        cTable.setBounds(300, 150, 200, 30);
                        cTable.setVisible(true);
                        pRemAttribute.add(cTable);
                        cTable.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ae2) {
                                try {
                                    pRemAttribute.remove(cColumns);
                                    String query2 = "SELECT column_name from information_schema.columns where table_name='" + cTable.getSelectedItem() + "'";
                                    Statement columnStmt = conn.createStatement();
                                    ResultSet columnRs = columnStmt.executeQuery(query2);
                                    java.util.List<String> columnList = new java.util.ArrayList<String>();
                                    columnList.add(" ");
                                    while (columnRs.next()) {
                                        columnList.add(columnRs.getString(1));
                                    }
                                    String[] columnsIn = columnList.toArray(new String[columnList.size()]);
                                    cColumns = new JComboBox(columnsIn);
                                    cColumns.setBounds(300, 200, 200, 30);
                                    cColumns.setVisible(true);
                                    pRemAttribute.add(cColumns);
                                } catch (Exception ex2) {
                                    ex2.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        });

        cTable = new JComboBox(tableNames);
        cTable.setBounds(300, 150, 200, 30);

        cColumns = new JComboBox(tableNames);
        cColumns.setBounds(300, 200, 200, 30);

        pRemAttribute.add(lhelp);
        pRemAttribute.add(lTitle);
        pRemAttribute.add(lBuilding);
        pRemAttribute.add(lTable);
        pRemAttribute.add(lColumnName);
        pRemAttribute.add(cTable);
        pRemAttribute.add(cBuilding);
        pRemAttribute.add(cColumns);

        add(pRemAttribute);
        setTitle("Deleting an Attribute");
        setSize(700, 450);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}
