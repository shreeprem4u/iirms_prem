package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : Modify_Attribute.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh kumar (Junior Research Fellow)
 * Last Modified    : the 18th of December, 2015
 * Purpose          : Class for Modify an attribute data to the table in a database.
 */
import com.amrita.IIRMS.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class Modify_Attribute extends JFrame {

    public JLabel lBuilding, lTable, lColumnName, lColumnType, lLength;
    public JComboBox cBuilding, cTable, cTableNamesFinal;
	JTextField cColumnType;
    JComboBox tColumnName;
	public JComboBox conditionstmt;
    public JButton bOk, bCancel, bReset;
    public JPanel pAttribute;
    public String[] tableNames = {"Door","Windows"};
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
	private Statement stmtFloor;
	private ResultSet rsFloor;
	private String[] floorValue;
	private String[] roomValue;
	private String[] roomTypeValue;
	private JComboBox marea;
	protected String table;
	protected String columnname;
	protected String query;
	protected String condition;
	protected String[] tableNamesIn;
	protected String[] material={"wood","glass","mixed","grill"};
	protected String[] atype={"single","double","sliding"};
	protected String[] ctype={"automatic","manual"};
	protected String[] columnNamesIn,conditionlistIn;
	protected String[] roomidValue;
	protected String[] wallidValue;
	protected String[] windowidValue;
	protected String[] exitidValue;
	private JLabel lLength1;
	 Color blue=new Color(147,221,236);
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
    public Modify_Attribute() throws Exception {
        LoadDB();
    
        pAttribute = new JPanel();
        pAttribute.setLayout(null);
        pAttribute.setBackground(Color.white);
        lTitle=new JLabel("Modifying Building Information",SwingConstants.CENTER);
        lTitle.setBounds(0,0,700,30);
        lTitle.setFont(new Font("Serif",Font.BOLD,15));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);
        
       
        URL url= new URL("http://172.17.9.60/html/pics/help.png");
        ImageIcon icon = new ImageIcon(url);
		Image helpImg = icon.getImage();
        helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        helpIcon=new ImageIcon(helpImg);
        JLabel lhelp=new JLabel(helpIcon);
        lhelp.setBounds(650, 10, 30, 30);
        lhelp.setToolTipText("<html><center>This tool helps to Modifiy the doors,windows type/material values in the Database ."
                + "<br/>1. Select the Building"
                + "<br/>2. Modifiy the attributes values."
                + "<br/>3. Click the button 'Ok'</center></html>");
		
        bOk = new JButton("OK");
        bOk.setBounds(50, 350, 100, 30);
        bOk.setBackground(blue);
        //Defining the action for "OK"
        bOk.addActionListener(new ActionListener() {
           

			@Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    if ((!cBuilding.getSelectedItem().equals(" ")) || (!cColumnType.getText().equals(" ")) || (!tColumnName.getSelectedItem().equals(" ")) || (!cTableNamesFinal.getSelectedItem().equals(" "))) {
                        Connection connOk = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem(),
                                "researcher", "researcher");
                        table=cTableNamesFinal.getSelectedItem().toString();
                        columnname=tColumnName.getSelectedItem().toString();
                       
                       
                       
                        if(conditionstmt.getSelectedItem().toString().equalsIgnoreCase("ALL"))
                        {
                        	  query = "UPDATE " + table + " SET " + columnname +"= '"+cColumnType.getText()+"'";	
                        }
                        else
                        {
                        	 query = "UPDATE " + table + " SET " + columnname +"= '"+cColumnType.getText()+ "' where "+ marea.getSelectedItem()+" = '" + conditionstmt.getSelectedItem()+"'";
                        }
                       
                        System.out.println(query);
                        JOptionPane.showMessageDialog(null, query);
                        Statement stmtOk = connOk.createStatement();
                        stmtOk.executeUpdate(query);
                        JOptionPane.showMessageDialog(null, "Attribute Modified!", "IIRMS - Message", 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Some details are yet to be filled!", "IIRMS - Message", 2);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        bCancel = new JButton("Cancel");
        bCancel.setBounds(200, 350, 100, 30);
        bCancel.setBackground(blue);
        //Defining the action for "Cancel"
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });

        bReset = new JButton("Reset");
        bReset.setBounds(350, 350, 100, 30);
        bReset.setBackground(blue);
        //Defining the action for "Reset"
        bReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cBuilding.setSelectedIndex(0);
                cTableNamesFinal.setSelectedIndex(0);
                cColumnType.setText("");
                marea.setSelectedIndex(0);
                tColumnName.setSelectedIndex(0);
                conditionstmt.setSelectedIndex(0);
            }
        });

        //Panel to contain the form components.
        pAttribute.add(bOk);
        pAttribute.add(bCancel);
        pAttribute.add(bReset);

        lBuilding = new JLabel("Select the Building");
        lBuilding.setBounds(50, 50, 200, 30);

        cBuilding = new JComboBox(IIRMSConfigurationInterface.getDBNames().toArray());
        cBuilding.setBounds(300, 50, 200, 30);
        
        //Defining the action for database drop down box.
        //Once the database is selected, the tables from the database is retrieved.
        cBuilding.addActionListener(new ActionListener() {
            

			@Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    System.out.println(cBuilding.getSelectedItem());
                    
                    
                    if (!cBuilding.getSelectedItem().equals(" ")) {
                        String connValue = "jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem();
                        System.out.println(connValue);
                        conn = DriverManager.getConnection(connValue,
                                "researcher", "researcher");
                        //tablenames
                        String query = "SELECT table_name FROM information_schema.tables WHERE table_name like '%room%' or table_name like '%exit%' or table_name like '%windows%' or table_name like 'wall%' ";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        System.out.println("\nThe tables are: ");
                        java.util.List<String> tableNamesList = new java.util.ArrayList<String>();
                        tableNamesList.add(" ");
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                            tableNamesList.add(rs.getString(1));
                        }
                        tableNamesIn = tableNamesList.toArray(new String[tableNamesList.size()]);
                        pAttribute.remove(cTableNamesFinal);                   
                        cTableNamesFinal = new JComboBox(tableNamesIn);
                        cTableNamesFinal.setBounds(300, 110, 200, 30);
                        pAttribute.add(cTableNamesFinal);
                        cTableNamesFinal.addActionListener(new ActionListener() {                                      			
						@Override
                            public void actionPerformed(ActionEvent ae) {
                				pAttribute.remove(tColumnName);  
                				 pAttribute.remove(marea);
                				if (!cBuilding.getSelectedItem().equals(" ")) {
                                    String connValue = "jdbc:postgresql://172.17.9.60:5432/" + (String) cBuilding.getSelectedItem();
                                    System.out.println(connValue);
                                    try {
                						conn = DriverManager.getConnection(connValue,      
                						        "researcher", "researcher");
                						String query = "SELECT distinct column_name FROM information_schema.columns WHERE table_name='" + cTableNamesFinal.getSelectedItem() + "' and (column_name like '%_type' or column_name like '%_material'or column_name like '%_name')";
                	                    stmt = conn.createStatement();      //or column_name like '%_material' or column_name like 'room_name'
                	                    rs = stmt.executeQuery(query);
                	                    System.out.println("\nThe columns are: ");
                	                    java.util.List<String> columnamelist = new java.util.ArrayList<String>();
                	                    columnamelist.clear();
                	                    columnamelist.add(" ");
                	                    while (rs.next()) {
                	                        System.out.println(rs.getString(1));
                	                        columnamelist.add(rs.getString(1));
                	                    }
                	                    columnNamesIn = columnamelist.toArray(new String[columnamelist.size()]);
                	                                     
                	                    tColumnName = new JComboBox(columnNamesIn);
                	                    tColumnName.setBounds(300, 170, 200, 30);
                	                    tColumnName.setVisible(true);
                	                    pAttribute.add(tColumnName);
                	                    String query1 = "SELECT distinct column_name FROM information_schema.columns WHERE table_name='" + cTableNamesFinal.getSelectedItem() + "'and (column_name like '%_type' or column_name like '%_material'or column_name like '%_name'or column_name like '%_id'or column_name like 'open_inward_dir')";
                	                    stmt = conn.createStatement();      //or column_name like '%_material' or column_name like 'room_name'
                	                    rs = stmt.executeQuery(query1);
                	                    System.out.println("\nThe columns are: ");
                	                    java.util.List<String> conditionlist = new java.util.ArrayList<String>();
                	                    conditionlist.clear();
                	                    conditionlist.add(" ");
                	                    while (rs.next()) {
                	                        System.out.println(rs.getString(1));
                	                        conditionlist.add(rs.getString(1));
                	                    }
                	                    conditionlistIn = conditionlist.toArray(new String[conditionlist.size()]);
                	                    marea = new JComboBox(conditionlistIn);
                	        	        marea.setBounds(120, 290, 100, 30);
                	        	        marea.setVisible(true);
                	        	        pAttribute.add(marea);
                	        	        marea.addActionListener(new ActionListener() {
                	        	        @Override
                	        	            public void actionPerformed(ActionEvent ae) {
                	        					//
                	        					String modifiyType=marea.getSelectedItem().toString();
                	        					pAttribute.remove(conditionstmt);
                	        					if(modifiyType.contains("material"))
                	        					{    
                	        						conditionstmt = new JComboBox(material);
                	        				       
                	        						
                	        					     
                	        					}
                	        					else if(modifiyType.contains("room_name") || modifiyType.contains("open_inward_dir")||modifiyType.contains("open_outward_dir"))
                	        					{     
                	        						conditionstmt = new JComboBox(roomValue);
                	        				        
                	        					        
                	        					}
                	        					else if(modifiyType.contains("close_type"))
                	        					{   
                	        						conditionstmt = new JComboBox(ctype);
                	        					}
                	        					else if(modifiyType.contains("floor_id"))
                	        					{
                	        						conditionstmt = new JComboBox(floorValue);
                	        					}
                	        					else if(modifiyType.contains("exit_type")|| modifiyType.contains("window_type"))
                	        					{
                	        						conditionstmt = new JComboBox(atype);
                	        					}
                	        					else if(modifiyType.contains("room_id"))
                	        					{
                	        						conditionstmt = new JComboBox(roomidValue);
                	        					}
                	        					else if(modifiyType.contains("wall_id"))
                	        					{
                	        						conditionstmt = new JComboBox(wallidValue);
                	        					}
                	        					else if(modifiyType.contains("exit_id"))
                	        					{
                	        						conditionstmt = new JComboBox(exitidValue);
                	        					}
                	        					else if(modifiyType.contains("window_id"))
                	        					{
                	        						conditionstmt = new JComboBox(windowidValue);
                	        					}
                	        					 conditionstmt.setBounds(300, 290, 200, 30);
                	        					  pAttribute.add(conditionstmt);
                	        					
                	        	            }
                	        	        });
                					} catch (SQLException e) {
                						// TODO Auto-generated catch block
                						e.printStackTrace();
                					}
                                    //tablenames
                                    
                				}
                				
                				
                            }
                        });
                     // List to contain the list of floors in the building.
                		java.util.List<Integer> floorList = new ArrayList<Integer>();
                		stmtFloor = conn.createStatement();
                		rsFloor = stmtFloor.executeQuery("select distinct floor_id from floor");
                		while (rsFloor.next()) {
                			floorList.add(rsFloor.getInt("floor_id"));
                		}
                		// String[] floorValue = (String[]) floorList.toArray();
                		Collections.sort(floorList);
                		// Sorted order of floor list with a "space" value in the beginning.
                		java.util.List<String> floorFinalValue = new ArrayList<String>();
                		floorFinalValue.add("All");
                		for (Integer inn : floorList) {
                			// System.out.println(inn);
                			floorFinalValue.add(Integer.toString(inn));
                		}

                	 floorValue = floorFinalValue.toArray(new String[floorFinalValue.size()]);
                               System.out.println(floorValue);
                              
                            //list of room_id
                       		java.util.List<Integer> roomidList = new ArrayList<Integer>();
                       		stmtFloor = conn.createStatement();
                       		rsFloor = stmtFloor.executeQuery("select distinct room_id from room");
                       		while (rsFloor.next()) {
                       			roomidList.add(rsFloor.getInt("room_id"));
                       		}
                       		// String[] floorValue = (String[]) floorList.toArray();
                       		Collections.sort(roomidList);
                       		// Sorted order of roomid list with a "space" value in the beginning.
                       		java.util.List<String> roomFinalValue = new ArrayList<String>();
                       		roomFinalValue.add("All");
                       		for (Integer inn : roomidList) {
                       			// System.out.println(inn);
                       			roomFinalValue.add(Integer.toString(inn));
                       		}

                       	 roomidValue = roomFinalValue.toArray(new String[roomFinalValue.size()]);
                       	 //list of wallid
                        	java.util.List<Integer> wallidList = new ArrayList<Integer>();
                    		stmtFloor = conn.createStatement();
                    		rsFloor = stmtFloor.executeQuery("select distinct wall_id from wall");
                    		while (rsFloor.next()) {
                    			wallidList.add(rsFloor.getInt("wall_id"));
                    		}
                    		// String[] floorValue = (String[]) floorList.toArray();
                    		Collections.sort(wallidList);
                    		// Sorted order of wallid list with a "space" value in the beginning.
                    		java.util.List<String> wallFinalValue = new ArrayList<String>();
                    		wallFinalValue.add("All");
                    		for (Integer inn : wallidList) {
                    			// System.out.println(inn);
                    			wallFinalValue.add(Integer.toString(inn));
                    		}

                    	 wallidValue = wallFinalValue.toArray(new String[wallFinalValue.size()]);
                    	 //list of windowid
                     	java.util.List<Integer> windowidList = new ArrayList<Integer>();
                 		stmtFloor = conn.createStatement();
                 		rsFloor = stmtFloor.executeQuery("select distinct window_id from windows");
                 		while (rsFloor.next()) {
                 			windowidList.add(rsFloor.getInt("window_id"));
                 		}
                 		// String[] floorValue = (String[]) floorList.toArray();
                 		Collections.sort(windowidList);
                 		// Sorted order of windowid list with a "space" value in the beginning.
                 		java.util.List<String> windowFinalValue = new ArrayList<String>();
                 		windowFinalValue.add("All");
                 		for (Integer inn : windowidList) {
                 			// System.out.println(inn);
                 			windowFinalValue.add(Integer.toString(inn));
                 		}

                 	 windowidValue = windowFinalValue.toArray(new String[windowFinalValue.size()]);
                 	 //list of doorid
                  	java.util.List<Integer> exitidList = new ArrayList<Integer>();
              		stmtFloor = conn.createStatement();
              		rsFloor = stmtFloor.executeQuery("select distinct exit_id from exit");
              		while (rsFloor.next()) {
              			exitidList.add(rsFloor.getInt("exit_id"));
              		}
              		// String[] floorValue = (String[]) floorList.toArray();
              		Collections.sort(exitidList);
              		// Sorted order of windowid list with a "space" value in the beginning.
              		java.util.List<String> exitFinalValue = new ArrayList<String>();
              		exitFinalValue.add("All");
              		for (Integer inn : exitidList) {
              			// System.out.println(inn);
              			exitFinalValue.add(Integer.toString(inn));
              		}

              	 exitidValue = exitFinalValue.toArray(new String[exitFinalValue.size()]);
                                      
                		
                		// List to contain the rooms in the building.
                 	Statement stmtRoom = conn.createStatement();
            		ResultSet rsRoom = stmtRoom.executeQuery("select distinct room_name from room");
                		java.util.List<String> roomDisplayList = new ArrayList<String>();
                		roomDisplayList.add("All");
                		while (rsRoom.next()) {
                			// System.out.println(rsRoom.getString("room_name"));
                			roomDisplayList.add(rsRoom.getString("room_name"));
                		}
                		roomValue = roomDisplayList.toArray(new String[roomDisplayList.size()]);
                		for (String ini : roomValue) {
                			// System.out.println(ini);
                		}	
                    }
                }
                    catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        });
       
        
        lTable = new JLabel("Select the Table");
        lTable.setBounds(50, 110, 200, 30);

        cTableNamesFinal = new JComboBox();
        cTableNamesFinal.setBounds(300, 110, 200, 30);
       
       

        lColumnName = new JLabel("Select the Attribute");
        lColumnName.setBounds(50, 170, 200, 30);
        String[] mtype={" ","material","type"};
        tColumnName = new JComboBox(mtype);
        tColumnName.setBounds(300, 170, 200, 30);
        

        lColumnType = new JLabel("Column value");
        lColumnType.setBounds(50, 230, 200, 30);
        cColumnType = new JTextField(15);
        cColumnType.setBounds(300, 230, 200, 30);
		pAttribute.add(cColumnType);
		     

        lLength = new JLabel("Where");
        lLength.setBounds(50, 290, 100, 30);
       
        String[] area={" ","room","floor"};
		 marea = new JComboBox(area);
	     marea.setBounds(120, 290, 100, 30);
	     
	     lLength1 = new JLabel("equals");
	     lLength1.setBounds(230, 290, 100, 30);
       
        conditionstmt = new JComboBox();
        conditionstmt.setBounds(300, 290, 200, 30);
       
        pAttribute.add(lhelp);
        pAttribute.add(lTitle);
        pAttribute.add(cBuilding);
        pAttribute.add(cTableNamesFinal);
        pAttribute.add(lLength);
        pAttribute.add(lLength1);
        pAttribute.add(lColumnType);
        
        pAttribute.add(tColumnName);
        pAttribute.add(lColumnName);
        pAttribute.add(lTable);
        pAttribute.add(conditionstmt);
        pAttribute.add(marea);
        pAttribute.add(lBuilding);
        add(pAttribute);
        setTitle("Modify Building Configuration");
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
