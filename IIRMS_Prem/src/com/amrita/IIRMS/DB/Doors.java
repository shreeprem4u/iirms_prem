package com.amrita.IIRMS.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointPlainShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/*
 * File Name        : Doors.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 29th of September, 2015
 * Purpose          : Class containing the data for DOORS to be stored in database.
 */
public class Doors {
    
    public int door_count, count, door_floor=0;
    public String door_type,door_material,close_type,room_name;
    public Geometry door_geom;
    public List<Integer> room_id=new ArrayList<Integer>();
    public boolean open_inwards;
    
    /*
     * Method name              :   Doors
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Doors() {
    }

    /*
     * Method name              :   Doors_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   List<Doors> (doorListIn),List<MultiPointPlainShape> (pointsList)
     * Arguments description    :   "doorListIn"	--> contains list of door details
     * 								"pointsList"	--> contains list of door geometries
     * Return type              :   List<Doors>
     * Return type description  :   Returns the final list with door geometry added to the door details.
     */
    public static List<Doors> Doors_Construction(List<Doors> doorListIn,List<MultiPointPlainShape> pointsList)throws Exception {
        
        List<Doors> door_finalList= new ArrayList<Doors>();
        
        for (int i = 0, j = 0; i < pointsList.size() && j < doorListIn.size(); i++, j++) {
            MultiPointPlainShape mppsIn = pointsList.get(i);
            Doors doorIn = doorListIn.get(j);
            int door_floorId=doorIn.door_floor;
            ArrayList<Point> pointsArrList=new ArrayList<Point>();
            List<Rooms> final_roomList=DB_Script_File.finalRoomList;
            Geometry doorGeom;
            PointData[] points=mppsIn.getPoints();
            for(int k=0;k<mppsIn.getNumberOfPoints();k++)
            {
            	double door_ptX=points[k].getX()-DB_Script_File.shiftDistX(door_floorId);
            	double door_ptY=points[k].getY()-DB_Script_File.shiftDistY(door_floorId);
            	Coordinate coord=new Coordinate(door_ptX,door_ptY);
            	GeometryFactory gf = new GeometryFactory();
            	Point p=gf.createPoint(coord);
            	pointsArrList.add(p);
            }
            
            GeometryFactory gf1 = new GeometryFactory();
            Point[] pointsArr=pointsArrList.toArray(new Point[pointsArrList.size()]);
            MultiPoint mp=gf1.createMultiPoint(pointsArr);
            doorIn.door_geom=mp;
            doorGeom=doorIn.door_geom;
            door_finalList.add(doorIn);
        }
        return door_finalList;
    }
    
    /*
     * Method name              :   MapDoorId_RoomId
     * Method description       :   Method to map the door to their corresponding rooms
     * Method Arguments         :   List<Doors> (final_doorList),List<Rooms> (final_roomList)
     * Arguments description    :   "final_doorList"	--> contains the final list of door details.
     * 								"final_roomList"	--> contains the final list of room details.
     * Return type              :   HashMap<Integer,List<Integer>>
     * Return type description  :   Returns the hashmap with door_id mapped to list of room_id's.
     */
    
    public static HashMap<Integer,List<Integer>> MapDoorId_RoomId(List<Doors> final_doorList,List<Rooms> final_roomList)
    {
    	HashMap<Integer,List<Integer>> map_doorIdRoomId=new HashMap<Integer,List<Integer>>();
    	for(int i=0;i<final_doorList.size();i++)
    	{
    		Doors doorObj=final_doorList.get(i);
    		for(int j=0;j<final_roomList.size();j++){
            	Rooms roomObj=final_roomList.get(j);
            	if(roomObj.room_floor==doorObj.door_floor)
            	{
            		if(doorObj.door_geom.intersects(roomObj.room_geom))
            		{
            			doorObj.room_id.add(roomObj.room_count);
            		}
            	}
            }
    		map_doorIdRoomId.put(doorObj.door_count, doorObj.room_id);
    	}
    	return map_doorIdRoomId;
    }
    
}
