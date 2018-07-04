package com.amrita.IIRMS.IndoorGML;

import com.infomatiq.jsi.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

/*
 * File Name        : IndoorGML_Windows.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : Class containing the data of window, required for IndoorGML.
 */
public class IndoorGML_Windows {

    public int count, window_count, window_floor;
    public Geometry window_geom, window_centroid,window_transGeom;
    public double window_xmin, window_xmax, window_ymin, window_ymax, window_xcenter, window_ycenter;
    public PointData[] wall_point;

    /*
     * Method name              :   IndoorGML_Windows
     * Method description       :   Constructor with no parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_Windows() {
    }

    /*
     * Method name              :   IndoorGML_Construction_Window
     * Method description       :   Method for processing the data as per the requirement
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void IndoorGML_Construction_Window(List<IndoorGML_Windows> windows_list,List<PolylineShape> win_polyLineList) throws Exception
    {
        List<PolylineShape> lineList = IndoorGML_File_Parser.win_polyLineList;
        List<IndoorGML_Windows> winListIn = IndoorGML_File_Parser.windowsList;
        
        for (int i = 0, j = 0; i < lineList.size() && j < winListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            IndoorGML_Windows windowsIn = winListIn.get(j);
            windowsIn.window_xmax = plsIn.getBoxMaxX();
            windowsIn.window_xmin = plsIn.getBoxMinX();
            windowsIn.window_ymax = plsIn.getBoxMaxY();
            windowsIn.window_ymin = plsIn.getBoxMinY();
            windowsIn.wall_point = plsIn.getPoints();
            ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
            pointList.add(new Coordinate(windowsIn.window_xmin, windowsIn.window_ymin));
            pointList.add(new Coordinate(windowsIn.window_xmax, windowsIn.window_ymax));
            GeometryFactory gf = new GeometryFactory();
            Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
            LineString ls = gf.createLineString(coordArray);
            windowsIn.window_geom = ls.getEnvelope();
            windowsIn.window_transGeom=IndoorGML_File_Parser.shiftGeometry(windowsIn.window_floor, windowsIn.window_geom);
            windowsIn.window_centroid = ls.getCentroid();
            windowsIn.window_xcenter = ls.getCentroid().getX();
            windowsIn.window_xcenter = ls.getCentroid().getY();
            Rectangle rr = new Rectangle((float) windowsIn.window_xmin, (float) windowsIn.window_ymin, (float) windowsIn.window_xmax, (float) windowsIn.window_ymax);
            IndoorGML_File_Parser.rTree.add(rr, windowsIn.count);
            IndoorGML_File_Parser.windowsMap.put(windowsIn.count, windowsIn);
            //gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);
        }
 }
}
