package com.amrita.IIRMS.DB;

/*
 * File Name        : Walls.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 29th of September, 2015
 * Purpose          : Class to store the data of WALLS to be stored in the database.
 */
import com.vividsolutions.jts.geom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

public class Walls {

    public int wall_count, count, wall_floor =0, window_id;
    public String wall_type, wall_material;
    public Geometry wall_geom;
    public ArrayList<String> room_name=new ArrayList<String>();
    public ArrayList<Integer> room_id=new ArrayList<Integer>();

    /*
     * Method name              :   Walls
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Walls() {
    }

    /*
     * Method name              :   Wall_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   List<Walls> (wallListIn),List<PolylineShape> (lineList)
     * Arguments description    :   "wallListIn"	--> contains list of wall details
     * 								"PolylineShape"	--> contains list of wall geometries 
     * Return type              :   List<Walls>
     * Return type description  :   Returns final list with wall geometry details added to the wall details.
     */
    public static List<Walls> Wall_Construction(List<Walls> wallListIn,List<PolylineShape> lineList) throws Exception{
       
        List<Walls> wall_finalList = new ArrayList<Walls>();
        
        for (int i = 0, j = 0; i < lineList.size() && j < wallListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            Walls wallsIn = wallListIn.get(j);
            int wall_floorId=wallsIn.wall_floor;
            ArrayList<LineString> lineStrList=new ArrayList<LineString>();
            Geometry wallGeom;
            for(int k=0;k<plsIn.getNumberOfParts();k++)
            {
            	PointData[] points=plsIn.getPointsOfPart(k);
            	double wall_p1X = points[0].getX()-DB_Script_File.shiftDistX(wall_floorId);
            	double wall_p1Y = points[0].getY()-DB_Script_File.shiftDistY(wall_floorId);
            	double wall_p2X = points[1].getX()-DB_Script_File.shiftDistX(wall_floorId);
            	double wall_p2Y = points[1].getY()-DB_Script_File.shiftDistY(wall_floorId);
            	
            	ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
                pointList.add(new Coordinate(wall_p1X, wall_p1Y));
                pointList.add(new Coordinate(wall_p2X, wall_p2Y));
                GeometryFactory gf = new GeometryFactory();
                Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
                
                LineString ls = gf.createLineString(coordArray);
                lineStrList.add(ls);
            }
            
            GeometryFactory gf1 = new GeometryFactory();
            LineString[] lineArray=lineStrList.toArray(new LineString[lineStrList.size()]);
            MultiLineString mls=gf1.createMultiLineString(lineArray);
            wallsIn.wall_geom = mls;
            wallGeom=wallsIn.wall_geom;            
            wall_finalList.add(wallsIn);
        }
        return wall_finalList;
    }
    
    /*
     * Method name              :   MapWallId_RoomId
     * Method description       :   Method to map the wall to their corresponding rooms
     * Method Arguments         :   List<Walls> final_wallsList,List<Rooms> final_roomsList
     * Arguments description    :   "final_wallsList"	--> contains the final list of wall details.
     * 								"final_roomsList"	--> contains the final list of room details.
     * Return type              :   HashMap<Integer,List<Integer>>
     * Return type description  :   Returns the hashmap with wall_id mapped to list of room_id's.
     */
   
    public static HashMap<Integer, List<Integer>> MapWallId_RoomId(List<Walls> final_wallsList,List<Rooms> final_roomsList)
    {
    	HashMap<Integer,List<Integer>> map_roomIdWallId=new HashMap<Integer, List<Integer>>();
    	for(int i=0;i<final_wallsList.size();i++)
    	{
    		Walls wallObj=final_wallsList.get(i);
    		for(int j=0;j<final_roomsList.size();j++){
    			Rooms roomObj=final_roomsList.get(j);
    			if(roomObj.room_floor==wallObj.wall_floor)
    			{
    				if(wallObj.wall_geom.intersects(roomObj.room_geom))
    				{
    					wallObj.room_id.add(roomObj.room_count);
    					//wallObj.room_name.add(roomObj.room_name);
    				}
    			}
    		}	
    		map_roomIdWallId.put(wallObj.wall_count, wallObj.room_id);
    	}
		return map_roomIdWallId;
    }
    

    /*
     * Method name              :   MapWallId_WallType
     * Method description       :   Method to map the rooms to their corresponding walls
     * Method Arguments         :   List<Walls> (final_wallsList),List<Rooms> (final_RoomsList)
     * Arguments description    :   "final_wallsList"	--> contains the final list of wall details
     * 								"final_roomsList"	--> contains the final list of room details
     * Return type              :   HashMap<Integer,String>
     * Return type description  :   Returns the hashmap with wall_id mapped to wall_type.
     */
    
    public static HashMap<Integer,String> MapWallId_WallType(List<Walls> final_wallsList,List<Rooms> final_roomsList)
    {
    	HashMap<Integer,String> map_WallTypeRoom=new HashMap<Integer,String>();
    	List<Integer> roomIdArray;
    	for(int i=0;i<final_wallsList.size();i++)
    	{
    		roomIdArray=new ArrayList<Integer>();
    		Walls wallObj=final_wallsList.get(i);
    		for(int j=0;j<final_roomsList.size();j++){
    			Rooms roomObj=final_roomsList.get(j);
    			if(roomObj.room_floor==wallObj.wall_floor)
    			{
    				if(wallObj.wall_geom.intersects(roomObj.room_geom))
    				{
    					roomIdArray.add(roomObj.room_count);
    					//wallObj.room_name.add(roomObj.room_name);
    				}
    			}
    		}
    		if(roomIdArray.size()==1)
    		{
    			wallObj.wall_type="outer";		
    		}
    		if(roomIdArray.size()>1)
    		{
    			wallObj.wall_type="common";
    		}
    		map_WallTypeRoom.put(wallObj.wall_count, wallObj.wall_type);
    	}
    	return map_WallTypeRoom;
    }
}
