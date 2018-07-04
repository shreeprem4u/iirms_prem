package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : IndoorGML_Doors.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to contain the information of doors required for IndoorGML.
 */
import com.infomatiq.jsi.Rectangle;
import java.util.*;
import com.vividsolutions.jts.geom.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;

public class IndoorGML_Doors {

    public int count, door_count, door_floor;
    public Geometry door_geom, door_centroid,door_transGeom;
    public double door_xmin, door_xmax, door_ymin, door_ymax, door_xcenter, door_ycenter;
    public PointData[] door_point;
    public String zonalConstraint,temporalConstraint;

    /*
     * Method name              :   IndoorGML_Doors
     * Method description       :   Constructor containing the door details (default constructor)
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_Doors() {
    }

    /*
     * Method name              :   IndoorGML_Construction_Door
     * Method description       :   Method to process the data in the required manner
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void IndoorGML_Construction_Door(List<IndoorGML_Doors> doors_list,List<MultiPointPlainShape> door_pointsList)  throws Exception {
        List<MultiPointPlainShape> multiPointList = door_pointsList;
        List<IndoorGML_Doors> wallListIn = doors_list;
        for (int i = 0, j = 0; i < multiPointList.size() && j < wallListIn.size(); i++, j++) {
            MultiPointPlainShape plsIn = multiPointList.get(i);
            IndoorGML_Doors doorsIn = wallListIn.get(j);
            doorsIn.door_xmax = plsIn.getBoxMaxX();
            doorsIn.door_xmin = plsIn.getBoxMinX();
            doorsIn.door_ymax = plsIn.getBoxMaxY();
            doorsIn.door_ymin = plsIn.getBoxMinY();
            doorsIn.door_point = plsIn.getPoints();
            ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
            pointList.add(new Coordinate(doorsIn.door_xmin, doorsIn.door_ymin));
            pointList.add(new Coordinate(doorsIn.door_xmax, doorsIn.door_ymax));
            GeometryFactory gf = new GeometryFactory();
            Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
            MultiPoint mp = gf.createMultiPoint(coordArray);
            doorsIn.door_geom = mp.getEnvelope();
            doorsIn.door_transGeom=IndoorGML_File_Parser.shiftGeometry(doorsIn.door_floor, doorsIn.door_geom);
            doorsIn.door_centroid = mp.getEnvelope().getCentroid();
            doorsIn.door_xcenter = mp.getEnvelope().getCentroid().getX();
            doorsIn.door_ycenter = mp.getEnvelope().getCentroid().getY();
            Rectangle rr = new Rectangle((float) doorsIn.door_xmin, (float) doorsIn.door_ymin, (float) doorsIn.door_xmax, (float) doorsIn.door_ymax);
            IndoorGML_File_Parser.rTree.add(rr, doorsIn.count);
            IndoorGML_File_Parser.doorMap.put(doorsIn.count, doorsIn);
        }
    }
}
