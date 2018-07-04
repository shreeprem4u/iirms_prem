package com.amrita.IIRMS.Navigation;


/*
 * File Name        : Navigation.java
 * Project Name     : Indoor Information Representation and Management System
 * Author           : Sindhya Kumari N (Junior Research Fellow) 
 * Last Modified    : the 18th of August, 2015
 * Purpose          : Class to create a graph from IndoorGML data and find the path
 */

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

public class Navigation {
	
	public static SimpleDirectedWeightedGraph<String,DefaultWeightedEdge> graph=new SimpleDirectedWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	public static Connection conn;
	public static java.util.List<PointDetails> pointsList = new java.util.ArrayList<PointDetails>();
	
	/*
     * Method name              :   Navigation
     * Method description       :   Constructor to create a connectivity graph based on IndoorGML data.
     * Method Arguments         :   String (dbName)
     * Arguments description    :   "dbName"    --> Name of the database to be connected
     * Return type              :   null
     * Return type description  :   --
     */
	
	public Navigation(String dbName) throws Exception{
		LoadDriverForDB(dbName);
		
		String queryState="SELECT state_id FROM state_indoorgml";
		Statement stmtState=conn.createStatement();
		ResultSet rsState=stmtState.executeQuery(queryState);
		while(rsState.next())
		{
			graph.addVertex(rsState.getString(1));
		}
		
		String queryGraph="SELECT startnode,endnode FROM transition_indoorgml where relation='connectivity'";
		Statement stmtGraph=conn.createStatement();
		ResultSet rsGraph=stmtGraph.executeQuery(queryGraph);
		while(rsGraph.next())
		{
			graph.addEdge(rsGraph.getString(1),rsGraph.getString(2));
			graph.addEdge(rsGraph.getString(2), rsGraph.getString(1));
		}
	}
	
	/*
     * Method name              :   findPath
     * Method description       :   Method to find the path between two indoor entities
     * Method Arguments         :   String(srcName),String(destName)
     * Arguments description    :   "srcName"    --> Name of the source room 
     * 								"destName"	--->Name of the destination room
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
	
	public static List<String> findPath(String srcName,String destName) throws Exception
	{
		String source="",destination="";
		Point srcPoint = null ,destPoint=null;
		int src_floor_id=0,dest_floor_id=0;
		String querySource="SELECT state_id,st_x(the_geom),st_y(the_geom),floor_id FROM state_indoorgml where state_name='"+srcName+"'";
		Statement stmtSource=conn.createStatement();
		ResultSet rsSource=stmtSource.executeQuery(querySource);
		while(rsSource.next())
		{
			source=rsSource.getString(1);
			Coordinate srcCoord=new Coordinate(rsSource.getDouble(2),rsSource.getDouble(3));
			GeometryFactory gf = new GeometryFactory();
           	srcPoint=gf.createPoint(srcCoord);
           	src_floor_id=rsSource.getInt(4);
		}
		String queryDest="SELECT state_id,st_x(the_geom),st_y(the_geom),floor_id FROM state_indoorgml where state_name='"+destName+"'";
		Statement stmtDest=conn.createStatement();
		ResultSet rsDest=stmtDest.executeQuery(queryDest);
		while(rsDest.next())
		{
			destination=rsDest.getString(1);
			Coordinate destCoord=new Coordinate(rsDest.getDouble(2),rsDest.getDouble(3));
			GeometryFactory gf = new GeometryFactory();
           	destPoint=gf.createPoint(destCoord);
           	dest_floor_id=rsDest.getInt(4);
		}
		
		//load source,destination details to database
		String queryInsSrc="INSERT INTO path(the_geom,floor_id,ipaddress) VALUES('"+srcPoint+"',"+src_floor_id+",'" + InetAddress.getLocalHost().getHostAddress() + "')";
		Statement stmtInsSrc=conn.createStatement();
		stmtInsSrc.executeUpdate(queryInsSrc);
						
		String queryInsDest="INSERT INTO path(the_geom,floor_id,ipaddress) VALUES('"+destPoint+"',"+dest_floor_id+",'" + InetAddress.getLocalHost().getHostAddress() + "')";
		Statement stmtInsDest=conn.createStatement();
		stmtInsDest.executeUpdate(queryInsDest);
		
		//find the path between the source and destination
		List shortestPath=DijkstraShortestPath.findPathBetween(graph, source, destination);
		//System.out.println("ShortestPath:"+shortestPath);
		List<String> path=new ArrayList<String>();
		for(int i=0;i<shortestPath.size();i++)
		{
			String[] states1=shortestPath.get(i).toString().replace("(","").replace(")", "").split(":");
			path.add(states1[0].trim());
			path.add(states1[1].trim());
		}
		//loadPathToDB(path);
		return path;
	}
	
	/*
     * Method name              :   loadPathToDB
     * Method description       :   Method to retrieve the points in the path and store in the database
     * Method Arguments         :   String(srcName),String(destName),path(List<String>)
     * Arguments description    :   "path"  ---> List that contains the path between source and destination
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
	
	public static void loadPathToDB(List<String> path) throws Exception
	{
		
		//To retrieve the points of the indoor entities in the path
		for(int i=0;i<path.size();i++)
		{
			String path_state=path.get(i);
			
			String queryPath="SELECT st_x(the_geom),st_y(the_geom),floor_id FROM state_indoorgml where state_id='"+path_state+"'";
			Statement stmtPath=conn.createStatement();
			ResultSet rsPath=stmtPath.executeQuery(queryPath);
			PointDetails pdObj=new PointDetails();
			while(rsPath.next())
			{
				pdObj.x=Float.parseFloat(rsPath.getString(1));
				pdObj.y=Float.parseFloat(rsPath.getString(2));
				pdObj.floor_id=rsPath.getInt(3);
			}
			pointsList.add(pdObj);
		}
			
		
		//To create a 3D path the source and destination floor_id for stairs are to be changed because each stair connects 2 floors
		for(int j=0;j<pointsList.size();j=j+2)
		{
			PointDetails pdObj1=pointsList.get(j);
			PointDetails pdObj2=pointsList.get(j+1);
			int src_floor=pdObj1.floor_id;
			int dest_floor=pdObj2.floor_id;
			
			if(!(src_floor==dest_floor))
			{
				if(pdObj2.floor_id>pdObj1.floor_id)
				{
					pdObj1.floor_id=pdObj2.floor_id;
				}
				else
				{
					pdObj2.floor_id=pdObj1.floor_id;
				}
			}
		}	
		
		int floorNow=0;
		MultiLineString mls;
		ArrayList<LineString> lineStrList=new ArrayList<LineString>();
		
		//create multilinestring from points and store it in the database
		for(int j=0;j<pointsList.size();j=j+2)
		{
			PointDetails pdObj1=pointsList.get(j);
			PointDetails pdObj2=pointsList.get(j+1);
			if(j==0)
			{
				ArrayList<Coordinate> ptList = new ArrayList<Coordinate>();
				ptList.add(new Coordinate(pdObj1.x, pdObj1.y));
				ptList.add(new Coordinate(pdObj2.x, pdObj2.y));
				floorNow=pdObj1.floor_id;
				GeometryFactory gf = new GeometryFactory();
				Coordinate[] pointArray = ptList.toArray(new Coordinate[ptList.size()]);
				LineString ls = gf.createLineString(pointArray);
				lineStrList.add(ls);
			}
			else
			{
				PointDetails pdObjPrev=pointsList.get(j-1);
				if(pdObj1.floor_id==pdObjPrev.floor_id)
				{
					floorNow=pdObj1.floor_id;
					ArrayList<Coordinate> ptList = new ArrayList<Coordinate>();
					ptList.add(new Coordinate(pdObj1.x, pdObj1.y));
					ptList.add(new Coordinate(pdObj2.x, pdObj2.y));
					GeometryFactory gf = new GeometryFactory();
					Coordinate[] pointArray = ptList.toArray(new Coordinate[ptList.size()]);
					LineString ls = gf.createLineString(pointArray);
					lineStrList.add(ls);
				}
				else
				{
					GeometryFactory gf1 = new GeometryFactory();
					LineString[] linestringArray=lineStrList.toArray(new LineString[lineStrList.size()]);
					mls=gf1.createMultiLineString(linestringArray);
					String queryPathGeom="INSERT INTO path(the_geom,floor_id,ipaddress) VALUES('"+mls+"',"+floorNow+",'" + InetAddress.getLocalHost().getHostAddress() + "')";
					Statement stmtPathGeom=conn.createStatement();
					stmtPathGeom.executeUpdate(queryPathGeom);
					
					lineStrList.clear();
					floorNow=pdObj1.floor_id;
					ArrayList<Coordinate> ptList = new ArrayList<Coordinate>();
					ptList.add(new Coordinate(pdObj1.x, pdObj1.y));
					ptList.add(new Coordinate(pdObj2.x, pdObj2.y));
					GeometryFactory gf = new GeometryFactory();
					Coordinate[] pointArray = ptList.toArray(new Coordinate[ptList.size()]);
					LineString ls = gf.createLineString(pointArray);
					lineStrList.add(ls);
				}
			}
			if(j==(pointsList.size()-2))
			{
				GeometryFactory gf1 = new GeometryFactory();
				LineString[] linestringArray=lineStrList.toArray(new LineString[lineStrList.size()]);
				mls=gf1.createMultiLineString(linestringArray);
				String queryPathGeom="INSERT INTO path(the_geom,floor_id,ipaddress) VALUES('"+mls+"',"+floorNow+",'" + InetAddress.getLocalHost().getHostAddress() + "')";
				Statement stmtPathGeom=conn.createStatement();
				stmtPathGeom.executeUpdate(queryPathGeom);
			}
		}
	}
	
	/*
     * Method name              :   LoadDriverForDB
     * Method description       :   Method to load the driver for the application's database.
     * Method Arguments         :   String (nameIn)
     * Arguments description    :   "nameIn"    --> Name of the database to be connected
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void LoadDriverForDB(String nameIn) throws Exception {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + nameIn, "researcher", "researcher");
    }
}

