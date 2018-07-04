package com.amrita.IIRMS.IndoorGML;
/*
 * File Name        : IndoorGML_Stairs.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhiya Kumari N (Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : Class containing the data of Stairs, required for IndoorGML.
 */
import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointPlainShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.infomatiq.jsi.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public class IndoorGML_Stairs {
	public int count,stair_count,stair_floor;
	public String stair_name;
	public Geometry stair_geom, stair_centroid,stair_transGeom;
	public double stair_xmin, stair_xmax, stair_ymin, stair_ymax;
	public double stair_xcenter;
	public double stair_ycenter;
    //public PointData[] stair_point;
    
    /*
     * Method name              :   IndoorGML_Stairs
     * Method description       :   Constructor containing the stair details (default constructor)
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_Stairs() {
    }

    /*
     * Method name              :   IndoorGML_Construction_Stair
     * Method description       :   Method to process the data in the required manner
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void IndoorGML_Construction_Stair(List<IndoorGML_Stairs> stairs_list,List<PolylineShape> stairs_polylineList) throws Exception{
        List<PolylineShape> lineList = stairs_polylineList;
        List<IndoorGML_Stairs> stairListIn = stairs_list;
        for (int i = 0, j = 0; i < lineList.size() && j < stairListIn.size(); i++, j++) {
        	PolylineShape plsIn = lineList.get(i);
            IndoorGML_Stairs stairsIn = stairListIn.get(j);
            stairsIn.stair_xmax = plsIn.getBoxMaxX();
            stairsIn.stair_xmin = plsIn.getBoxMinX();
            stairsIn.stair_ymax = plsIn.getBoxMaxY();
            stairsIn.stair_ymin = plsIn.getBoxMinY();
            //stairsIn.stair_point = plsIn.getPoints();
            ArrayList<LineString> lineStrList=new ArrayList<LineString>();
            for(int k=0;k<plsIn.getNumberOfParts();k++)
            {
            	PointData[] points=plsIn.getPointsOfPart(k);
          
            	double st_p1X = points[0].getX();
            	double st_p1Y = points[0].getY();
            	double st_p2X = points[1].getX();
            	double st_p2Y = points[1].getY();
            	
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
            //stairsIn.stair_geom = mls;
            stairsIn.stair_name="ST"+stairsIn.stair_count;
            stairsIn.stair_geom=mls.getEnvelope();
            stairsIn.stair_transGeom=IndoorGML_File_Parser.shiftGeometry(stairsIn.stair_floor, stairsIn.stair_geom);
            stairsIn.stair_centroid = mls.getCentroid();
            stairsIn.stair_xcenter = mls.getCentroid().getX();
            stairsIn.stair_ycenter = mls.getCentroid().getY();
            Rectangle rr = new Rectangle((float) stairsIn.stair_xmin, (float) stairsIn.stair_ymin, (float) stairsIn.stair_xmax, (float) stairsIn.stair_ymax);
            IndoorGML_File_Parser.rTree.add(rr, stairsIn.count);
            IndoorGML_File_Parser.stairsMap.put(stairsIn.count, stairsIn);
        }
    }
}