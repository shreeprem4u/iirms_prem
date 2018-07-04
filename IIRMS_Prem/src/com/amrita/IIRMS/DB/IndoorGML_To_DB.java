package com.amrita.IIRMS.DB;

/*
 * File Name        : IndoorGML_To_DB.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to parse the data from IndoorGML Instance Document
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.*;

import com.amrita.IIRMS.IIRMSApplicationInterface;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.*;

public class IndoorGML_To_DB {

	public static JFrame fQuery;
	public static JPanel pQuery;
	public static JLabel lDocument,lDatabase;
	public static JButton bDocument,bVisualize;
	public static JFileChooser jfDocument;
	public static FileNameExtensionFilter fnef = new FileNameExtensionFilter("IndoorGML files", "xml");
    public static File fileXML = null;
    public static Connection conn;
    public static String dbName = "";
    public static JComboBox cDatabase;
    public static Statement stmt;
    public static ResultSet rs;
    public static String pathValue;
    public static HashMap<String,String> relationMap=new HashMap<String,String>();
    private ImageIcon helpIcon;
    private JLabel lTitle;
    Color blue=new Color(147,221,236);
    /*
     * Method name              :   IndoorGML_To_DB
     * Method description       :   Constructor to define the variables used in the class
     * Method Arguments         :   null
     * Arguments description    :   null
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_To_DB() throws Exception {
    	
    	fQuery=new JFrame("Export IndoorGML to database");
		pQuery=new JPanel();
		pQuery.setBackground(Color.WHITE);
		pQuery.setLayout(null);
		lTitle=new JLabel("Export IndoorGML to database",SwingConstants.CENTER);
        lTitle.setBounds(0,0,650,50);
        lTitle.setFont(new Font("Serif",Font.BOLD,18));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);
       
        URL url=new URL("http://172.17.9.60/html/pics/help.png");
        ImageIcon icon = new ImageIcon(url);
		Image helpImg = icon.getImage();
        helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        helpIcon=new ImageIcon(helpImg);
        JLabel lhelp=new JLabel(helpIcon);
        lhelp.setBounds(600, 10, 30, 30);
        lhelp.setToolTipText("<html><center>This tool helps to generate the Graph Data from IndoorGML Document."
                + "<br/>1. Select the IndoorGML Document"
                + "<br/>2. Select the corresponding Database to populate graph data"
                + "<br/>3. Click the button 'Export' to populate  Graph data into database </center></html>");
		
		java.util.List<String> dbList = IIRMSApplicationInterface.getDBNames();
        String[] dbArray = dbList.toArray(new String[dbList.size()]);
		
		jfDocument=new JFileChooser("/home/Documents");
		
		lDocument=new JLabel("Select the IndoorGML document");
		lDocument.setBounds(40,100,250,25);
		bDocument = new JButton("Select document");
		bDocument.setBackground(blue);
        bDocument.setBounds(290, 100, 200, 25);
        //bDocument.setBackground(Color.PINK);
        bDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	try {
            		jfDocument.setFileFilter(fnef);
            		jfDocument.showOpenDialog(null);
            		File indoorFile=jfDocument.getSelectedFile();
            		pathValue=indoorFile.getPath();
            		//System.out.println("path:"+pathValue);
            		
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        lDatabase=new JLabel("Select the database");
        lDatabase.setBounds(40,170,180,25);
        cDatabase = new JComboBox(dbArray);
        cDatabase.setBounds(290, 170, 200, 25);
        
        bVisualize=new JButton("Export");
        bVisualize.setBackground(blue);
        bVisualize.setBounds(200, 260, 100, 25);
        bVisualize.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileXML = new File(pathValue);
					dbName = cDatabase.getSelectedItem().toString();
			        IndoorGML_Display();
			        JOptionPane.showMessageDialog(null, "IndoorGML file is exported to the database!", "IIRMS - Message", 1);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        });
        pQuery.add(lhelp);
        pQuery.add(lTitle);
        pQuery.add(bDocument);
        pQuery.add(cDatabase);
        pQuery.add(lDocument);
        pQuery.add(lDatabase);
        pQuery.add(bVisualize);
		fQuery.add(pQuery);
		fQuery.setVisible(true);
		fQuery.setSize(650, 400);
		
        
    }

    public static void LoadDBDriver() throws Exception {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + dbName, "researcher", "researcher");
    }

    public static void IndoorGML_Display() throws Exception {
        LoadDBDriver();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(fileXML);
        doc.getDocumentElement().normalize();

        stmt = conn.createStatement();
        //Creating room for indoorgml data.
        String queryTable = "CREATE TABLE state_indoorgml ("
                + "state_id CHARACTER VARYING(50) not null,"
                + "state_name CHARACTER VARYING(50) not null,"
                + "the_geom GEOMETRY not null,"
                + "floor_id INTEGER not null"
                + ")";
        stmt.execute(queryTable);
        		

        //Creating path for the graph display.
        queryTable = "CREATE TABLE transition_indoorgml("
                + "transition_id CHARACTER VARYING(50) not null,"
                + "startnode CHARACTER VARYING(50) not null,"
                + "endnode CHARACTER VARYING(50) not null,"
                + "relation CHARACTER VARYING(50) not null,"
                + "zonal_constraint CHARACTER VARYING(50),"
                + "the_geom GEOMETRY not null,"
                + "src_floor_id INTEGER not null,"
                + "dest_floor_id INTEGER not null"
                + ")";

        stmt.execute(queryTable);

        //Iterating the xml file for "STATE" tag.
        int roomcount = 0, doorcount = 0,staircount=0;
        String queryIn = "";
        NodeList nodeListSpaceLayer=doc.getElementsByTagName("indoorCore:SpaceLayer");
        for (int nodeCount1 = 0; nodeCount1 < nodeListSpaceLayer.getLength(); nodeCount1++) {
        	Node spaceNode=nodeListSpaceLayer.item(nodeCount1);
        	Node childNode=spaceNode.getFirstChild();
        	Element spaceNodeIn=(Element) childNode;
        	if(spaceNodeIn.getTextContent().toLowerCase().equals("topographic"))
        	{
        		NodeList childNodes=spaceNode.getChildNodes();
        		for(int i=0;i<childNodes.getLength();i++)
        		{
        			Node cNode=childNodes.item(i);
        			if(cNode.getNodeName().equals("indoorCore:nodes"))
        			{
        				NodeList stateNodes=cNode.getChildNodes();
        				for(int j=0;j<stateNodes.getLength();j++)
        				{
        					Node stateNodeNow=stateNodes.item(j);
        					if(stateNodeNow.getNodeName().equals("indoorCore:stateMember"))
        					{
        						Node sNode=stateNodeNow.getFirstChild();
        						if(sNode.getNodeName().equals("indoorCore:State"))
        						{
        							Element nodeIn = (Element) sNode;
        							String idValue = nodeIn.getAttribute("gml:id");
        	                        if (idValue.contains("S_R")) {
        	                            roomcount++;
        	                            String roomName = nodeIn.getElementsByTagName("gml:name").item(0).getTextContent();
        	                            String[] roomSplit = idValue.split("_");
        	                            int roomFloor = Integer.parseInt(roomSplit[roomSplit.length - 1]);
        	                            String pointValue = nodeIn.getElementsByTagName("gml:pos").item(0).getTextContent();
        	                            String roomGeom = "POINT(" + pointValue + ")";
        	                            queryIn = "INSERT INTO state_indoorgml (state_id, state_name, the_geom, floor_id) VALUES ('" + idValue.trim() + "',"
        	                                    + "'" + roomName.trim() + "', '" + roomGeom + "', " + roomFloor+ ")";
        	                            //System.out.println("Name: " + nodeIn.getElementsByTagName("gml:name").item(0).getTextContent());
        	                            int resValue = stmt.executeUpdate(queryIn);
        	                            //System.out.println("-------------------");
        	                            
        	                        } else if (idValue.contains("S_D")) {
        	                            doorcount++;
        	                            String doorName = nodeIn.getElementsByTagName("gml:name").item(0).getTextContent();
        	                            String[] doorSplit = idValue.split("_");
        	                            int doorFloor = Integer.parseInt(doorSplit[doorSplit.length - 1]);
        	                            String pointValue = nodeIn.getElementsByTagName("gml:pos").item(0).getTextContent();
        	                            String doorGeom = "POINT(" + pointValue + ")";
        	                            queryIn = "INSERT INTO state_indoorgml (state_id, state_name, the_geom, floor_id) VALUES ('" + idValue.trim() + "',"
        	                                    + "'" + doorName.trim() + "', '" + doorGeom + "', " + doorFloor + ")";
        	                            //System.out.println("Name: " + nodeIn.getElementsByTagName("gml:name").item(0).getTextContent());
        	                            int resValue = stmt.executeUpdate(queryIn);
        	                            //System.out.println("--------------------");
        	                        }
        	                        else if(idValue.contains("S_T")){
        	                        	staircount++;
        	                        	String stairName=nodeIn.getElementsByTagName("gml:name").item(0).getTextContent();
        	                        	String[] stairSplit=idValue.split("_");
        	                        	int stairFloor=Integer.parseInt(stairSplit[stairSplit.length-1]);
        	                        	String pointValue=nodeIn.getElementsByTagName("gml:pos").item(0).getTextContent();
        	                        	String stairGeom="POINT("+pointValue+")";
        	                        	queryIn="INSERT INTO state_indoorgml (state_id,state_name,the_geom,floor_id) VALUES ('" + idValue.trim() +"',"
        	                        			+"'" + stairName.trim() + "','" + stairGeom + "'," + stairFloor+")";
        	                        	int resValue=stmt.executeUpdate(queryIn);
        	                        	System.out.println("--------------------");
        	                        	//System.out.println(stairGeom);
        	                        }
        	                        
        	                        String adjacentValue = "", connectivityValue = "";
        	                        NodeList nodeListAdjacent = nodeIn.getElementsByTagName("adjacent");
        	                        for (int startCount = 0; startCount < nodeListAdjacent.getLength(); startCount++) {
        	                            Node nodeAdjacent = nodeListAdjacent.item(startCount);
        	                            if (nodeAdjacent.getNodeType() == Node.ELEMENT_NODE) {
        	                                Element elementAdj = (Element) nodeAdjacent;
        	                                adjacentValue = elementAdj.getAttribute("xlink:href").replace("#", "");
        	                                relationMap.put(adjacentValue.trim(), "adjacency");
        	                            }
        	                        }
        	                        NodeList nodeListConnects = nodeIn.getElementsByTagName("connects");
        	                        for (int startCount = 0; startCount < nodeListConnects.getLength(); startCount++) {
        	                            Node nodeConnects = nodeListConnects.item(startCount);
        	                            if (nodeConnects.getNodeType() == Node.ELEMENT_NODE) {
        	                                Element elementConn = (Element) nodeConnects;
        	                                connectivityValue = elementConn.getAttribute("xlink:href").replace("#", "");
        	                                relationMap.put(connectivityValue.trim(), "connectivity");
        	                            }
        	                        }
        						}
        					}
        				}
        			}
        			
        			else if(cNode.getNodeName().equals("indoorCore:edges"))
        			{
        				NodeList transNodes=cNode.getChildNodes();
        				for(int j=0;j<transNodes.getLength();j++)
        				{
        					Node transNodeNow=transNodes.item(j);
        					if(transNodeNow.getNodeName().equals("indoorCore:transitionMember"))
        					{
        						Node sNode=transNodeNow.getFirstChild();
        						int src_floor_id=0,dest_floor_id=0;
        						if(sNode.getNodeName().equals("indoorCore:Transition"))
        						{
        					                Element nodeOut = (Element) sNode;
        					                String trans_id = nodeOut.getAttribute("gml:id");
        					                String relation=relationMap.get(trans_id);
        					                //System.out.println(idValue2);
        					                //String[] splitValueArray = idValue2.split("_");
        					                //String floor_id = splitValueArray[splitValueArray.length - 1];
        					                //NodeList nodeListPos = nodeOut.getElementsByTagName("gml:pos");
        					                String finalPath = "MULTILINESTRING ((";
        					                for (int trans2 = 0; trans2 < 2; trans2++) {
        					                    String pathValue = nodeOut.getElementsByTagName("gml:pos").item(trans2).getTextContent();
        					                    if (trans2 != 1) {
        					                        finalPath += pathValue + ",";
        					                    } else {
        					                        finalPath += pathValue;
        					                    }
        					                }
        					                finalPath += "))";
        					                //System.out.println(finalPath);
        					                String nodeStartValue = "", nodeEndValue = "";
        					                NodeList nodeListStart = nodeOut.getElementsByTagName("start");
        					                for (int startCount = 0; startCount < nodeListStart.getLength(); startCount++) {
        					                    Node nodeStart = nodeListStart.item(startCount);
        					                    if (nodeStart.getNodeType() == Node.ELEMENT_NODE) {
        					                        Element elementStart = (Element) nodeStart;
        					                        nodeStartValue = elementStart.getAttribute("xlink:href").replace("#", "");
        					                        String[] splitValueArray=nodeStartValue.split("_");
        					                        src_floor_id=Integer.parseInt(splitValueArray[splitValueArray.length-1]);
        					                    }
        					                }
        					                NodeList nodeListEnd = nodeOut.getElementsByTagName("end");
        					                for (int startCount = 0; startCount < nodeListEnd.getLength(); startCount++) {
        					                    Node nodeStart = nodeListEnd.item(startCount);
        					                    if (nodeStart.getNodeType() == Node.ELEMENT_NODE) {
        					                        Element elementStart = (Element) nodeStart;
        					                        nodeEndValue = elementStart.getAttribute("xlink:href").replace("#", "");
        					                        String[] splitValueArray=nodeEndValue.split("_");
        					                        dest_floor_id=Integer.parseInt(splitValueArray[splitValueArray.length-1]);
        					                    }
        					                }
        					                //int count = transCount + 1;
        					                queryIn = "INSERT INTO transition_indoorgml (transition_id, startnode, endnode, relation,the_geom, src_floor_id,dest_floor_id)"
        					                        + "VALUES ('" + trans_id + "', '" + nodeStartValue + "', '" + nodeEndValue + "','"+relation+"',"
        					                        + "'" + finalPath + "', " + src_floor_id + ","+ dest_floor_id + ")";
        					                stmt = conn.createStatement();
        					                int resCount2 = stmt.executeUpdate(queryIn);
        					            }
        					        }
        						
        						}
        					}
        				}
        	}
        				
        	else if(spaceNodeIn.getTextContent().toLowerCase().equals("logical"))
        	{
        		NodeList childNodes=spaceNode.getChildNodes();
        		for(int i=0;i<childNodes.getLength();i++)
        		{
        			Node cNode=childNodes.item(i);
        			if(cNode.getNodeName().equals("indoorCore:edges"))
        			{
        				NodeList stateNodes=cNode.getChildNodes();
        				for(int j=0;j<stateNodes.getLength();j++)
        				{
        					Node stateNodeNow=stateNodes.item(j);
        					if(stateNodeNow.getNodeName().equals("indoorCore:transitionMember"))
        					{
        						Node sNode=stateNodeNow.getFirstChild();
        						if(sNode.getNodeName().equals("indoorCore:Transition"))
        						{
        							Element nodeOut = (Element) sNode;
        							String trans_id = nodeOut.getAttribute("gml:id");
        							String relation=relationMap.get(trans_id);
                        
        							String finalPath = "MULTILINESTRING ((";
        							for (int trans2 = 0; trans2 < 2; trans2++) {
        								String pathValue = nodeOut.getElementsByTagName("gml:pos").item(trans2).getTextContent();
        								if (trans2 != 1) {
        									finalPath += pathValue + ",";
        								} else {
        									finalPath += pathValue;
        								}
        							}
        							finalPath += "))";
        							String nodeStartValue = "", nodeEndValue = "";
        							NodeList nodeListStart = nodeOut.getElementsByTagName("start");
        							for (int startCount = 0; startCount < nodeListStart.getLength(); startCount++) {
        								Node nodeStart = nodeListStart.item(startCount);
        								if (nodeStart.getNodeType() == Node.ELEMENT_NODE) {
        									Element elementStart = (Element) nodeStart;
        									nodeStartValue = elementStart.getAttribute("xlink:href").replace("#", "");
        									
        								}
        							}
        							NodeList nodeListEnd = nodeOut.getElementsByTagName("end");
        							for (int startCount = 0; startCount < nodeListEnd.getLength(); startCount++) {
        								Node nodeStart = nodeListEnd.item(startCount);
        								if (nodeStart.getNodeType() == Node.ELEMENT_NODE) {
        									Element elementStart = (Element) nodeStart;
        									nodeEndValue = elementStart.getAttribute("xlink:href").replace("#", "");
        									
        								}
        			 				}
        							//int count = transCount + 1;
        							String query="UPDATE transition_indoorgml set zonal_constraint='accessible' where startnode='"+nodeStartValue+"' and endnode='"+nodeEndValue+"'";
        							stmt = conn.createStatement();
        							stmt.executeUpdate(query);
        						}
        						String query="UPDATE transition_indoorgml set zonal_constraint='non-accessible' where zonal_constraint Is NULL";
        						stmt=conn.createStatement();
        						stmt.executeUpdate(query);
        					}
        				}
        			}   
        		}
        	}
        }
    }
}