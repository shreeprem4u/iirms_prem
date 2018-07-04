package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : IndoorGML_File_Parser.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow)
 * Last Modified    : the 9th of October, 2014
 * Purpose          : Class to parse the information from .SHP and .DBF files
 */
import com.infomatiq.jsi.rtree.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import gnu.trove.TIntProcedure;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import nl.knaw.dans.common.dbflib.*;

import org.nocrala.tools.gis.data.esri.shapefile.*;
import org.nocrala.tools.gis.data.esri.shapefile.header.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;
import org.nocrala.tools.gis.data.esri.shapefile.shape.*;

public class IndoorGML_File_Parser {

    public static JFileChooser fileChooserObj;
    public static FileNameExtensionFilter fnef = new FileNameExtensionFilter("SHP and DBF files", "shp", "dbf");
    public static List<PolylineShape> wall_polyLineList = new ArrayList<PolylineShape>();
    public static java.util.List<PolylineShape> win_polyLineList = new ArrayList<PolylineShape>();
    public static java.util.List<PolylineShape> stairs_polyLineList = new ArrayList<PolylineShape>();
    public static List<MultiPointPlainShape> door_pointList = new ArrayList<MultiPointPlainShape>();
    public static List<PolygonShape> room_polygonList = new ArrayList<PolygonShape>();
    public static List<IndoorGML_Room> roomList = new ArrayList<IndoorGML_Room>();
    public static List<IndoorGML_Doors> doorsList = new ArrayList<IndoorGML_Doors>();
    public static List<IndoorGML_Walls> wallsList = new ArrayList<IndoorGML_Walls>();
    public static List<IndoorGML_Windows> windowsList = new ArrayList<IndoorGML_Windows>();
    public static List<IndoorGML_Stairs> stairsList=new ArrayList<IndoorGML_Stairs>();
    public static int roomcount = 1, doorcount = 1, wallcount = 1, count = 1, windowcount = 1,staircount=1;
    public static Map<Integer, IndoorGML_Room> roomMap = new HashMap<Integer, IndoorGML_Room>();
    public static Map<Integer, IndoorGML_Doors> doorMap = new HashMap<Integer, IndoorGML_Doors>();
    public static Map<Integer, IndoorGML_Walls> wallsMap = new HashMap<Integer, IndoorGML_Walls>();
    public static Map<Integer, IndoorGML_Windows> windowsMap = new HashMap<Integer, IndoorGML_Windows>();
    public static Map<Integer,IndoorGML_Stairs> stairsMap=new HashMap<Integer,IndoorGML_Stairs>();
    public static java.util.List<FloorDistance> floordistList = new ArrayList<>();
    public static RTree rTree;
    public static String filePathForIndoorGML = "";
    public static ArrayList<Integer> fIdList=new ArrayList<Integer>();
    public static double f1_coordX = 0,f1_coordY=0;

    /*
     * Method name              :   IndoorGML_File_Parser
     * Method description       :   Constructor to initialize the RTree and the fileChooser to select the required files.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IndoorGML_File_Parser() {
        try {
            rTree = new RTree();
            rTree.init(new Properties());
            System.out.println(rTree.toString());
            fileChooserObj = new JFileChooser("/home/Documents");
            DB_Create();
            
//             IndoorGML_Creation iGMLC = new IndoorGML_Creation(filePathForIndoorGML);
//             iGMLC.XML_Creation();
             
        } catch (Exception ex) {
        }
    }
    
    public static void DB_Create()throws Exception
    {
        //fileChooserObj = new JFileChooser("/home/Documents");
    	fileChooserObj.setFileFilter(fnef);
        fileChooserObj.setMultiSelectionEnabled(true);
        fileChooserObj.showOpenDialog(null);
        File[] fileList = fileChooserObj.getSelectedFiles();
        for (File fileNow : fileList)
        {
        	String fileId=fileNow.getName().replaceAll("\\D+", "");
        	if((fileId!=null) && !(fileId.isEmpty()))
        	{
        		int f_id=Integer.parseInt(fileId);
        		if(!fIdList.contains(f_id))
        		{
        			fIdList.add(f_id);
        		}
        	}
        }
        Collections.sort(fIdList);
        for(int i=0;i<fIdList.size();i++)
        {
            floorDistance(fIdList.get(i));
        }
        
        for (File fileNow : fileList) 
        {
            if (fileNow.getName().contains(".shp")) {
            	//System.out.println("shapeFile:"+fileNow.getName());
                shapeAddingProcess(fileNow);
            } else if (fileNow.getName().contains(".dbf")) {
            	//System.out.println("dbfFile:"+fileNow.getName());
                dataAddingProcess(fileNow);
            }
            filePathForIndoorGML = fileNow.getParent();
        }
        System.out.println(filePathForIndoorGML);
        IndoorGML_Room.IndoorGML_Construction_Room(roomList,room_polygonList);
        IndoorGML_Walls.IndoorGML_Construction_Wall(wallsList,wall_polyLineList);
        IndoorGML_Windows.IndoorGML_Construction_Window(windowsList,win_polyLineList);
        IndoorGML_Doors.IndoorGML_Construction_Door(doorsList,door_pointList);
        IndoorGML_Stairs.IndoorGML_Construction_Stair(stairsList,stairs_polyLineList);
    }
    
    
    /*
     * Method name              :   floorDistance
     * Method description       :   Method to find the distance between the first floor and the other floors.This is used 
     * 								for visualization to place one floor above the other
     * Method Arguments         :   (int) floor_id
     * Arguments description    :   floor_id       ---> floor_id of the floor for which the distance is to be calculated
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static void floorDistance(int floor_id) throws Exception {
    	
    	File[] fileList = fileChooserObj.getSelectedFiles();
        for (File fileNow : fileList) {
            String fid = fileNow.getName().replaceAll("\\D+", "");
            if ((fid != null) && !(fid.isEmpty())) {
                int fileId = Integer.parseInt(fid);
                if (fileId == floor_id && fileNow.getName().contains(".shp")) {
                    if ((fileNow.getName().contains("base"))) {
                        FileInputStream fileStream = new FileInputStream(fileNow);
                        ShapeFileReader sfReader = new ShapeFileReader(fileStream);
                        ShapeFileHeader sfHeader = sfReader.getHeader();
                        double xmin,ymin;
                        xmin = sfHeader.getBoxMinX();
                        ymin=sfHeader.getBoxMinY();
                        
                        if (floor_id == 1) {
                            FloorDistance fdObj = new FloorDistance();
                            f1_coordX=xmin;
                            f1_coordY=ymin;
                            fdObj.f_xdist = 0;//xmin;
                            fdObj.f_ydist=0;//ymin;
                            fdObj.f_id = floor_id;
                            floordistList.add(fdObj);
                        } else {
                            FloorDistance fdObj = new FloorDistance();
                            double f_coordX = xmin;
                            double f_coordY=ymin;
                            fdObj.f_xdist = f_coordX- f1_coordX;
                            fdObj.f_ydist=f_coordY-f1_coordY;
                            fdObj.f_id = floor_id;
                            floordistList.add(fdObj);
                        }
                    }
                }
            }
        }
    }
    
    /*
     * Method name              :   shiftGeometry
     * Method description       :   Method to shift the geometry points
     * Method Arguments         :   (int) floor_id,(Geometry) geom
     * Arguments description    :   floor_id        --->floor_id of the geometry that is to be transformed
     * Return type              :   Geometry
     * Return type description  :   geom           --->Returns the transformed geometry
     */
    public static Geometry shiftGeometry(int floorId,Geometry geom) throws Exception 
    {
    	Geometry new_geom = null;
    	//To iterate the list which contains the distance of each floor from the first floor
    	Iterator<FloorDistance> FLIterator = floordistList.iterator();
    	double xdist = 0d,ydist=0d;
        int id = 0;
        while (FLIterator.hasNext()) 
        {
            FloorDistance fObj = FLIterator.next();
            if (floorId == fObj.f_id) 
            {
                xdist = fObj.f_xdist;
                ydist = fObj.f_ydist;
            }
        }
        if(geom.toString().contains("POLYGON"))
    	{
    		String[] pointsArray= geom.toString().replace("POLYGON ","").replace("(","").replace(")", "").replace(", "," ").split(" ");
    		List<Coordinate> pointsList= new ArrayList<Coordinate>();
    		for(int i=0;i<pointsArray.length;i=i+2)
    		{
    			double xPoint = Double.parseDouble(pointsArray[i])-xdist;
    			double yPoint = Double.parseDouble(pointsArray[i+1])-ydist;
    			pointsList.add(new Coordinate(xPoint, yPoint));
    		}
    		GeometryFactory gf = new GeometryFactory();
    		Coordinate[] coordArray = pointsList.toArray(new Coordinate[pointsList.size()]);
    		Polygon poly = gf.createPolygon(new LinearRing(new CoordinateArraySequence(coordArray), gf), null);
    		new_geom=poly;
    	}
        
        else if(geom.toString().contains("LINESTRING"))
    	{
        	if(geom.toString().contains("MULTILINESTRING"))
        	{
        		String[] pointsArray= geom.toString().replace("MULTILINESTRING", "").replace(","," ").replace("(", "").replace(")", "").split(" ");
        		System.out.println("mls_trans:"+geom.toString().replace("MULTILINESTRING", "").replace(","," ").replace("(", "").replace(")", ""));
        		List<Coordinate> pointsList;
        		ArrayList<LineString> linestringList=new ArrayList<LineString>();
        		for(int i=0;i<pointsArray.length;i=i+4)
        		{
        			pointsList= new ArrayList<Coordinate>();
        			double xPoint1 = Double.parseDouble(pointsArray[i])-xdist;
        			double yPoint1 = Double.parseDouble(pointsArray[i+1])-ydist;
        			double xPoint2 = Double.parseDouble(pointsArray[i+2])-xdist;
        			double yPoint2 = Double.parseDouble(pointsArray[i+3])-ydist;
        			pointsList.add(new Coordinate(xPoint1, yPoint1));
        			pointsList.add(new Coordinate(xPoint2, yPoint2));
        			GeometryFactory gf = new GeometryFactory();
            		Coordinate[] coordArray = pointsList.toArray(new Coordinate[pointsList.size()]);
            		LineString ls = gf.createLineString(coordArray);
            		linestringList.add(ls);
        		}
        		GeometryFactory gf1 = new GeometryFactory();
                LineString[] lineArray=linestringList.toArray(new LineString[linestringList.size()]);
                MultiLineString mls=gf1.createMultiLineString(lineArray);
        		new_geom=mls;
        	}
        	else
        	{
        		String[] pointsArray= geom.toString().replace("LINESTRING", "").replace(","," ").replace("(", "").replace(")", "").split(" ");
        		List<Coordinate> pointsList= new ArrayList<Coordinate>();
        		for(int i=0;i<pointsArray.length;i=i+2)
        		{
        			double xPoint = Double.parseDouble(pointsArray[i])-xdist;
        			double yPoint = Double.parseDouble(pointsArray[i+1])-ydist;
        			pointsList.add(new Coordinate(xPoint, yPoint));
        		}
        		GeometryFactory gf = new GeometryFactory();
        		Coordinate[] coordArray = pointsList.toArray(new Coordinate[pointsList.size()]);
        		LineString ls = gf.createLineString(coordArray);
        		new_geom=ls;
        	}
    	}
        return new_geom;
    }
    

    /*
     * Method name              :   shapeAddingProcess
     * Method description       :   Method to add the details of the shapes in the shapefiles to corresponding lists.
     * Method Arguments         :   File (fileForProcess)
     * Arguments description    :   "fileForProcess"    --> File to be read for shapes
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void shapeAddingProcess(File fileForProcess) throws Exception {
        FileInputStream fileStream = new FileInputStream(fileForProcess);
        ShapeFileReader sfReader = new ShapeFileReader(fileStream);
        ShapeFileHeader sfHeader = sfReader.getHeader();
        
        if (fileForProcess.getName().toLowerCase().contains("wall"))
        {
            AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolylineShape pls = (PolylineShape) absShape;
            	wall_polyLineList.add(pls);
            }
        }
        if(fileForProcess.getName().toLowerCase().contains("room"))
        {
            AbstractShape absShape;
            while((absShape=sfReader.next())!=null)
            {
                PolygonShape pgs = (PolygonShape) absShape;
                room_polygonList.add(pgs);
            }
        }
        if (fileForProcess.getName().toLowerCase().contains("stair")) 
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
               PolylineShape pls = (PolylineShape) absShape;
               stairs_polyLineList.add(pls);
            }
        }
       
        if(fileForProcess.getName().toLowerCase().contains("win"))
        {
        	AbstractShape absShape;
            while ((absShape = sfReader.next()) != null) 
            {
            	PolylineShape pls = (PolylineShape) absShape;
            	win_polyLineList.add(pls);
            }
        }
        
        if((fileForProcess.getName().toLowerCase().contains("ex"))||(fileForProcess.getName().contains("exit")))
        {
        	AbstractShape absShape;
        	while((absShape=sfReader.next())!=null)
        	{
        		MultiPointPlainShape mpps=(MultiPointPlainShape) absShape;
        		door_pointList.add(mpps);
        	}
        }
        
        
        /*AbstractShape absShape;
        while ((absShape = sfReader.next()) != null) {
            switch (absShape.getShapeType()) {
                case POLYLINE:
                    PolylineShape pls = (PolylineShape) absShape;
                    polyLineList.add(pls);
                    break;

                case POLYGON:
                    PolygonShape pgs = (PolygonShape) absShape;
                    polygonList.add(pgs);
                    break;

                case MULTIPOINT:
                    MultiPointPlainShape mpps = (MultiPointPlainShape) absShape;
                    pointList.add(mpps);
                    break;
            }
        }*/
    }

    /*
     * Method name              :   dataAddingProcess
     * Method description       :   Method to add the details of the data in the DBF files to corresponding lists.
     * Method Arguments         :   File (fileForProcess)
     * Arguments description    :   "fileForProcess"    --> File to be read for data
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void dataAddingProcess(File fileForProcess) throws Exception {
        Table tableIn = new Table(fileForProcess);
        tableIn.open(IfNonExistent.ERROR);
        List<Field> fieldList = tableIn.getFields();

        //Write the code to get the records individually
        Iterator<Record> recordIterator = tableIn.recordIterator();

        if (fileForProcess.getName().toLowerCase().contains("room")) {
            while (recordIterator.hasNext()) {
                IndoorGML_Room iRoom = new IndoorGML_Room();
                Record record = recordIterator.next();
                iRoom.room_count = roomcount;
                iRoom.count = count;
                for (Field field : fieldList) {
                    try {
                        if ((field.getName().toLowerCase().contains("roomname")) || (field.getName().toLowerCase().contains("room_name"))) {
                            iRoom.room_name = new String(record.getRawValue(field));
                        } else if (field.getName().toLowerCase().contains("floor")) {
                            String floorString = new String(record.getRawValue(field)).trim();
                            iRoom.room_floor = Integer.parseInt(floorString);
                        } else if ((field.getName().toLowerCase().equals("type")) || (field.getName().toLowerCase().contains("roomtype"))) {
                            iRoom.room_type = new String(record.getRawValue(field));
                        }
                    } catch (ValueTooLargeException vtle) {
                        System.out.println("Exception here");
                    }
                }
                if ((iRoom.room_name == null) || (iRoom.room_name.equals(""))) {
                    iRoom.room_name = "Room_Name" + count;
                }
                iRoom.room_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                roomcount += 1;
                count += 1;
                roomList.add(iRoom);
            }
        } else if (fileForProcess.getName().toLowerCase().contains("ex")) {
            while (recordIterator.hasNext()) {
                IndoorGML_Doors iDoor = new IndoorGML_Doors();
                Record record = recordIterator.next();
                iDoor.count = count;
                iDoor.door_count = doorcount;
                for (Field field : fieldList) {
                    try {
                        if (field.getName().toLowerCase().contains("floor")) {
                            String inFloor = new String(record.getRawValue(field)).trim();
                            iDoor.door_floor = Integer.parseInt(inFloor);
                        }else if(field.getName().toLowerCase().contains("zonal")){
                        	String inZonal=new String(record.getRawValue(field)).trim();
                        	iDoor.zonalConstraint=inZonal;
                        }else if(field.getName().toLowerCase().contains("temporal")){
                        	String inTemporal=new String(record.getRawValue(field)).trim();
                        	iDoor.temporalConstraint=inTemporal;
                        }
                    } catch (ValueTooLargeException vtle2) {
                        System.out.println("Exception here too!");
                    }
                }
                iDoor.door_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                doorcount += 1;
                count += 1;
                doorsList.add(iDoor);
            }
        } else if (fileForProcess.getName().toLowerCase().contains("wall")) {
            while (recordIterator.hasNext()) {
                IndoorGML_Walls iWall = new IndoorGML_Walls();
                Record record = recordIterator.next();
                iWall.count = count;
                iWall.wall_count = wallcount;
                for (Field field : fieldList) {
                    try {
                        if (field.getName().toLowerCase().contains("floor")) {
                            String inFloor = new String(record.getRawValue(field)).trim();
                            iWall.wall_floor = Integer.parseInt(inFloor);
                        }
                    } catch (ValueTooLargeException vtle1) {
                        System.out.println("Exception here too!");
                    }
                }
                iWall.wall_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                wallcount += 1;
                count += 1;
                wallsList.add(iWall);
            }
        } else if (fileForProcess.getName().toLowerCase().contains("win")) {
            while (recordIterator.hasNext()) {
                IndoorGML_Windows iWindow = new IndoorGML_Windows();
                Record record = recordIterator.next();
                iWindow.count = count;
                iWindow.window_count = windowcount;
                for (Field field : fieldList) {
                    try {
                        if (field.getName().toLowerCase().contains("floor")) {
                            String inFloor = new String(record.getRawValue(field)).trim();
                            if (!inFloor.equals("")) {
                                iWindow.window_floor = Integer.parseInt(inFloor);
                            }
                        }
                    } catch (ValueTooLargeException vtle1) {
                        System.out.println("Exception here too!");
                    }
                }
                iWindow.window_floor = Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
                windowcount += 1;
                count += 1;
                windowsList.add(iWindow);
            }
        }else if(fileForProcess.getName().toLowerCase().contains("stair")){
        	while(recordIterator.hasNext()){
        		IndoorGML_Stairs iStair=new IndoorGML_Stairs();
        		Record record=recordIterator.next();
        		iStair.count=count;
        		iStair.stair_count=staircount;
        		for(Field field:fieldList){
        			try{
        				if(field.getName().toLowerCase().contains("floor")){
        					String inFloor=new String(record.getRawValue(field)).trim();
        					if(!inFloor.equals("")){
        						iStair.stair_floor=Integer.parseInt(inFloor);
        					}
        				}
        			}catch(ValueTooLargeException vtle1){
        				System.out.println("Exception here too!");
        			}
        		}
        		iStair.stair_floor=Integer.parseInt(fileForProcess.getName().replaceAll("\\D+", ""));
        		staircount+=1;
        		count+=1;
        		stairsList.add(iStair);
        	}
        }
    }
}

class FloorDistance{
	double f_xdist;
    double f_ydist;
    int f_id;
}

class ListIntProcedure implements TIntProcedure {

    ArrayList solution = new ArrayList();

    /*
     * Method name              :   execute
     * Method description       :   Overriding the existing execute() method of TIntProcedure interface, 
     *                              to add the nearest neighbors.
     * Method Arguments         :   int (arg0)
     * Arguments description    :   "arg0"      --> ID of the entry identified as nearest.
     * Return type              :   boolean (true/false)
     * Return type description  :   "true"      --> added,
     *                              "false"     --> not added
     */
    @Override
    public boolean execute(int arg0) 
    {
        solution.add(new Integer(arg0));
        return true;
    }

    /*
     * Method name              :   getSolution
     * Method description       :   Method to get the list of nearest neighbors.
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   java.util.List (solution)
     * Return type description  :   "solution"  --> List of nearest entries.
     */
    public List getSolution() {
        return solution;
    }
}