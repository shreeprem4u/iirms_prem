package com.amrita.IIRMS.DB;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineMShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

/*
 * File Name        : Stairs.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 28th of September, 2015
 * Purpose          : Class to contain the data of STAIRS to be stored in database.
 */
public class Stairs {
    
	public int stair_count, count, stair_floor =0;
    public String stair_type;
    public Geometry stair_geom;
    
    /*
     * Method name              :   Windows
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Stairs() {
    }

    /*
     * Method name              :   Stairs_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   List<Stairs> (stairsListIn),List<PolylineShape> lineList
     * Arguments description    :   "stairsListIn"	--> contains list of stair details
     * 								"lineList"		--> contains list of stair geometries
     * Return type              :   List<Stairs>
     * Return type description  :   Returns final list with stair geometries added to the stair details.
     */
    public static List<Stairs> Stairs_Construction(List<Stairs> stairListIn,List<PolylineShape> lineList)throws Exception {
        
        List<Stairs> stair_finalList = new ArrayList<Stairs>();
        
        for (int i = 0, j = 0; i < lineList.size() && j < stairListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            Stairs stairIn = stairListIn.get(j);
            int floor_id=stairIn.stair_floor;
            ArrayList<LineString> lineStrList=new ArrayList<LineString>();
            for(int k=0;k<plsIn.getNumberOfParts();k++)
            {
            	PointData[] points=plsIn.getPointsOfPart(k);
          
            	double st_p1X = points[0].getX()-DB_Script_File.shiftDistX(floor_id);
            	double st_p1Y = points[0].getY()-DB_Script_File.shiftDistY(floor_id);
            	double st_p2X = points[1].getX()-DB_Script_File.shiftDistX(floor_id);
            	double st_p2Y = points[1].getY()-DB_Script_File.shiftDistY(floor_id);
            	
            	ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
                pointList.add(new Coordinate(st_p1X, st_p1Y));
                pointList.add(new Coordinate(st_p2X, st_p2Y));
                GeometryFactory gf = new GeometryFactory();
                Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
    
                LineString ls = gf.createLineString(coordArray);
                lineStrList.add(ls);	
            	
            }
            GeometryFactory gf1 = new GeometryFactory();
            LineString[] lineArray=lineStrList.toArray(new LineString[lineStrList.size()]);
            MultiLineString mls=gf1.createMultiLineString(lineArray);
            stairIn.stair_geom = mls;
            stair_finalList.add(stairIn);  
        }
        return stair_finalList;
    }
}
