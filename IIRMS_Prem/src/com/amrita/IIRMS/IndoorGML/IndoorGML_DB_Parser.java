package com.amrita.IIRMS.IndoorGML;
/*
 * File Name        : IndoorGML_DB_Parser.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhiya kumari N(Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : Class to parse the information from database
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointPlainShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.amrita.IIRMS.IIRMSApplicationInterface;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class IndoorGML_DB_Parser {
	
	public static Connection dbConn;
	public static Statement stmt;
	public static ResultSet rs;
	public static WKTReader reader=new WKTReader();
	public static List<IndoorGML_Room> roomList = new ArrayList<IndoorGML_Room>();
    public static List<IndoorGML_Doors> doorsList = new ArrayList<IndoorGML_Doors>();
    public static List<IndoorGML_Walls> wallsList = new ArrayList<IndoorGML_Walls>();
    public static List<IndoorGML_Windows> windowsList = new ArrayList<IndoorGML_Windows>();
    public static List<IndoorGML_Stairs> stairsList=new ArrayList<IndoorGML_Stairs>();
    public static Map<Integer, IndoorGML_Room> roomMap = new HashMap<Integer, IndoorGML_Room>();
    public static Map<Integer, IndoorGML_Doors> doorMap = new HashMap<Integer, IndoorGML_Doors>();
    public static Map<Integer, IndoorGML_Walls> wallsMap = new HashMap<Integer, IndoorGML_Walls>();
    public static Map<Integer, IndoorGML_Windows> windowsMap = new HashMap<Integer, IndoorGML_Windows>();
    public static Map<Integer,IndoorGML_Stairs> stairsMap=new HashMap<Integer,IndoorGML_Stairs>();
    public static RTree rTree;
    
  
	
	public IndoorGML_DB_Parser(String dbName) throws Exception
	{
		rTree = new RTree();
        rTree.init(new Properties());
		LoadDBDriver(dbName);
		dataAddingProcess();
	}
	
	public static void dataAddingProcess() throws Exception
	{
		getRoomData();
		getWallData();
		getDoorData();
		getWindowData();
		getStairData();
	}
	public static void getRoomData() throws Exception
	{
		float room_xmax,room_xmin,room_ymin,room_ymax;
		String queryRoom="select room_id,room_name,room_type,floor_id,area,st_asText(the_geom),st_xmin(the_geom),st_ymin(the_geom),st_xmax(the_geom),st_ymax(the_geom) from room order by room_id";
		Statement stmtRoom=dbConn.createStatement();
		ResultSet rsRoom=stmtRoom.executeQuery(queryRoom);
		while(rsRoom.next())
		{
			IndoorGML_Room igmlRoom=new IndoorGML_Room();
			igmlRoom.room_count=rsRoom.getInt(1);
			igmlRoom.room_name=rsRoom.getString(2);
			igmlRoom.room_type=rsRoom.getString(3);
			igmlRoom.room_floor=rsRoom.getInt(4);
			igmlRoom.room_area=rsRoom.getDouble(5);
			String room_wkt=rsRoom.getString(6);
			Polygon room_geom=(Polygon) reader.read(room_wkt);
			igmlRoom.room_geom=room_geom;
			igmlRoom.room_transGeom=room_geom;
			igmlRoom.room_centroid=room_geom.getCentroid();
			igmlRoom.room_xcenter=room_geom.getCentroid().getX();
			igmlRoom.room_ycenter=room_geom.getCentroid().getY();
			room_xmin =Float.parseFloat(rsRoom.getString(7));
			room_ymin=Float.parseFloat(rsRoom.getString(8));
			room_xmax=Float.parseFloat(rsRoom.getString(9));
			room_ymax=Float.parseFloat(rsRoom.getString(10));
            Rectangle rr=new Rectangle(room_xmin,room_ymin,room_xmax,room_ymax);
            rTree.add(rr, igmlRoom.count);
            roomMap.put(igmlRoom.count, igmlRoom);
			igmlRoom.count+=1;
			roomList.add(igmlRoom);
		}
	}
	
	public static void getWallData() throws Exception
	{
		float wall_xmin,wall_ymin,wall_xmax,wall_ymax;
		String queryWall="select wall_id,floor_id,st_asText(the_geom),st_xmin(the_geom),st_ymin(the_geom),st_xmax(the_geom),st_ymax(the_geom) from wall order by wall_id";
		Statement stmtWall=dbConn.createStatement();
		ResultSet rsWall=stmtWall.executeQuery(queryWall);
		while(rsWall.next())
		{
			IndoorGML_Walls igmlWalls=new IndoorGML_Walls();
			igmlWalls.wall_count=rsWall.getInt(1);
			igmlWalls.wall_floor=rsWall.getInt(2);
			String wall_wkt=rsWall.getString(3);
			MultiLineString wall_geom=(MultiLineString)reader.read(wall_wkt);
			igmlWalls.wall_geom=wall_geom;
			igmlWalls.wall_transGeom=wall_geom;
			igmlWalls.wall_centroid=wall_geom.getCentroid();
			igmlWalls.wall_xcenter=wall_geom.getCentroid().getX();
			igmlWalls.wall_ycenter=wall_geom.getCentroid().getY();
			wall_xmin=Float.parseFloat(rsWall.getString(4));
			wall_ymin=Float.parseFloat(rsWall.getString(5));
			wall_xmax=Float.parseFloat(rsWall.getString(6));
			wall_ymax=Float.parseFloat(rsWall.getString(7));
			Rectangle rr=new Rectangle(wall_xmin,wall_ymin,wall_xmax,wall_ymax);
			rTree.add(rr,igmlWalls.count);
			wallsMap.put(igmlWalls.count,igmlWalls);
			igmlWalls.count+=1;
			wallsList.add(igmlWalls);
		}
		
	}
	
	public static void getDoorData()throws Exception
	{
		float door_xmin,door_ymin,door_xmax,door_ymax;
		String queryDoor="select exit_id,floor_id,st_asText(the_geom),st_xmin(the_geom),st_ymin(the_geom),st_xmax(the_geom),st_ymax(the_geom) from exit order by exit_id";
		Statement stmtDoor=dbConn.createStatement();
		ResultSet rsDoor=stmtDoor.executeQuery(queryDoor);
		while(rsDoor.next())
		{
			IndoorGML_Doors igmlDoors=new IndoorGML_Doors();
			igmlDoors.door_count=rsDoor.getInt(1);
			igmlDoors.door_floor=rsDoor.getInt(2);
			String door_wkt=rsDoor.getString(3);
			MultiPoint door_geom=(MultiPoint)reader.read(door_wkt);
			igmlDoors.door_geom=door_geom.getEnvelope();
			igmlDoors.door_transGeom=door_geom.getEnvelope();
			igmlDoors.door_centroid=door_geom.getEnvelope().getCentroid();
			igmlDoors.door_xcenter=door_geom.getEnvelope().getCentroid().getX();
			igmlDoors.door_ycenter=door_geom.getEnvelope().getCentroid().getY();
			door_xmin=Float.parseFloat(rsDoor.getString(4));
			door_ymin=Float.parseFloat(rsDoor.getString(5));
			door_xmax=Float.parseFloat(rsDoor.getString(6));
			door_ymax=Float.parseFloat(rsDoor.getString(7));
			Rectangle rr=new Rectangle(door_xmin,door_ymin,door_xmax,door_ymax);
			rTree.add(rr, igmlDoors.count);
			doorMap.put(igmlDoors.count, igmlDoors);
			igmlDoors.count+=1;
			doorsList.add(igmlDoors);
		}
	}
	
	public static void getStairData() throws Exception
	{
		float stair_xmin,stair_ymin,stair_xmax,stair_ymax;
		String queryStairs="select stair_id,floor_id,st_asText(the_geom),st_xmin(the_geom),st_ymin(the_geom),st_xmax(the_geom),st_ymax(the_geom) from stairs order by stair_id";
		Statement stmtStairs=dbConn.createStatement();
		ResultSet rsStairs=stmtStairs.executeQuery(queryStairs);
		while(rsStairs.next())
		{
			IndoorGML_Stairs igmlStairs=new IndoorGML_Stairs();
			igmlStairs.stair_count=rsStairs.getInt(1);
			igmlStairs.stair_floor=rsStairs.getInt(2);
			igmlStairs.stair_name="ST"+igmlStairs.stair_count;
			String stair_wkt=rsStairs.getString(3);
			MultiLineString stair_geom=(MultiLineString)reader.read(stair_wkt);
			igmlStairs.stair_geom=stair_geom.getEnvelope();
			igmlStairs.stair_transGeom=stair_geom.getEnvelope();
			igmlStairs.stair_centroid=stair_geom.getEnvelope().getCentroid();
			igmlStairs.stair_xcenter=stair_geom.getEnvelope().getCentroid().getX();
			igmlStairs.stair_ycenter=stair_geom.getEnvelope().getCentroid().getY();
			stair_xmin=Float.parseFloat(rsStairs.getString(4));
			stair_ymin=Float.parseFloat(rsStairs.getString(5));
			stair_xmax=Float.parseFloat(rsStairs.getString(6));
			stair_ymax=Float.parseFloat(rsStairs.getString(7));
			Rectangle rr=new Rectangle(stair_xmin,stair_ymin,stair_xmax,stair_ymax);
			rTree.add(rr, igmlStairs.count);
			stairsMap.put(igmlStairs.count, igmlStairs);
			igmlStairs.count+=1;
			stairsList.add(igmlStairs);
		}
	}
	
	public static void getWindowData() throws Exception
	{
		float window_xmin,window_ymin,window_xmax,window_ymax;
		String queryWindows="select window_id,floor_id,st_asText(the_geom),st_xmin(the_geom),st_ymin(the_geom),st_xmax(the_geom),st_ymax(the_geom) from windows order by window_id";
		Statement stmtWindows=dbConn.createStatement();
		ResultSet rsWindows=stmtWindows.executeQuery(queryWindows);
		while(rsWindows.next())
		{
			IndoorGML_Windows igmlWindows=new IndoorGML_Windows();
			igmlWindows.window_count=rsWindows.getInt(1);
			igmlWindows.window_floor=rsWindows.getInt(2);
			String window_wkt=rsWindows.getString(3);
			MultiLineString window_geom=(MultiLineString)reader.read(window_wkt);
			igmlWindows.window_geom=window_geom.getEnvelope();
			igmlWindows.window_transGeom=window_geom.getEnvelope();
			igmlWindows.window_centroid=window_geom.getEnvelope().getCentroid();
			igmlWindows.window_xcenter=window_geom.getEnvelope().getCentroid().getX();
			igmlWindows.window_ycenter=window_geom.getEnvelope().getCentroid().getY();
			window_xmin=Float.parseFloat(rsWindows.getString(4));
			window_ymin=Float.parseFloat(rsWindows.getString(5));
			window_xmax=Float.parseFloat(rsWindows.getString(6));
			window_ymax=Float.parseFloat(rsWindows.getString(7));
			Rectangle rr=new Rectangle(window_xmin,window_ymin,window_xmax,window_ymax);
			rTree.add(rr, igmlWindows.count);
			windowsMap.put(igmlWindows.count, igmlWindows);
			igmlWindows.count+=1;
			windowsList.add(igmlWindows);
		}
	}
	
	
	/*
     * Method name              :   LoadDB
     * Method description       :   Method to load the driver of PostgreSQL database.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
	private void LoadDBDriver(String databaseName) throws Exception {
        Class.forName("org.postgresql.Driver");
        dbConn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" +databaseName, "researcher", "researcher");
    }

}
