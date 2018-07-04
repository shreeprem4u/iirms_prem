package com.amrita.IIRMS.DB;

/*
 * File Name        : DB_Script_File.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow), Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 28th of September, 2015
 * Purpose          : Class to parse the information from shapefiles to database.
 */

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.sql.*;

import org.apache.ibatis.jdbc.*;

import java.util.*;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Polygon;

import nl.knaw.dans.common.dbflib.*;

import org.nocrala.tools.gis.data.esri.shapefile.*;
import org.nocrala.tools.gis.data.esri.shapefile.header.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.*;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.io.WKTReader;

import java.util.List.*;

import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;

public class DB_Script_File {

    //Frame to contain the panel and their components.
    public static JFrame dbFrame;
    public static JPanel dbPanel;
    public static JLabel lFile, lDBName;
    public static JTextField tFile, tDBName;
    public static JButton bCreate, bBrowse, bPopulate;
    public static Connection conn;
    public static JFileChooser jfc;
    
    //List to contain the walls
    public static java.util.List<PolylineShape> wall_polyLineList = new ArrayList<PolylineShape>();
    //List to contain the temp_walls
    public static java.util.List<PolylineShape> tempWall_polyLineList = new ArrayList<PolylineShape>();
    //List to contain the rooms
    public static java.util.List<PolygonShape> room_polygonList = new ArrayList<PolygonShape>();
    //List to contain the doors
    public static java.util.List<MultiPointPlainShape> door_pointList = new ArrayList<MultiPointPlainShape>();
    //List to contain the windows
    public static java.util.List<PolylineShape> win_polyLineList = new ArrayList<PolylineShape>();
    //List to contain the windows
    public static java.util.List<PolylineShape> stairs_polyLineList = new ArrayList<PolylineShape>();
    //List to contain the rooms with modified details.
    public static java.util.List<Rooms> finalRoomList = new ArrayList<Rooms>();
    //List to contain the walls with modified details.
    public static java.util.List<Walls> finalWallList = new ArrayList<Walls>();
    //List to contain the temporary walls with modified details.
    public static java.util.List<Temp_walls> finaltempWallList = new ArrayList<Temp_walls>();
    //List to contain the stairs with modified details.
    public static java.util.List<Stairs> finalStairsList = new ArrayList<Stairs>();
    //List to contain the walls with modified details.
    public static java.util.List<Windows> finalWindowList = new ArrayList<Windows>();
    //List to contain the door with modified details.
    public static java.util.List<Doors> finalDoorList = new ArrayList<Doors>();
    //List to contain the room with modified details (initial version)
    public static java.util.List<Rooms> roomList = new ArrayList<Rooms>();
    //List to contain the wall with modified details (initial version)
    public static java.util.List<Walls> wallsList = new ArrayList<Walls>();
    //List to contain the stairs with modified details (initial version)
    public static java.util.List<Stairs> stairsList = new ArrayList<Stairs>();
    //List to contain the temporary wall with modified details (initial version)
    public static java.util.List<Temp_walls> twallsList = new ArrayList<Temp_walls>();
    //List to contain the windows with modified details (initial version)
    public static java.util.List<Windows> windowList = new ArrayList<Windows>();
    //List to contain the Doors with modified details (initial version)
    public static java.util.List<Doors> doorList = new ArrayList<Doors>();
    //List to contain the floors with modified details (initial version)
    public static java.util.Set<Floors> floorsList = new HashSet<Floors>();
    //counts for various entities
    public static int roomcount = 1, wallcount = 1,twallcount=1, windowcount = 1, doorcount = 1, staircount=1,count = 1;
    //List to contain the links to walls and stairs
    public static java.util.List<String> wallLinkList = new ArrayList<String>();
    //List to contain the links to walls and stairs with floor values too. (final version)
    public static java.util.List<WallLinkClass> wallLinksList = new ArrayList<WallLinkClass>();
    
    public static ArrayList<Integer> fIdList=new ArrayList<Integer>();
    
    public static Floor_dist fdist = new Floor_dist();
    
    public static java.util.List<Floor_dist> fdistList = new ArrayList<>();
    public static double f1_coordX = 0,f1_coordY=0;
    //public static double floor1_coordX = 0;
    
    //HashMap to map the indoor entities
    public static HashMap<Integer,java.util.List<Integer>> doorId_roomIdMap=new HashMap<Integer,java.util.List<Integer>>();
    public static HashMap<Integer,java.util.List<Integer>> roomId_doorIdMap=new HashMap<Integer,java.util.List<Integer>>();
    public static HashMap<Integer,java.util.List<Integer>> wallId_roomIdMap=new HashMap<Integer,java.util.List<Integer>>();
    public static HashMap<Integer,java.util.List<Integer>> roomId_wallIdMap=new HashMap<Integer,java.util.List<Integer>>();
    public static HashMap<Integer,String> wallId_wallTypeMap=new HashMap<Integer,String>();
    public static HashMap<Integer,Integer> windowId_roomIdMap=new HashMap<Integer,Integer>();
    public static HashMap<Integer,Integer> windowId_wallIdMap=new HashMap<Integer,Integer>();
    
    //List that contain with modified details
    public static java.util.List<Sensors> sensorList=new ArrayList<Sensors>();
    public static java.util.List<PolygonShape> polygon_sensor_List = new ArrayList<PolygonShape>();
    public static java.util.List<Sensors> finalSensorList = new ArrayList<Sensors>();
    public static int sensorcount=1;
    private ImageIcon helpIcon;
    private JLabel lTitle;
    
    WKTReader reader=new WKTReader();

    /*
     * Method name              :   DB_Connection
     * Method description       :   Method to load the driver for PostgreSQL database and its connection establishment process.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void DB_Connection() throws Exception {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/", "researcher", "researcher");
    }

    /*
     * Method name              :   DB_Script_File
     * Method description       :   Constructor to define the form components and their behaviors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public DB_Script_File() throws Exception {
        //Frame to contain the panel and other components.
        dbFrame = new JFrame("DB Creation - IIRMS");

        //Setting layout for customization of form components.
        dbPanel = new JPanel();
        dbPanel.setLayout(null);
        
        Color blue=new Color(147,221,236);
        lTitle=new JLabel("DB Creation from Shapefiles",SwingConstants.CENTER);
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
        lhelp.setToolTipText("<html>This helps to create database from <br/>the building shapefiles<html> ");

        lFile = new JLabel("<html><center>Select the location of the shapefile</center></html>");
        lFile.setBounds(10, 80, 250, 40);

        lDBName = new JLabel("<html><center>Enter the name of the database<br>to be created</center></html>");
        lDBName.setBounds(10, 130, 250, 40);

        tFile = new JTextField(250);
        tFile.setBounds(300, 90, 200, 20);

        tDBName = new JTextField(100);
        tDBName.setBounds(300, 140, 200, 20);

        bBrowse = new JButton("Browse");
        bBrowse.setBounds(540, 90, 100, 25);
        //Color blue=new Color(165,208,236);
        bBrowse.setBackground(blue);
        jfc = new JFileChooser("/home/researcher");
        //Defining the action for "Browse" button.
        bBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showOpenDialog(null);
                System.out.println(jfc.getSelectedFile());
                tFile.setText(jfc.getSelectedFile().getAbsolutePath());
            }
        });

        bCreate = new JButton("Create the DB");
        bCreate.setBounds(250, 230, 180, 25);
        bCreate.setBackground(blue);
        //Defining the action for "Create the DB!" button.
        bCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    //Loading the driver.
                    DB_Connection();
                    System.out.println(tDBName.getText());
                    String query = "create database " + tDBName.getText() + "_Building";
                    Statement stmt2 = conn.createStatement();
                    System.out.println(query);
                    stmt2.execute(query);
                    conn.close();
                    conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + tDBName.getText().toLowerCase() + "_building", "researcher", "researcher");
                    boolean isScriptExecuted = false;
                    //Reading the postgis.sql file for constructing the database.
                    //Reading the postgis.sql file for constructing the database.
                    URL url= new URL("http://172.17.9.60/html/IIRMS_Web/postgis/postgis.sql");
                    URL url1= new URL("http://172.17.9.60/html/IIRMS_Web/postgis/spatial_ref_sys.sql");

                    BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));

                    String str = null;
                    StringBuilder sb = new StringBuilder();
                    while ((str = in.readLine()) != null) {
                        sb.append(str).append("\n ");
                    }
                    in.close();
                    Statement stmt3 = conn.createStatement();
                    System.out.println("------------------------");
                    System.out.println(sb.toString());
                    System.out.println("------------------------");
                    stmt3.executeUpdate(sb.toString());
                    //Reading the spatial_ref_sys.sql for constructing the database.
                    ScriptRunner srObj = new ScriptRunner(conn);
                    srObj.runScript(new BufferedReader(new InputStreamReader(url.openStream())));
                    srObj.runScript(new BufferedReader(new InputStreamReader(url1.openStream())));

                    isScriptExecuted = true;
                    //Reading the shapefile
                    DB_Creation();
                    //Populating the database.
                    DB_Population();
                    DB_Update();
                   
                    JOptionPane.showMessageDialog(null, "Database created successfully!", "IIRMS", 1);
                    dbFrame.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Adding the form components to the panel.
        dbPanel.add(lhelp);
        dbPanel.add(lTitle);
        dbPanel.add(lFile);
        dbPanel.add(lDBName);
        dbPanel.add(tFile);
        dbPanel.add(tDBName);
        dbPanel.add(bBrowse);
        dbPanel.add(bCreate);

        dbPanel.setBackground(Color.WHITE);

        //Adding the panel to the frame.
        dbFrame.add(dbPanel);
        dbFrame.setMinimumSize(new java.awt.Dimension(700, 300));
        dbFrame.setSize(700, 400);
        dbFrame.setVisible(true);
    }
    
    
    
    /*
     * Method name              :   DB_Population
     * Method description       :   Method to populate the database with the information from shapefile.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void DB_Population() throws Exception {
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + tDBName.getText().toLowerCase() + "_building", "researcher", "researcher");
        //Query for creating the "floor"
        String floorTable = "CREATE TABLE floor ("
                + "floor_id INTEGER NOT NULL,"
                + "the_geom geometry NOT NULL,"
                + "area double precision NOT NULL"
                + ")";
        Statement stmt = conn.createStatement();
        stmt.execute(floorTable);

        //Query for creating the "room"
        String roomTable = "CREATE TABLE "
                + "room ("
                + "room_id INTEGER NOT NULL,"
                + "room_name CHARACTER VARYING(50) NOT NULL,"
                + "room_type CHARACTER VARYING(50),"
                + "area double precision NOT NULL,"
                + "volume DOUBLE PRECISION NOT NULL,"
                + "the_geom geometry NOT NULL,"
                + "floor_id INTEGER NOT NULL"
                + ")";
        stmt = conn.createStatement();
        stmt.execute(roomTable);

        //Query for creating the "wall"
        String wallTable = "CREATE TABLE wall ("
                + "wall_id INTEGER NOT NULL,"
                + "wall_material CHARACTER VARYING(50),"
                + "wall_type CHARACTER VARYING(50),"
                + "the_geom geometry NOT NULL,"
                + "floor_id INTEGER NOT NULL"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(wallTable);
        
      //Query for creating the " temporary walls"
        /*String twallTable = "CREATE TABLE temp_wall ("
                + "twall_id INTEGER NOT NULL,"
                + "twall_material CHARACTER VARYING(50) NOT NULL,"
                + "twall_type CHARACTER VARYING(50) NOT NULL,"
                + "the_geom geometry NOT NULL,"
                + "floor_id INTEGER,"
                + "room_id INTEGER"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(twallTable);*/
        
        
        //Query for creating the "exit"
        String exitTable = "CREATE TABLE exit ("
                + "exit_id INTEGER NOT NULL,"
                + "exit_type CHARACTER VARYING(50),"
                + "exit_material CHARACTER VARYING(50),"
                + "close_type CHARACTER VARYING(50),"
                + "open_inward_dir BOOLEAN,"
                + "the_geom geometry NOT NULL,"
                + "floor_id INTEGER NOT NULL"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(exitTable);

        //Query for creating the "windows"
        String windowTable = "CREATE TABLE windows ("
                + "window_id INTEGER NOT NULL,"
                + "window_type CHARACTER VARYING(50),"
                + "window_material CHARACTER VARYING(50),"
                + "width double precision,"
                + "the_geom geometry NOT NULL,"
                + "room_id INTEGER,"
                + "floor_id INTEGER NOT NULL,"
                + "wall_id INTEGER"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(windowTable);
        
        //Query for creating the "stairs"
        String stairTable = "CREATE TABLE stairs ("
                + "stair_id INTEGER NOT NULL,"
                + "stair_type CHARACTER VARYING(50),"
                + "the_geom geometry NOT NULL,"
                + "floor_id INTEGER NOT NULL"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(stairTable);
        
        //Query for creating the "room_exit"
        String room_exitTable="CREATE TABLE room_exit("
        		+"room_id INTEGER,"
        		+"exit_id INTEGER"
        		+")";
        stmt=conn.createStatement();
        stmt.execute(room_exitTable);
        
        //Query for creating the "room_wall"
        String room_wallTable="CREATE TABLE room_wall("
        		+"room_id INTEGER,"
        		+"wall_id INTEGER"
        		+")";
        stmt=conn.createStatement();
        stmt.execute(room_wallTable);


        //Query for creating the "path"
        String pathTable = "CREATE TABLE path ("
                + "the_geom geometry NOT NULL,"
        		+ "floor_id INTEGER NOT NULL,"
                + "ipaddress CHARACTER VARYING(50) NOT NULL)";

        stmt = conn.createStatement();
        stmt.execute(pathTable);

        //Query for creting the "links to file"
        String linkTable = "CREATE TABLE filetable ("
                + "file_id INTEGER NOT NULL,"
                + "file_loc CHARACTER VARYING(300) NOT NULL,"
                + "floor_id INTEGER NOT NULL"
                + ")";

        stmt = conn.createStatement();
        stmt.execute(linkTable);
        ///////////  after changes
        String sensorCatalogueGeneric="CREATE TABLE "
                + "sensor_master("
        		+ "sensor_generic_code CHARACTER VARYING(10) NOT NULL,"
                + "sensor_generic_type CHARACTER VARYING(25) NOT NULL,"
                + "sensor_generic_name CHARACTER VARYING(25) NOT NULL"
                + ")";
        stmt = conn.createStatement();
        stmt.execute(sensorCatalogueGeneric);
        String sensorCatalogue="CREATE TABLE "
                + "sensor_details("
        		+ "sensor_specific_code CHARACTER VARYING(10) NOT NULL,"
                + "sensor_generic_code CHARACTER VARYING(10) NOT NULL,"
                + "sensor_name CHARACTER VARYING(100) NOT NULL,"
                + "unique_identifier CHARACTER VARYING(100) NOT NULL,"
                + "intended_application CHARACTER VARYING(25) NOT NULL,"
                + "weight DOUBLE PRECISION NOT NULL,"
                + "length DOUBLE PRECISION NOT NULL,"
                + "height DOUBLE PRECISION NOT NULL,"
                + "casting_material CHARACTER VARYING(25) NOT NULL,"
                + "voltage DOUBLE PRECISION NOT NULL,"
                + "current_type CHARACTER VARYING(25) NOT NULL,"
                + "amp_range DOUBLE PRECISION NOT NULL,"
                + "range_influence DOUBLE PRECISION NOT NULL,"
                + "output CHARACTER VARYING(25) NOT NULL"
                +")";
        stmt = conn.createStatement();
        stmt.execute(sensorCatalogue);
        String sensorTable="CREATE TABLE "
                + "sensors("
        		+ "sensor_id CHARACTER VARYING(10) NOT NULL,"
                + "sensor_specific_code CHARACTER VARYING(10) NOT NULL,"
        		+ "sensor_generic_code CHARACTER VARYING(10) NOT NULL,"
                + "the_geom geometry NOT NULL,"
                + "position CHARACTER VARYING(500),"//edited last @17-dec-15
                + "room_id INTEGER,"
                + "floor_id INTEGER"      
                + ")";
        stmt = conn.createStatement();
        stmt.execute(sensorTable);
        ///////////

      //Query for creating the primary key constraint.
        String primaryKeyLink = "ALTER TABLE ONLY filetable ADD CONSTRAINT link_pkey PRIMARY KEY (file_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyLink);

        //Query for creating the primary key constraint.
        String primaryKeyFloor = "ALTER TABLE ONLY floor ADD CONSTRAINT floor_pkey PRIMARY KEY (floor_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyFloor);

        //Query for creating the primary key constraint.
        String primaryKeyRoom = "ALTER TABLE ONLY room ADD CONSTRAINT room_pkey PRIMARY KEY (room_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyRoom);

        //Query for creating the primary key constraint.
        String primaryKeyWall = "ALTER TABLE ONLY wall ADD CONSTRAINT wall_pkey PRIMARY KEY (wall_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyWall);

        //Query for creating the primary key constraint.
        String primaryKeyExit = "ALTER TABLE ONLY exit ADD CONSTRAINT exit_pkey PRIMARY KEY(exit_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyExit);
        
        //Query for creating the primary key constraint.
        String primaryKeyWindows = "ALTER TABLE ONLY windows ADD CONSTRAINT window_pkey PRIMARY KEY(window_id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeyWindows);
        
        //Query for creating the primary key constraint
        String primaryKeyRoomExit="ALTER TABLE ONLY room_exit ADD CONSTRAINT room_exit_pkey PRIMARY KEY(room_id,exit_id)";
        stmt=conn.createStatement();
        stmt.execute(primaryKeyRoomExit);
        
        //Qery for creating the primary key constraint
        String primaryKeyRoomWall="ALTER TABLE ONLY room_wall ADD CONSTRAINT room_wall_pkey PRIMARY KEY(room_id,wall_id)";
        stmt=conn.createStatement();
        stmt.execute(primaryKeyRoomWall);
        
        String primaryKeySensorMaster = "ALTER TABLE ONLY sensor_master ADD CONSTRAINT sensor_master_pkey PRIMARY KEY (sensor_generic_code)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeySensorMaster);
        
        String primaryKeySensorDetail = "ALTER TABLE ONLY sensor_details ADD CONSTRAINT sensor_details_pkey PRIMARY KEY (sensor_specific_code)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeySensorDetail);
        
        String primaryKeySensor = "ALTER TABLE ONLY sensors ADD CONSTRAINT sensor_pkey PRIMARY KEY (sensor_Id)";
        stmt = conn.createStatement();
        stmt.execute(primaryKeySensor);

        //Query for creating the foreign Key constraint.
        String foreignKeyRoom = "ALTER TABLE ONLY room ADD CONSTRAINT room_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyRoom);

        //Query for creating the foreign Key constraint.
        String foreignKeyWall = "ALTER TABLE ONLY wall ADD CONSTRAINT wall_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyWall);
        
        //Query for creating the foreign Key constraint.
        String foreignKeyRoomWall1="ALTER TABLE ONLY room_wall ADD CONSTRAINT roomWall_room_fkey FOREIGN KEY (room_id) REFERENCES room(room_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeyRoomWall1);
        
        //Query for creating the foreign key constraint
        String foreignKeyRoomWall2="ALTER TABLE ONLY room_wall ADD CONSTRAINT roomWall_wall_fkey FOREIGN KEY (wall_id) REFERENCES wall(wall_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeyRoomWall2);
        
        //Query for creating the foreign key constraint
        String foreignKeyRoomExit1="ALTER TABLE ONLY room_exit ADD CONSTRAINT roomExit_room_fkey FOREIGN KEY (room_id) REFERENCES room(room_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeyRoomExit1);
        
        //Query for creating the foreign key constraint
        String foreignKeyRoomExit2="ALTER TABLE ONLY room_exit ADD CONSTRAINT roomExit_exit_fkey FOREIGN KEY (exit_id) REFERENCES exit(exit_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeyRoomExit2);

        //Query for creating the foreign key constraint.
        String foreignKeyExit = "ALTER TABLE ONLY exit ADD CONSTRAINT exit_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyExit);

        //Query for creating the foreign key constraint.
        String foreignKeyWindow = "ALTER TABLE ONLY windows ADD CONSTRAINT window_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyWindow);
        
        //Query for creating the foreign key constraint.
        String foreignKeyWindow1 = "ALTER TABLE ONLY windows ADD CONSTRAINT window_room_fkey FOREIGN KEY (room_id) REFERENCES room(room_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyWindow1);
        
        String foreignKeyWindow2="ALTER TABLE ONLY windows ADD CONSTRAINT window_wall_fkey FOREIGN KEY (wall_id) REFERENCES wall(wall_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeyWindow2);
        
        String foreignKeySensor1="ALTER TABLE ONLY sensor_details ADD CONSTRAINT sensor_generic_fkey FOREIGN KEY (sensor_generic_code) REFERENCES sensor_master(sensor_generic_code)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeySensor1);
        
        String foreignKeySensor2="ALTER TABLE ONLY sensors ADD CONSTRAINT sensor_specific_fkey FOREIGN KEY (sensor_specific_code) REFERENCES sensor_details(sensor_specific_code)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeySensor2);
        
        String foreignKeySensor3="ALTER TABLE ONLY sensors ADD CONSTRAINT sensor_generic1_fkey FOREIGN KEY(sensor_generic_code) REFERENCES sensor_master(sensor_generic_code)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeySensor3);
        
        String foreignKeySensorFloor="ALTER TABLE ONLY sensors ADD CONSTRAINT sensor_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeySensorFloor);
        
        String foreignKeySensorRoom="ALTER TABLE ONLY sensors ADD CONSTRAINT sensor_room_fkey FOREIGN KEY (room_id) REFERENCES room(room_id)";
        stmt=conn.createStatement();
        stmt.execute(foreignKeySensorRoom);

        //Query for creating another constraint.
        String foreignKeyLink = "ALTER TABLE ONLY filetable ADD CONSTRAINT file_floor_fkey FOREIGN KEY (floor_id) REFERENCES floor(floor_id)";
        stmt = conn.createStatement();
        stmt.execute(foreignKeyLink);
        
        
        

      //Inserting the floor values into the table.
        for (Floors floorObj : floorsList) {
            String floorValue = "ST_GeomFromText('" + floorObj.the_geom.toString() + "')";
            String floorIns = "INSERT INTO floor (floor_id, the_geom, area) VALUES ('" + floorObj.floor_id + "'," + floorValue + ", '" + floorObj.area + "')";
            Statement stmtIn = conn.createStatement();
            stmtIn.executeUpdate(floorIns);
            stmtIn.close();
        }
        System.out.println("Floor table created!");

        //Inserting the room values into the table.
        for (Rooms roomObj : finalRoomList) {
            String roomValue = "ST_GeomFromText('" + roomObj.room_geom.toString() + "')";
            String roomIns = "INSERT INTO room (room_id, room_name, the_geom, room_type, floor_id, area, volume) VALUES ('" + roomObj.room_count + "','" + roomObj.room_name.trim() + "',"
                    + roomValue + ", '" + roomObj.room_type.trim() + "', '" + roomObj.room_floor + "', '" + roomObj.area + "', '" + roomObj.volume + "')";
            Statement stmtIn2 = conn.createStatement();
            stmtIn2.executeUpdate(roomIns);
            stmtIn2.close();
        }
        System.out.println("Room Table created!");

        //Inserting the wall values into the table.
        for (Walls wallObj : finalWallList) {
            String wallValue = "ST_GeomFromText('" + wallObj.wall_geom.toString() + "')";
            //String roomIdList=insertArray(wallObj.room_id);
            String wall_type=wallId_wallTypeMap.get(wallObj.wall_count);
            String wallIns = "INSERT INTO wall (wall_id, the_geom, wall_type, floor_id,wall_material) VALUES ('" + wallObj.wall_count + "', " + wallValue + ","
                    + "'" + wall_type+ "'," + wallObj.wall_floor +",'"+ wallObj.wall_material.trim() + "')";
            Statement stmtIn3 = conn.createStatement();
            stmtIn3.executeUpdate(wallIns);
            stmtIn3.close();
        }
        System.out.println("Wall table created!");
        
        

      //Inserting the wall values into the table.
        /*for (Temp_walls twallObj : finaltempWallList) {
            String twallValue = "ST_GeomFromText('" + twallObj.twall_geom.toString() + "')";
            String twallIns = "INSERT INTO temp_wall (twall_id, the_geom, twall_type, floor_id, twall_material) VALUES ('" + twallObj.twall_count + "', " + twallValue + ","
                    + "'" + twallObj.twall_type.trim() + "','" + twallObj.twall_floor + "', '" + twallObj.twall_material.trim() + "')";
            Statement stmtIn31 = conn.createStatement();
            stmtIn31.executeUpdate(twallIns);
            stmtIn31.close();
        }
        System.out.println("Temporary Walls table created!");*/
        
        
        for (Windows winObj : finalWindowList) {
            String winValue = "ST_GeomFromText('" + winObj.win_geom.toString() + "')";
            int room_id=windowId_roomIdMap.get(winObj.win_count);
            int wall_id=windowId_wallIdMap.get(winObj.win_count);
            String winIns = "INSERT INTO windows (window_id, the_geom, window_type,width, floor_id,room_id,wall_id,window_material) VALUES ('" + winObj.win_count + "', " + winValue + ","
                    + "'" + winObj.win_type.trim() + "','"+winObj.win_width+"','"+ winObj.win_floor + "',"+room_id+","+wall_id+",'"+winObj.win_material.trim() + "')";
            Statement stmtIn4 = conn.createStatement();
            stmtIn4.executeUpdate(winIns);
            stmtIn4.close();
        }
        System.out.println("Window table created!");
        
        
        for (Doors doorObj : finalDoorList) {
            String doorValue = "ST_GeomFromText('" + doorObj.door_geom.toString() + "')";
            String doorIns = "INSERT INTO exit (exit_id, the_geom, exit_type,exit_material,close_type,open_inward_dir,floor_id) VALUES ('" + doorObj.door_count + "', " + doorValue + ","
                    + "'" + doorObj.door_type.trim() + "','" +doorObj.door_material.trim()+"','"+doorObj.close_type+"','"+doorObj.open_inwards+"',"+ doorObj.door_floor + ")";
            Statement stmtIn5 = conn.createStatement();
            stmtIn5.executeUpdate(doorIns);
            stmtIn5.close();
        }
        System.out.println("Doors table created!");
        
        for (Stairs stairObj : finalStairsList) {
            String stairValue = "ST_GeomFromText('" + stairObj.stair_geom.toString() + "')";
            String stairIns = "INSERT INTO stairs (stair_id, the_geom, stair_type, floor_id) VALUES ('" + stairObj.stair_count + "', " + stairValue + ","
                    + "'" + stairObj.stair_type.trim() + "','" + stairObj.stair_floor + "')";
            Statement stmtIn6 = conn.createStatement();
            stmtIn6.executeUpdate(stairIns);
            stmtIn6.close();
        }
        System.out.println("Stairs table created!");
        
        //Inserting values to the room_exit table
        for(Doors doorObj:finalDoorList){
        	java.util.List<Integer> r_roomIdList=doorId_roomIdMap.get(doorObj.door_count);
        	for(int i=0;i<r_roomIdList.size();i++)
        	{
        		//System.out.println("RoomId:"+doorObj.door_count+" Exit_id:"+r_doorIdList.get(i));
        		String exit_roomIns="INSERT INTO room_exit(exit_id,room_id) VALUES ("+doorObj.door_count+","+r_roomIdList.get(i)+")";
        		Statement stmtIn7=conn.createStatement();
        		stmtIn7.executeUpdate(exit_roomIns);
        		stmtIn7.close();
        	}	
        }
        for(Rooms roomObj:finalRoomList){
        	java.util.List<Integer> d_doorIdList=roomId_doorIdMap.get(roomObj.room_count);
        	for(int i=0;i<d_doorIdList.size();i++)
        	{
        		//String room_exitIns="INSERT INTO room_exit(room_id,exit_id) VALUES ("+roomObj.room_count+","+d_doorIdList.get(i)+")";
        		String room_exitIns="INSERT INTO room_exit(room_id,exit_id) SELECT "+roomObj.room_count+","+d_doorIdList.get(i)+" WHERE NOT EXISTS( SELECT * FROM room_exit WHERE room_id="+roomObj.room_count+" AND exit_id="+d_doorIdList.get(i)+")";
        		Statement stmtIn8=conn.createStatement();
        		stmtIn8.executeUpdate(room_exitIns);
        		stmtIn8.close();
        	}
        }
        System.out.println("Room_Exit table created");
        
        //Inserting values to the room_wall table
        for(Walls wallObj:finalWallList){
        	java.util.List<Integer> w_roomIdList=wallId_roomIdMap.get(wallObj.wall_count);
        	for(int i=0;i<w_roomIdList.size();i++)
        	{
        		String wall_roomIns="INSERT INTO room_wall(wall_id,room_id) VALUES ("+wallObj.wall_count+","+w_roomIdList.get(i)+")";
        		Statement stmtIn9=conn.createStatement();
        		stmtIn9.executeUpdate(wall_roomIns);
        		stmtIn9.close();
        	}
        }
        for(Rooms roomObj:finalRoomList){
        	java.util.List<Integer> r_wallIdList=roomId_wallIdMap.get(roomObj.room_count);
        	for(int i=0;i<r_wallIdList.size();i++)
        	{
        		//String room_wallIns="INSERT INTO room_wall(room_id,wall_id) VALUES ("+roomObj.room_count+","+r_wallIdList.get(i)+")";
        		String room_wallIns="INSERT INTO room_wall(room_id,wall_id) SELECT "+roomObj.room_count+","+r_wallIdList.get(i)+" WHERE NOT EXISTS( SELECT * FROM room_wall WHERE room_id="+roomObj.room_count+" AND wall_id="+r_wallIdList.get(i)+")";
        		Statement stmtIn10=conn.createStatement();
        		stmtIn10.executeUpdate(room_wallIns);
        		stmtIn10.close();
        	}
        }
        System.out.println("Room_Wall table created");
        
        //update tables
        //Inserting the file link values into the table.
        int i = 1;
        for (WallLinkClass wlcObj : wallLinksList) {
            try {
                String linkInsMain = "INSERT INTO filetable (file_id, file_loc, floor_id) VALUES (" + i + ","
                        + "'" + wlcObj.file_loc + "', " + wlcObj.floor_id + ")";
                Statement linkStmtMain = conn.createStatement();
                linkStmtMain.executeUpdate(linkInsMain);
                linkStmtMain.close();
                i++;
            } catch (Exception exx) {
                exx.printStackTrace();
            }
        }
    }
    
    /*
     * Method name              :   insertArray
     * Method description       :   Method to convert the array list to array format to insert in the database
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static String insertArray(ArrayList<String> arrList)
    {
    	String roomNameList = null ;
    	String emptyArray="ARRAY[null]";
    	if(!(roomNameList==null))
    	{
    		roomNameList="ARRAY[";
    		for(int i=0;i<arrList.size();i++)
    		{
    			if(i>0 )
    			{
    				roomNameList+=",";
    			}
    			roomNameList+="'"+arrList.get(i)+"'";
    		}
    		roomNameList+="]";
    		return roomNameList;
    	}
    	else
    		return emptyArray;
		
    }
    
    /*
     * Method name              :   DB_Update
     * Method description       :   Method to update the database
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void DB_Update()throws Exception
    {   
        /*String q_exitUpdate="select open_inward_dir,floor_id,exit_id from exit order by exit_id";
        Statement st_exitUpdate =conn.createStatement();
        ResultSet rs_exitUpdate=st_exitUpdate.executeQuery(q_exitUpdate);
        while(rs_exitUpdate.next())
        {
            String roomIn=rs_exitUpdate.getString(1);
            int floorId=rs_exitUpdate.getInt(2);
            int exitId=rs_exitUpdate.getInt(3);           
            String query2="update room set exit_id="+exitId+" where room_name LIKE '"+roomIn+"%' and floor_id="+floorId;
                Statement stmt2=conn.createStatement();
                stmt2.executeUpdate(query2);
                stmt2.close();
        }
        System.out.println("Rooms table updated");*/        
    }  
       
     /*
     * Method name              :   DB_Creation
     * Method description       :   Method to sort the floors IDs and insert the floor details in order
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static void DB_Creation() throws Exception
    {
    	File file = new File(tFile.getText());
        File[] fileList = file.listFiles();
        for (File fileNow : fileList)
        {
        	String fileId=fileNow.getName().replaceAll("\\D+", "");
        	if((fileId!=null) && !(fileId.isEmpty()))
        	{
        		int f_id=Integer.parseInt(fileId);
        		if(!fIdList.contains(f_id))
        		{
        			fIdList.add(f_id);
        		}
        	}
        }
        Collections.sort(fIdList);
        for(int i=0;i<fIdList.size();i++)
        {
            find_Floors_Distance(fIdList.get(i));
        }
        for(int i=0;i<fIdList.size();i++)
        {
            parseFile(fIdList.get(i));    	
        }
        
        //To construct a final list with the data from shp and dbf files.
        finalRoomList=Rooms.Room_Construction(roomList,room_polygonList);
        finalWallList=Walls.Wall_Construction(wallsList,wall_polyLineList);
        //Temp_walls.tempWall_Construction();
        finalWindowList=Windows.Window_Construction(windowList,win_polyLineList);
        finalDoorList=Doors.Doors_Construction(doorList,door_pointList);
        finalStairsList=Stairs.Stairs_Construction(stairsList,stairs_polyLineList);
        
        //To construct hash map to map the indoor entities
        doorId_roomIdMap=Doors.MapDoorId_RoomId(finalDoorList, finalRoomList);
        roomId_doorIdMap=Rooms.MapRoomId_DoorId(finalRoomList, finalDoorList);
        wallId_roomIdMap=Walls.MapWallId_RoomId(finalWallList, finalRoomList);
        roomId_wallIdMap=Rooms.MapRoomId_WallId(finalRoomList, finalWallList);
        wallId_wallTypeMap=Walls.MapWallId_WallType(finalWallList, finalRoomList);
        windowId_roomIdMap=Windows.MapWindowId_RoomId(finalWindowList, finalRoomList);
        windowId_wallIdMap=Windows.MapWindowId_WallId(finalWindowList, finalWallList);
        
    }
    
    /*
     * Method name              :   find_Floors_Distance
     * Method description       :   Method to find the distance between the first floor and the other floors.This is used 
     * 								for visualization to place one floor above the other.
     * Method Arguments         :   (int) floor_id
     * Arguments description    :   floor_id       ---> floor_id of the floor for which the distance is to be calculated
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
       
    public static void find_Floors_Distance(int floor_id) throws Exception {
    	
        File fileIn = new File(tFile.getText());
        File[] folderFiles = fileIn.listFiles();
        for (File fileNow : folderFiles) {
            String fid = fileNow.getName().replaceAll("\\D+", "");
            if ((fid != null) && !(fid.isEmpty())) {
                int fileId = Integer.parseInt(fid);
                if (fileId == floor_id && fileNow.getName().contains(".shp")) {
                    if ((fileNow.getName().contains("base"))) {
                        FileInputStream fileStream = new FileInputStream(fileNow);
                        ShapeFileReader sfReader = new ShapeFileReader(fileStream);
                        ShapeFileHeader sfHeader = sfReader.getHeader();
                        double xmin,ymin;
                        xmin = sfHeader.getBoxMinX();
                        ymin=sfHeader.getBoxMinY();
                        
                        if (floor_id == 1) {
                            Floor_dist fdObj = new Floor_dist();
                            f1_coordX=xmin;
                            f1_coordY=ymin;
                            fdObj.f_xdist = 0;//xmin;
                            fdObj.f_ydist=0;//ymin;
                            fdObj.f_id = floor_id;
                            //System.out.println("floor id:"+floor_id);
                            //System.out.println("xdist:"+fdObj.f_xdist);
                            //System.out.println("ydist:"+fdObj.f_ydist);
                            //System.out.println("________________");
                            fdistList.add(fdObj);
                        } else {
                            Floor_dist fdObj = new Floor_dist();
                            double f_coordX = xmin;
                            double f_coordY=ymin;
                            fdObj.f_xdist = f_coordX- f1_coordX;
                            fdObj.f_ydist=f_coordY-f1_coordY;
                            fdObj.f_id = floor_id;
                            //System.out.println("floor id:"+floor_id);
                            //System.out.println("xdist:"+fdObj.f_xdist);
                            //System.out.println("ydist:"+fdObj.f_ydist);
                            fdistList.add(fdObj);
                        }
                    }
                }
            }
        }
    }

    /*
     * Method name              :   parseFile
     * Method description       :   Method to parse the data from shapefiles and dbf file.
     * Method Arguments         :   (int) floor_id
     * Arguments description    :   floor_id        --->floor_id of the floor for which the floor_details is to be inserted
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void parseFile(int floor_id) throws Exception 
    {
    	
        File fileIn = new File(tFile.getText());
        File[] folderFiles = fileIn.listFiles();
        
        //System.out.println("Files length:"+folderFiles.length);
        //Iterating the file for data retrieval
        System.out.println("**********************************************");
        for (File fileNow : folderFiles) 
        {
        	String fid=fileNow.getName().replaceAll("\\D+", "");
        	if((fid!=null) && !(fid.isEmpty()))
        	{
        		int fileId=Integer.parseInt(fid);
                       
        		if (fileId==floor_id && fileNow.getName().contains(".shp")) 
        		{
        			shapeAddingProcess(fileNow,floor_id);	
        		} 
                else if (fileId==floor_id && fileNow.getName().contains(".dbf")) 
        		{
        			dataAddingProcess(fileNow);
        		}
        	}
        }
        System.out.println("**********************************************");
        System.out.println("Read all the shapefiles!");
        
    }
   
    /*
     * Method name              :   shiftDistX
     * Method description       :   Method to iterate the list which contains the distance of each floor from the fist floor(x distance)
     * Method Arguments         :   (int) floor_id
     * Arguments description    :   floor_id        --->floor_id of the floor for which the distance is to be returned
     * Return type              :   double
     * Return type description  :   xdist           --->Returns floor distance
     */
    public static double shiftDistX(int floorId) throws Exception 
    {
        Iterator<Floor_dist> FLIterator = fdistList.iterator();
        double xdist = 0f;
        int id = 0;
        while (FLIterator.hasNext()) 
        {
            Floor_dist fObj = FLIterator.next();
            if (floorId == fObj.f_id) 
            {
                xdist = fObj.f_xdist;
                id = fObj.f_id;
            }
        }
        return xdist;
    }
    
    
    /*
     * Method name              :   shiftDistY
     * Method description       :   Method to iterate the list which contains the distance of each floor from the fist floor(y distance)
     * Method Arguments         :   (int) floor_id
     * Arguments description    :   floor_id        --->floor_id of the floor for which the distance is to be returned
     * Return type              :   double
     * Return type description  :   ydist           --->Returns floor distance
     */
    public static double shiftDistY(int floorId) throws Exception 
    {
        Iterator<Floor_dist> FLIterator = fdistList.iterator();
        double ydist = 0f;
        int id = 0;
        while (FLIterator.hasNext()) 
        {
            Floor_dist fObj = FLIterator.next();
            if (floorId == fObj.f_id) 
            {
                ydist = fObj.f_ydist;
                id = fObj.f_id;
            }
        }
        return ydist;
    }
    
    
    

    /*
     * Method name              :   shapeAddingProcess
     * Method description       :   Method for parsing the data from shapefiles.
     * Method Arguments         :   File (fileForProcess)
     * Arguments description    :   "fileForProcess"    --> file to be parsed for data,"floor_id"   -->floor_id of the floor
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void shapeAddingProcess(File fileForProcess,int floor_id) throws Exception {
    	System.out.println(fileForProcess.getName());
        FileInputStream fileStream = new FileInputStream(fileForProcess);
        ShapeFileReader sfReader = new ShapeFileReader(fileStream);
        ShapeFileHeader sfHeader = sfReader.getHeader();
        //AbstractShape absShape;
        
        if(fileForProcess.getName().toLowerCase().contains("base"))
        {
        	AbstractShape absShape;
        	int floorId= Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
        	Polygon floor_polygon = null;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolygonShape floor_polygonshape = (PolygonShape) absShape;
            	
            	GeometryFactory gf = new GeometryFactory();
            	ArrayList<Coordinate> pointList= new ArrayList<Coordinate>();;
                PointData[] points=floor_polygonshape.getPoints();
                for(int k=0;k<floor_polygonshape.getNumberOfPoints();k++)
                {
                	double room_p1X = points[k].getX()-DB_Script_File.shiftDistX(floor_id);
                	double room_p1Y = points[k].getY()-DB_Script_File.shiftDistY(floor_id);
                    pointList.add(new Coordinate(room_p1X, room_p1Y)); 
                }
                Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
                floor_polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(coordArray), gf), null);
            }
            Floors iFloor=new Floors();
            iFloor.floor_id=floorId;
            iFloor.the_geom=floor_polygon;
            iFloor.area=floor_polygon.getArea();
            floorsList.add(iFloor);
        }
        
        else if ((fileForProcess.getName().toLowerCase().contains("wall")) && (!fileForProcess.getName().toLowerCase().contains("temp_wall"))) 
        {
            /*GeometryFactory gf = new GeometryFactory();
            double xmin, xmax, ymin, ymax;
            xmin = sfHeader.getBoxMinX()-shiftDistX(floor_id);
            xmax = sfHeader.getBoxMaxX()-shiftDistX(floor_id);
            ymin = sfHeader.getBoxMinY()-shiftDistY(floor_id);
            ymax = sfHeader.getBoxMaxY()-shiftDistY(floor_id);
            ArrayList<Coordinate> pointsList = new ArrayList<>();
            pointsList.add(new Coordinate(xmin, ymin));
            pointsList.add(new Coordinate(xmax, ymin));
            pointsList.add(new Coordinate(xmax, ymax));
            pointsList.add(new Coordinate(xmin, ymax));
            Coordinate[] coordArray = pointsList.toArray(new Coordinate[pointsList.size()]);
            LineString ls = gf.createLineString(coordArray);
            Floors ff = new Floors();
            ff.the_geom = ls;
            ff.area = ls.getArea();
            ff.floor_id = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
            
            floorsList.add(ff);*/
            
            AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolylineShape pls = (PolylineShape) absShape;
            	wall_polyLineList.add(pls);
            }

            WallLinkClass wlc = new WallLinkClass();
            int floorId=Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));

            wlc.file_loc = fileForProcess.getAbsolutePath().toString();
            wlc.floor_id = floorId;
            wallLinksList.add(wlc);
        }
        else if(fileForProcess.getName().toLowerCase().contains("temp_wall"))
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolylineShape pls = (PolylineShape) absShape;
            	tempWall_polyLineList.add(pls);
            }
        }
        else if (fileForProcess.getName().toLowerCase().contains("stair")) 
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
               PolylineShape pls = (PolylineShape) absShape;
               stairs_polyLineList.add(pls);
            }
            int floorNow = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
            WallLinkClass wlc2 = new WallLinkClass();
            wlc2.file_loc = fileForProcess.getAbsolutePath().toString();
            wlc2.floor_id = floorNow;
            wallLinksList.add(wlc2);
        }
        else if (fileForProcess.getName().toLowerCase().contains("room"))
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolygonShape pgs = (PolygonShape) absShape;
            	room_polygonList.add(pgs);
               	
            }
        }
        else if(fileForProcess.getName().toLowerCase().contains("win"))
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolylineShape pls = (PolylineShape) absShape;
            	win_polyLineList.add(pls);
            }
        }
        
        else if((fileForProcess.getName().toLowerCase().contains("ex"))||(fileForProcess.getName().contains("exit")))
        {
        	AbstractShape absShape;
        	while((absShape=sfReader.next())!=null)
        	{
        		MultiPointPlainShape mpps=(MultiPointPlainShape) absShape;
        		door_pointList.add(mpps);
        	}
        }
        
    }

    /*
     * Method name              :   dataAddingProcess
     * Method description       :   Method for parsing the data from DBF files.
     * Method Arguments         :   File (fileForProcess)
     * Arguments description    :   "fileForProcess"    --> file to be parsed for data.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void dataAddingProcess(File fileForProcess) throws Exception {
    	System.out.println(fileForProcess.getName());
        Table tableIn = new Table(fileForProcess);
        tableIn.open(IfNonExistent.ERROR);
        java.util.List<Field> fieldList = tableIn.getFields();

        //Write the code to get the records individually
        Iterator<Record> recordIterator = tableIn.recordIterator();

        if (fileForProcess.getName().toLowerCase().contains("room")) {
            while (recordIterator.hasNext()) {
                Rooms iRoom = new Rooms();
                Record record = recordIterator.next();
                iRoom.room_count = roomcount;
                iRoom.count = count;
                for (Field field : fieldList) {
                    try {
                        if ((field.getName().toLowerCase().contains("roomname")) || (field.getName().toLowerCase().contains("room_name"))) {
                        	String roomname=new String(record.getRawValue(field));
                            iRoom.room_name =roomname.trim(); 
                        } else if ((field.getName().toLowerCase().equals("type")) || (field.getName().toLowerCase().contains("roomtype"))) {
                        	String roomtype= new String(record.getRawValue(field));
                            iRoom.room_type =roomtype.trim();
                        }
                    } catch (ValueTooLargeException vtle) {
                        System.out.println("Exception here");
                    }
                }
                if (iRoom.room_name == null) {
                    iRoom.room_name = "Room";
                }
                if (iRoom.room_type == null) {
                    iRoom.room_type = "Simple";
                }
                iRoom.room_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                roomcount += 1;
                count += 1;
                roomList.add(iRoom);
            }
            System.out.println("--------------");
        }
         
        else if ((fileForProcess.getName().toLowerCase().contains("wall"))&&(!fileForProcess.getName().toLowerCase().contains("temp_wall"))) {
            while (recordIterator.hasNext()) {
                Walls iWall = new Walls();
                Record record = recordIterator.next();
                iWall.count = count;
                iWall.wall_count = wallcount;
                for (Field field : fieldList) {
                    try {
                        if (field.getName().toLowerCase().contains("material")) {
                            String inMaterial = new String(record.getRawValue(field));
                            iWall.wall_material = inMaterial.trim();
                        }
                        /*if(field.getName().toLowerCase().contains("type"))
                        {
                            String inType=new String(record.getRawValue(field));
                            iWall.wall_type=inType;
                        }*/
                    } catch (ValueTooLargeException vtle1) {
                        System.out.println("Exception here too!");
                    }
                }
                if (iWall.wall_material == null) {
                    iWall.wall_material = "Ordinary";
                }
                /*if (iWall.wall_type == null) {
                    iWall.wall_type = "Cement";
                }*/
                String number = fileForProcess.getName().replaceAll("\\D+", "");
                iWall.wall_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                wallcount += 1;
                count += 1;
                wallsList.add(iWall);
            }
        }
        
    else if (fileForProcess.getName().toLowerCase().contains("temp_wall")) {
        while (recordIterator.hasNext()) {
            Temp_walls iTWall = new Temp_walls();
            Record record = recordIterator.next();
            iTWall.count = count;
            iTWall.twall_count = twallcount;
            for (Field field : fieldList) {
                try {
                    if (field.getName().toLowerCase().contains("material")) {
                        String inMaterial = new String(record.getRawValue(field));
                        iTWall.twall_material = inMaterial.trim();
                    }
                } catch (ValueTooLargeException vtle1) {
                    System.out.println("Exception here too!");
                }
            }
            if (iTWall.twall_material == null) {
                iTWall.twall_material = "Ordinary";
            }
            if (iTWall.twall_type == null) {
                iTWall.twall_type = "Cement";
            }
            
            iTWall.twall_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
            twallcount += 1;
            count += 1;
            twallsList.add(iTWall);
        }
    }
        
    else if(fileForProcess.getName().toLowerCase().contains("win"))
    {
    	while(recordIterator.hasNext())
    	{
    		Windows iWin=new Windows();
    		Record record=recordIterator.next();
    		iWin.count=count;
    		iWin.win_count=windowcount;
                    iWin.win_width=5;
    		for(Field field:fieldList)
    		{
    			try{
    				if(field.getName().toLowerCase().contains("type")){
    					String winType=new String(record.getRawValue(field));
    					iWin.win_type=winType.trim();
    				}
    				if(field.getName().toLowerCase().contains("material")){
    					String winMaterial=new String(record.getRawValue(field));
    					iWin.win_material=winMaterial.trim();
    				}
    			}
    				catch(Exception e)
    				{
    					e.printStackTrace();
    				}
    		}
    		if(iWin.win_type==null)
    		{
    			iWin.win_type="Single";
    		}
    		if(iWin.win_material==null)
    		{
    			iWin.win_material="Ordinary";
    		}
            iWin.win_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
            windowcount += 1;
            count += 1;
            windowList.add(iWin);
    	}   	
    }
        else if(fileForProcess.getName().toLowerCase().contains("ex"))
        {
        	while(recordIterator.hasNext())
        	{
        		Doors iDoor=new Doors();
        		Record record=recordIterator.next();
        		iDoor.count=count;
        		iDoor.door_count=doorcount;
        		for(Field field:fieldList)
        		{
        			try{
        					if(field.getName().toLowerCase().contains("doortype"))
        					{
        						String doorType=new String(record.getRawValue(field));
        						iDoor.door_type=doorType.trim();
        					}
        					if(field.getName().toLowerCase().contains("material"))
        					{
        						String doorMaterial=new String(record.getRawValue(field));
        						iDoor.door_material=doorMaterial.trim();
        					}
        					if(field.getName().toLowerCase().contains("closetype"))
        					{
        						String closeType=new String(record.getRawValue(field));
        						iDoor.close_type=closeType.trim();
        					}
        					if(field.getName().toLowerCase().contains("direction"))
        					{	
        						String direction=new String(record.getRawValue(field));
        						if(direction.contains("inward"))
        							iDoor.open_inwards=true;
        						else
        							iDoor.open_inwards=false;
        					}
                                        
        				}
        				catch(Exception e)
        				{
        					e.printStackTrace();
        				}
        		}
        		if(iDoor.door_type==null)
        		{
        			iDoor.door_type="Single";
        		}
                if(iDoor.door_material==null)
                {
                	iDoor.door_material="Ordinary";
                }
                iDoor.door_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                doorcount += 1;
                count += 1;
                doorList.add(iDoor);
        	}   	
        }
        else if(fileForProcess.getName().toLowerCase().contains("stair"))
        {
        	while(recordIterator.hasNext())
        	{
        		Stairs iStair=new Stairs();
        		Record record=recordIterator.next();
        		iStair.count=count;
        		iStair.stair_count=staircount;
        		for(Field field:fieldList)
        		{
        			try{
        				if(field.getName().toLowerCase().contains("type")){
        					String stairType=new String(record.getRawValue(field));
        					iStair.stair_type=stairType.trim();
        				}
        				
        			}
        				catch(Exception e)
        				{
        					e.printStackTrace();
        				}
        		}
        		if(iStair.stair_type==null)
        		{
        			iStair.stair_type="Ordinary";
        		}
        		
                iStair.stair_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                staircount += 1;
                count += 1;
                stairsList.add(iStair);
        	}   	
        }
        
        
    }
}

class WallLinkClass {

    String file_loc;
    int floor_id;
}
class Floor_dist {

    double f_xdist;
    double f_ydist;
    int f_id;
}