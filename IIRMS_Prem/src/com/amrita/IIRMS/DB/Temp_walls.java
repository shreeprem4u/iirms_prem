
package com.amrita.IIRMS.DB;

/*
 * File Name        : Temp_walls.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kuamri N (Junior Research Fellow)
 * Last Modified    : the 6th of January, 2014
 * Purpose          : Class to store the data of temporary WALLS to be stored in the database.
 */
import com.vividsolutions.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

public class Temp_walls {

    public int twall_count, count, twall_floor =0, room_id;
    public String twall_type, twall_material;
    public Geometry twall_geom;

    /*
     * Method name              :   Walls
     * Method description       :   Constructor with no-parameters
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public Temp_walls() {
    }

    /*
     * Method name              :   Wall_Construction
     * Method description       :   Method to process the data as per the requirement
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void tempWall_Construction()throws Exception {
        List<PolylineShape> lineList = DB_Script_File.tempWall_polyLineList;
        List<Temp_walls> twallListIn = DB_Script_File.twallsList;
        for (int i = 0, j = 0; i < lineList.size() && j < twallListIn.size(); i++, j++) {
            PolylineShape plsIn = lineList.get(i);
            Temp_walls twallsIn = twallListIn.get(j);
            int floor_id=twallsIn.twall_floor;
            double twall_xmax = plsIn.getBoxMaxX()-DB_Script_File.shiftDistX(floor_id);
            double twall_xmin = plsIn.getBoxMinX()-DB_Script_File.shiftDistX(floor_id);
            double twall_ymax = plsIn.getBoxMaxY()-DB_Script_File.shiftDistY(floor_id);
            double twall_ymin = plsIn.getBoxMinY()-DB_Script_File.shiftDistY(floor_id);
            ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
            pointList.add(new Coordinate(twall_xmin, twall_ymin));
            pointList.add(new Coordinate(twall_xmax, twall_ymax));
            GeometryFactory gf = new GeometryFactory();
            Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
            LineString ls = gf.createLineString(coordArray);
            twallsIn.twall_geom = ls.getGeometryN(0);
            DB_Script_File.finaltempWallList.add(twallsIn);
        }
    }
}
