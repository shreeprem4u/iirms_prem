package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : IndoorGML_Walls.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class containing the data of walls, required for IndoorGML.
 */
import com.infomatiq.jsi.Rectangle;
import java.util.*;
import com.vividsolutions.jts.geom.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;

public class IndoorGML_Walls {

    public int count, wall_count, wall_floor;
    public Geometry wall_geom, wall_centroid,wall_transGeom;
    public double wall_xmin, wall_xmax, wall_ymin, wall_ymax, wall_xcenter, wall_ycenter;
    public PointData[] wall_point;

    /*
     * Method name              :   IndoorGML_Walls
     * Method description       :   Constructor with no parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_Walls() {
    }

    /*
     * Method name              :   IndoorGML_Construction_Wall
     * Method description       :   Method to format the data as per the requirement.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void IndoorGML_Construction_Wall(List<IndoorGML_Walls> walls_list,List<PolylineShape> wall_polylineList) throws Exception{
        List<PolylineShape> lineList = wall_polylineList;
        List<IndoorGML_Walls> wallListIn = walls_list;
        for (int i = 0, j = 0; i < lineList.size() && j < wallListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            IndoorGML_Walls wallsIn = wallListIn.get(j);
            wallsIn.wall_xmax = plsIn.getBoxMaxX();
            wallsIn.wall_xmin = plsIn.getBoxMinX();
            wallsIn.wall_ymax = plsIn.getBoxMaxY();
            wallsIn.wall_ymin = plsIn.getBoxMinY();
            wallsIn.wall_point = plsIn.getPoints();
            ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
            pointList.add(new Coordinate(wallsIn.wall_xmin, wallsIn.wall_ymin));
            pointList.add(new Coordinate(wallsIn.wall_xmax, wallsIn.wall_ymax));
            GeometryFactory gf = new GeometryFactory();
            Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
            LineString ls = gf.createLineString(coordArray);
            wallsIn.wall_geom = ls.getEnvelope();
            wallsIn.wall_transGeom=IndoorGML_File_Parser.shiftGeometry(wallsIn.wall_floor, wallsIn.wall_geom);
            wallsIn.wall_centroid = ls.getCentroid();
            wallsIn.wall_xcenter = ls.getCentroid().getX();
            wallsIn.wall_xcenter = ls.getCentroid().getY();
            Rectangle rr = new Rectangle((float) wallsIn.wall_xmin, (float) wallsIn.wall_ymin, (float) wallsIn.wall_xmax, (float) wallsIn.wall_ymax);
            IndoorGML_File_Parser.rTree.add(rr, wallsIn.count);
            IndoorGML_File_Parser.wallsMap.put(wallsIn.count, wallsIn);
            //gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);
        }
    }
}