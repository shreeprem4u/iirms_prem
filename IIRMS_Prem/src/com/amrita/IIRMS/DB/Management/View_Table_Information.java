package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : View_Table_Information.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to view the information of a table in a database.
 */
import com.amrita.IIRMS.IIRMSConfigurationInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class View_Table_Information extends JFrame {

    public JLabel lBuilding, lTable;
    public JComboBox cBuilding, cTable;
    public JButton bOk, bReset, bCancel;
    public JPanel pViewTable;
    private JLabel lTitle;
    private ImageIcon helpIcon;

    /*
     * Method name              :   View_Table_Information
     * Method description       :   Constructor to define the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public View_Table_Information() throws Exception{
        DB_Load_Driver();
        pViewTable = new JPanel();
        pViewTable.setLayout(null);
        pViewTable.setBackground(Color.white);
        
        Color blue=new Color(147,221,236);
        lTitle=new JLabel("View Table Information",SwingConstants.CENTER);
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
        lhelp.setToolTipText("<html>This helps to view the table structure <br/>of the selected database</html>");

        lBuilding = new JLabel("Select the building");
        lBuilding.setBounds(50, 70, 200, 30);

        lTable = new JLabel("Select the table");
        lTable.setBounds(50, 130, 200, 30);

        cBuilding = new JComboBox(IIRMSConfigurationInterface.getDBNames().toArray());
        cBuilding.setBounds(300, 70, 200, 30);
        cBuilding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    pViewTable.remove(cTable);
                    Connection connIn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + cBuilding.getSelectedItem(),
                            "researcher", "researcher");
                    Statement stmtIn = connIn.createStatement();
                    String query = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_type='BASE TABLE'";
                    ResultSet rsIn = stmtIn.executeQuery(query);
                    java.util.List<String> tableNames = new java.util.ArrayList<String>();
                    tableNames.add(" ");
                    while (rsIn.next()) {
                        tableNames.add(rsIn.getString(1));
                    }
                    String[] tableNamesArray = tableNames.toArray(new String[tableNames.size()]);
                    cTable = new JComboBox(tableNamesArray);
                    cTable.setBounds(300, 120, 200, 30);
                    pViewTable.add(cTable);
                } catch (Exception ex) {
                }
            }
        });

        cTable = new JComboBox();
        cTable.setBounds(300, 120, 200, 30);

        bOk = new JButton("OK");
        bOk.setBounds(100, 190, 100, 25);
        bOk.setBackground(blue);
        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Connection connIn2 = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + cBuilding.getSelectedItem(),
                            "researcher", "researcher");
                    Statement stmtIn2 = connIn2.createStatement();
                    String query2 = "select column_name, data_type, character_maximum_length\n"
                            + "from INFORMATION_SCHEMA.COLUMNS where table_name = '" + cTable.getSelectedItem() + "'";
                    ResultSet rs2 = stmtIn2.executeQuery(query2);
                    JTable tableIn = new JTable(buildTableModel(rs2));
                    JOptionPane.showMessageDialog(null, new JScrollPane(tableIn), "IIRMS - Display table - '"
                            + cBuilding.getSelectedItem() + "/" + cTable.getSelectedItem() + "'", 1);
                } catch (Exception ex) {
                }
            }
        });

        bReset = new JButton("Reset");
        bReset.setBounds(400, 190, 100, 25);
        bReset.setBackground(blue);
        bReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cBuilding.setSelectedIndex(0);
                cTable.setSelectedIndex(0);
            }
        });

        bCancel = new JButton("Cancel");
        bCancel.setBounds(250, 190,100, 25);
        bCancel.setBackground(blue);
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        pViewTable.add(lhelp);
        pViewTable.add(lTitle);
        pViewTable.add(lBuilding);
        pViewTable.add(lTable);
        pViewTable.add(cBuilding);
        pViewTable.add(cTable);
        pViewTable.add(bOk);
        pViewTable.add(bReset);
        pViewTable.add(bCancel);

        add(pViewTable);
        setSize(650, 300);
        setVisible(true);
        setTitle("View Table Information");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /*
     * Method name              :   DB_Load_Driver
     * Method description       :   Method to load the driver of PostgreSQL database.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void DB_Load_Driver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
        }
    }

    /*
     * Method name              :   buildTableModel
     * Method description       :   Method to display the results of the table information in a table format.
     * Method Arguments         :   ResultSet (rs)
     * Arguments description    :   "rs"    --> Set of results parsed for the table.
     * Return type              :   DefaultTableModel (dtm)
     * Return type description  :   "dtm"   --> information modeled in a table format.
     */
    public static DefaultTableModel buildTableModel(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        DefaultTableModel dtm;

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        dtm = new DefaultTableModel(data, columnNames);
        return dtm;
    }
}
