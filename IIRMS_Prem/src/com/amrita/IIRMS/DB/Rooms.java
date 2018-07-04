package com.amrita.IIRMS.DB;

/*
 * File Name        : Rooms.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow),Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 29th of September, 2015
 * Purpose          : Class to contain the data of ROOMS to be stored in database.
 */

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

public class Rooms {

    public int room_count, count, room_floor;
    public String room_name, room_type;
    public Geometry room_geom;
    public double area, volume;
    public List<Integer> door_id=new ArrayList<Integer>();
    public List<Integer> wall_id=new ArrayList<Integer>();

    /*
     * Method name              :   Rooms
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Rooms() {
    }
    
    /*
     * Method name              :   Room_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   List<Rooms> (roomList),List<PolygonShape> (polygonList)
     * Arguments description    :   "roomList"		--> contains list of room details
     * 								"polygonShape"	--> contains list of room geometries
     * Return type              :   List<Rooms>
     * Return type description  :   Returns final list with room geometry added to the room details.
     */
    public static List<Rooms> Room_Construction(List<Rooms> roomList,List<PolygonShape> polygonList) throws Exception {
     
        List<Rooms> room_finalList=new ArrayList<Rooms>();
        
        	for (int i = 0, j = 0; i < polygonList.size() && j < roomList.size(); i++, j++) 
        	{
        		PolygonShape psIn = polygonList.get(i);
        		Rooms roomIn = roomList.get(j);
                int room_floorId=roomIn.room_floor;
                        
                        ArrayList<Coordinate> pointList= new ArrayList<Coordinate>();
                        PointData[] points=psIn.getPoints();
                        for(int k=0;k<psIn.getNumberOfPoints();k++)
                        {
                        	double room_p1X = points[k].getX()-DB_Script_File.shiftDistX(room_floorId);
                        	double room_p1Y = points[k].getY()-DB_Script_File.shiftDistY(room_floorId);
                            pointList.add(new Coordinate(room_p1X, room_p1Y));   
                        }
                        GeometryFactory gf = new GeometryFactory();
                        Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
                        Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(coordArray), gf), null);
                        roomIn.room_geom = polygon;
                        roomIn.area = polygon.getArea();
                        roomIn.volume = polygon.getArea() * 150;
                        room_finalList.add(roomIn);  
        	}
			return room_finalList;
       }

    /*
     * Method name              :   MapRoomId_WallId
     * Method description       :   Method to map the rooms to their corresponding walls
     * Method Arguments         :   List<Rooms> final_roomsList,List<Walls> final_wallsList
     * Arguments description    :   "final_roomsList"	--> contains the final list of room details
     * 								"final_wallsList"	--> contains the final list of wall details
     * Return type              :   HashMap<Integer,List<Integer>>
     * Return type description  :   Returns the hashmap with wall_id mapped to list of room_id's.
     */
   
    public static HashMap<Integer, List<Integer>> MapRoomId_WallId(List<Rooms> final_roomsList,List<Walls> final_wallsList)
    {
    	HashMap<Integer,List<Integer>> map_roomIdWallId=new HashMap<Integer, List<Integer>>();
    	for(int i=0;i<final_roomsList.size();i++)
    	{
    		Rooms roomObj=final_roomsList.get(i);
    		for(int j=0;j<final_wallsList.size();j++){
    			Walls wallObj=final_wallsList.get(j);
    			if(wallObj.wall_floor==roomObj.room_floor)
    			{
    				if(roomObj.room_geom.intersects(wallObj.wall_geom))
    				{
    					roomObj.wall_id.add(wallObj.wall_count);
    					//wallObj.room_name.add(roomObj.room_name);
    				}
    			}
    		}	
    		map_roomIdWallId.put(roomObj.room_count, roomObj.wall_id);
    	}
		return map_roomIdWallId;
    }
    
    
    
    
/*
 * Method name              :   MapRoomId_DoorId
 * Method description       :   Method to map the door to their corresponding rooms
 * Method Arguments         :   List<Rooms> (final_roomList),List<Doors> (final_doorList)
 * Arguments description    :   "final_roomList"	--> contains the final list of room details
 * 								"final_doorList"	--> contains the final lisr of door details
 * Return type              :   HashMap<Integer,List<Integer>>
 * Return type description  :   Returns the hashmap with room_id mapped to list of door_id's.
 */


    public static HashMap<Integer,List<Integer>> MapRoomId_DoorId(List<Rooms> final_roomList,List<Doors> final_doorList)
    {
    	HashMap<Integer,List<Integer>> map_roomIdDoorId=new HashMap<Integer,List<Integer>>();
    	for(int i=0;i<final_roomList.size();i++)
    	{
    		Rooms roomObj=final_roomList.get(i);
    		for(int j=0;j<final_doorList.size();j++){
    			Doors doorObj=final_doorList.get(j);
    			if(doorObj.door_floor==roomObj.room_floor)
    			{
    				if(roomObj.room_geom.intersects(doorObj.door_geom))
    				{
    					roomObj.door_id.add(doorObj.door_count);
    				}
    			}
    		}
    		map_roomIdDoorId.put(roomObj.room_count, roomObj.door_id);
    	}
    	return map_roomIdDoorId;
    }

}