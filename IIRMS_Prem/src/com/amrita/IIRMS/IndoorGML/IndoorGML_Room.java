package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : IndoorGML_Room.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 28th of September, 2015
 * Purpose          : Class containing the details of the room required for IndoorGML.
 */
import com.infomatiq.jsi.Rectangle;
import java.util.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;

public class IndoorGML_Room {

    public List<IndoorGML_Room> roomAdjacent, roomOverlap, roomContains, roomConnected, roomTemp, roomNearest;
    public int count, room_count, room_floor;
    public String room_name, room_type;
    public Geometry room_geom, room_centroid,room_transGeom;
    public PointData[] room_point;
    public double room_xmin, room_xmax, room_ymin, room_ymax, room_xcenter, room_ycenter, room_area;

    /*
     * Method name              :   IndoorGML_Room
     * Method description       :   Constructor containing the room details (default constructor)
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_Room() {
    }

    /*
     * Method name              :   IndoorGML_Construction_Room
     * Method description       :   Method to process the data in the required manner
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void IndoorGML_Construction_Room(List<IndoorGML_Room> room_list,List<PolygonShape> room_polygonList) throws Exception{
        List<PolygonShape> polyList = room_polygonList;
        List<IndoorGML_Room> roomList = room_list;
        for (int i = 0, j = 0; i < polyList.size() && j < roomList.size(); i++, j++) {
            PolygonShape psIn = polyList.get(i);
            IndoorGML_Room roomIn = roomList.get(j);
            ArrayList<Coordinate> points = new ArrayList<Coordinate>();
            roomIn.room_xmax = psIn.getBoxMaxX();
            roomIn.room_xmin = psIn.getBoxMinX();
            roomIn.room_ymin = psIn.getBoxMinY();
            roomIn.room_ymax = psIn.getBoxMaxY();
            roomIn.room_point = psIn.getPoints();
            points.add(new Coordinate(roomIn.room_xmin, roomIn.room_ymin));
            points.add(new Coordinate(roomIn.room_xmax, roomIn.room_ymin));
            points.add(new Coordinate(roomIn.room_xmax, roomIn.room_ymax));
            points.add(new Coordinate(roomIn.room_xmin, roomIn.room_ymax));
            points.add(new Coordinate(roomIn.room_xmin, roomIn.room_ymin));
            GeometryFactory gf = new GeometryFactory();
            Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);
            roomIn.room_area = polygon.getArea();
            roomIn.room_geom = polygon.getEnvelope();
            roomIn.room_transGeom=IndoorGML_File_Parser.shiftGeometry(roomIn.room_floor, roomIn.room_geom);
            roomIn.room_centroid = polygon.getCentroid();
            roomIn.room_xcenter = polygon.getCentroid().getX();
            roomIn.room_ycenter = polygon.getCentroid().getY();
            Rectangle rr = new Rectangle((float) roomIn.room_xmin, (float) roomIn.room_ymin, (float) roomIn.room_xmax, (float) roomIn.room_ymax);
            IndoorGML_File_Parser.rTree.add(rr, roomIn.count);
            IndoorGML_File_Parser.roomMap.put(roomIn.count, roomIn);
        }
    }

    /*
     * Method name              :   Set_Adjacent
     * Method description       :   Method to set the adjacent rooms of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (adjacentList)
     * Arguments description    :   "adjacentList"  --> List of adjacent rooms.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Adjacent(List<IndoorGML_Room> adjacentList) {

        List<IndoorGML_Room> listHere = new ArrayList<IndoorGML_Room>();
        /*
         for (IndoorGML_Room x : roomTemp) {
         listHere.add(x);
         }
         * */
        for (IndoorGML_Room x : adjacentList) {
            listHere.add(x);
        }
        roomAdjacent = listHere;
    }

    /*
     * Method name              :   Set_Contains
     * Method description       :   Method to set the contained rooms of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (containsList)
     * Arguments description    :   "containsList"  --> List of contained rooms.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Contains(List<IndoorGML_Room> containsList) {
        roomContains = containsList;
    }

    /*
     * Method name              :   Set_Connected
     * Method description       :   Method to set the connected rooms of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (connectedList)
     * Arguments description    :   "connectedList"  --> List of connected rooms.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Connected(List<IndoorGML_Room> connectedList) {
        roomConnected = connectedList;
    }

    /*
     * Method name              :   Set_Overlaps
     * Method description       :   Method to set the overlapping rooms of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (overlapsList)
     * Arguments description    :   "overlapsList"  --> List of overlapping rooms.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Overlaps(List<IndoorGML_Room> overlapsList) {
        roomOverlap = overlapsList;
    }

    /*
     * Method name              :   Set_Temp
     * Method description       :   Method to set the temporary adjacent rooms of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (tempList)
     * Arguments description    :   "tempList"  --> List of temporary adjacent rooms.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Temp(List<IndoorGML_Room> tempList) {
        roomTemp = tempList;
    }

    /*
     * Method name              :   Set_Door
     * Method description       :   Method to set the door of a particular room.
     * Method Arguments         :   java.util.List<IndoorGML_Room> (doorList)
     * Arguments description    :   "doorList"  --> List of door for the room.
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Set_Door(List<IndoorGML_Room> doorList) {
    }

    public void Set_Nearest(List<IndoorGML_Room> nearestList) {
        roomNearest = nearestList;
    }
}
