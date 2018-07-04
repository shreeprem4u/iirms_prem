package com.amrita.IIRMS.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

/*
 * File Name        : Windows.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 28th of September, 2015
 * Purpose          : Class to contain the data of WINDOWS to be stored in database.
 */
public class Windows {
    
    public int win_count, count, win_floor =0,win_width,room_id,wall_id;
    public String win_type,win_material,room_name;
    public Geometry win_geom;
    
    /*
     * Method name              :   Windows
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Windows() {
    }

    /*
     * Method name              :   Window_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   List<Windows> (winListIn),List<PolylineShape> (lineList)
     * Arguments description    :   "winListIn"		--> contains list of window details.
     * 								"lineList"		--> contains list of window geometries.	
     * Return type              :   List<Windows>
     * Return type description  :   Returns the final list of window geometries added to the window details .
     */
    public static List<Windows> Window_Construction(List<Windows> winListIn,List<PolylineShape>lineList)throws Exception {
       
    	List<Windows> window_finalList=new ArrayList<Windows>();
       
        for (int i = 0, j = 0; i < lineList.size() && j < winListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            Windows windIn = winListIn.get(j);
            int win_floorId=windIn.win_floor;
            ArrayList<LineString> lineStrList=new ArrayList<LineString>();
            Geometry winGeom;
            for(int k=0;k<plsIn.getNumberOfParts();k++)
            {
            	PointData[] points=plsIn.getPointsOfPart(k);
            	
            	double win_p1X = points[0].getX()-DB_Script_File.shiftDistX(win_floorId);
            	double win_p1Y = points[0].getY()-DB_Script_File.shiftDistY(win_floorId);
            	double win_p2X = points[1].getX()-DB_Script_File.shiftDistX(win_floorId);
            	double win_p2Y = points[1].getY()-DB_Script_File.shiftDistY(win_floorId);
            	
            	ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
                pointList.add(new Coordinate(win_p1X, win_p1Y));
                pointList.add(new Coordinate(win_p2X, win_p2Y));
                GeometryFactory gf = new GeometryFactory();
                Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
    
                LineString ls = gf.createLineString(coordArray);
                lineStrList.add(ls);	
            	
            }
            GeometryFactory gf1 = new GeometryFactory();
            LineString[] lineArray=lineStrList.toArray(new LineString[lineStrList.size()]);
            MultiLineString mls=gf1.createMultiLineString(lineArray);
            windIn.win_geom = mls;
            winGeom=windIn.win_geom;
            window_finalList.add(windIn);
        }
        return window_finalList;
    }
    
    /*
     * Method name              :   MapWindowId_RoomId
     * Method description       :   Method to map the windows to their corresponding rooms
     * Method Arguments         :   List<Windows> (final_windowList),List<Rooms> (final_roomList)
     * Arguments description    :   "final_windowList"	--> contains the final list of window details
     * 								"final_roomsList"	--> contains the final list of room detals
     * Return type              :   HashMap<Integer,Integer>
     * Return type description  :   Returns the hashmap with window_id mapped to room_id.
     */
    
    public static HashMap<Integer,Integer> MapWindowId_RoomId(List<Windows> final_windowList,List<Rooms> final_roomList)
    {
    	HashMap<Integer,Integer> map_windowRoom=new HashMap<Integer, Integer>();
    	for(int i=0;i<final_windowList.size();i++)
    	{
    		Windows windowObj=final_windowList.get(i);
    		
    		for(int j=0;j<final_roomList.size();j++){
    			Rooms roomObj=final_roomList.get(j);
    			if(roomObj.room_floor==windowObj.win_floor)
    			{
    				if(windowObj.win_geom.intersects(roomObj.room_geom))
    				{
    					windowObj.room_id=roomObj.room_count;
    					//windowObj.room_name=roomObj.room_name;
    					//System.out.println("windID:"+windowObj.count+" "+"windFloor:"+windowObj.win_floor+"windRoom:"+windowObj.room_name);
    				}
    			}
    		}
    		//System.out.println("windID:"+windIn.count+" "+"windFloor:"+windIn.win_floor+"windRoom:"+windIn.room_name);
            //System.out.println("windRoomId:"+windIn.room_id);
    		map_windowRoom.put(windowObj.win_count,windowObj.room_id);
    	}
    	return map_windowRoom;
    }
    
    /*
     * Method name              :   MapWindowId_WallId
     * Method description       :   Method to map the windows to their corresponding walls
     * Method Arguments         :   List<Windows> (final_windowList),List<Walls> (final_wallList)
     * Arguments description    :   "final_windowList"	--> contains the final list of window details
     * 								"final_wallList"	--> contains the final list of wall details
     * Return type              :   HashMap<Integer,Integer>
     * Return type description  :   Returns the hashmap with window_id mapped to wall_id.
     */
    
    public static HashMap<Integer,Integer> MapWindowId_WallId(List<Windows> final_windowList,List<Walls> final_wallList)
    {
    	HashMap<Integer,Integer> map_windowWall=new HashMap<Integer,Integer>();
    	for(int i=0;i<final_windowList.size();i++)
    	{
    		Windows windowObj=final_windowList.get(i);
    		
            for(int n=0;n<final_wallList.size();n++){
            	Walls wallObj=final_wallList.get(n);
            	if(wallObj.wall_floor==windowObj.win_floor)
            	{
            		if(windowObj.win_geom.intersects(wallObj.wall_geom))
            		{
            			windowObj.wall_id=wallObj.wall_count;
            		}
            	}
            }
            map_windowWall.put(windowObj.win_count,windowObj.wall_id);
    	}
    	return map_windowWall;
    }
}
